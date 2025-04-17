using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Windows.Forms;

namespace Laborator1
{
    public partial class MagazinTelefoane : Form
    {
        private static string _con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
        private static DataSet _dataset = new DataSet();
        private static SqlDataAdapter _parentAdapter = new SqlDataAdapter();
        private static SqlDataAdapter _childAdapter = new SqlDataAdapter();
        private static BindingSource _parentBindingSource = new BindingSource();
        private static BindingSource _childBindingSource = new BindingSource();
        private static SqlCommandBuilder _parentCommandBuilder = new SqlCommandBuilder();
        private static SqlCommandBuilder _childCommandBuilder = new SqlCommandBuilder();
        SqlConnection _sqlConnection = new SqlConnection(_con);
        private static string _parentTable = ConfigurationManager.AppSettings.Get("parentTable");
        private static string _parentPrimaryKey = ConfigurationManager.AppSettings.Get("parentPrimaryKey");
        private static string _childTable = ConfigurationManager.AppSettings.Get("childTable");
        private static string _childForeignKey = ConfigurationManager.AppSettings.Get("childForeignKey");

        public MagazinTelefoane()
        {
            InitializeComponent();
        }

        private void ConnectBtn_Click(object sender, EventArgs e)
        {
            try
            {
                string selectParent = ConfigurationManager.AppSettings.Get("selectParent");
                string selectChild = ConfigurationManager.AppSettings.Get("selectChild");

                _parentAdapter.SelectCommand = new SqlCommand(selectParent, _sqlConnection);
                _childAdapter.SelectCommand = new SqlCommand(selectChild, _sqlConnection);

                _parentCommandBuilder = new SqlCommandBuilder(_parentAdapter);
                _childCommandBuilder = new SqlCommandBuilder(_childAdapter);

                _dataset.Clear();
                _parentAdapter.Fill(_dataset, _parentTable);
                _childAdapter.Fill(_dataset, _childTable);

                DataColumn parentPK = _dataset.Tables[_parentTable].Columns[_parentPrimaryKey];
                DataColumn childFK = _dataset.Tables[_childTable].Columns[_childForeignKey];

                DataRelation relation = new DataRelation("FK_" + _parentTable + "_" + _childTable, parentPK, childFK);
                _dataset.Relations.Add(relation);

                _parentBindingSource.DataSource = _dataset;
                _parentBindingSource.DataMember = _parentTable;

                _childBindingSource.DataSource = _parentBindingSource;
                _childBindingSource.DataMember = "FK_" + _parentTable + "_" + _childTable;

                Parent_dataGridView.DataSource = _parentBindingSource;
                Child_dataGridView.DataSource = _childBindingSource;

                // Generate controls based on child table columns
                GenerateControls();
            }
            catch (Exception exception)
            {
                MessageBox.Show("Eroare la conectare: " + exception.Message);
            }
        }

        private void GenerateControls()
        {
            // Clear existing controls
            panel1.Controls.Clear();

            // Get the child table configuration
            string childColumnNames = ConfigurationManager.AppSettings.Get("childColumnNames");
            string[] columnNames = childColumnNames.Split(',');

            // Iterate through the columns of the child table
            for (int i = 0; i < columnNames.Length; i++)
            {
                string columnName = columnNames[i].Trim();
                Control control;

                // Check if the column is the foreign key or not
                if (columnName == _childForeignKey)
                {
                    continue; // Skip the foreign key column
                }

                // Check the data type and create appropriate control (TextBox or DateTimePicker)
                if (columnName.Contains("Data") || columnName.ToLower().Contains("date"))
                {
                    control = new DateTimePicker(); // If the column is related to date, create DateTimePicker
                }
                else
                {
                    control = new TextBox(); // Otherwise, create a TextBox
                }

                // Set control properties
                control.Name = "control" + columnName;
                control.Location = new Point(10, 30 * i);
                control.Size = new Size(200, 22);

                // Add a label for the control
                Label label = new Label();
                label.Text = columnName;
                label.Location = new Point(220, 30 * i);
                label.Size = new Size(100, 22);

                // Add the control and label to the panel
                panel1.Controls.Add(control);
                panel1.Controls.Add(label);
            }
        }


        private void AdaugaBtn_Click(object sender, EventArgs e)
        {
            try
            {
                string childTable = ConfigurationManager.AppSettings.Get("childTable");
                string childColumnNames = ConfigurationManager.AppSettings.Get("childColumnNames");
                string childColumnNamesInsertParameters =
                    ConfigurationManager.AppSettings.Get("childColumnNamesInsertParamenters");

                List<string> columnNamesList = new List<string>(childColumnNames.Split(','));
                List<string> columnParametersList = new List<string>(childColumnNamesInsertParameters.Split(','));

                // Create the SQL command for the INSERT operation
                SqlCommand sqlCommand =
                    new SqlCommand(
                        $"INSERT INTO {childTable} ({childColumnNames}) VALUES ({childColumnNamesInsertParameters})",
                        _sqlConnection);

                // Loop through the columns and parameters
                for (int i = 0; i < columnNamesList.Count; i++)
                {
                    string columnName = columnNamesList[i].Trim();
                    string parameterName = columnParametersList[i].Trim();

                    // If the column is the foreign key, set the value from the selected parent row
                    if (columnName == _childForeignKey)
                    {
                        DataRowView parentRowView = (DataRowView)_parentBindingSource.Current;
                        sqlCommand.Parameters.AddWithValue(parameterName, parentRowView[_parentPrimaryKey]);
                    }
                    else
                    {
                        // If it's a regular field, get the value from the respective control
                        Control control = panel1.Controls["control" + columnName];
                        if (control is TextBox textBox)
                        {
                            sqlCommand.Parameters.AddWithValue(parameterName, textBox.Text);
                        }
                        else if (control is DateTimePicker dateTimePicker)
                        {
                            if (dateTimePicker.Value.Date > DateTime.Now.Date)
                            {
                                throw new Exception("Data selectată nu poate fi mai veche decât data curentă.");
                            }

                            sqlCommand.Parameters.AddWithValue(parameterName, dateTimePicker.Value);
                        }

                    }
                }

                // Execute the command to insert data
                _sqlConnection.Open();
                sqlCommand.ExecuteNonQuery();
                _sqlConnection.Close();

                // Refresh the dataset after inserting data
                _dataset.Clear();
                _parentAdapter.Fill(_dataset, _parentTable);
                _childAdapter.Fill(_dataset, _childTable);

                MessageBox.Show("Date adăugate cu succes");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare la adăugarea datelor: " + ex.Message);
                _sqlConnection.Close();
            }
        }

        private void StergeBtn_Click(object sender, EventArgs e)
        {
            try
            {
                if (_childBindingSource.Current == null)
                {
                    MessageBox.Show("Nu există un rând selectat pentru ștergere.");
                    return;
                }

                if (MessageBox.Show("Sigur doriți să ștergeți înregistrarea selectată?", "Confirmare ștergere",
                        MessageBoxButtons.YesNo, MessageBoxIcon.Question) != DialogResult.Yes)
                {
                    return;
                }

                DataRowView childRowView = (DataRowView)_childBindingSource.Current;
                string childPrimaryKey = ConfigurationManager.AppSettings.Get("childPrimaryKey");
                object primaryKeyValue = childRowView[childPrimaryKey];

                // Create DELETE command
                string deleteQuery = $"DELETE FROM {_childTable} WHERE {childPrimaryKey} = @id";
                SqlCommand sqlCommand = new SqlCommand(deleteQuery, _sqlConnection);
                sqlCommand.Parameters.AddWithValue("@id", primaryKeyValue);

                _sqlConnection.Open();
                sqlCommand.ExecuteNonQuery();
                _sqlConnection.Close();

                // Refresh the dataset
                _dataset.Clear();
                _parentAdapter.Fill(_dataset, _parentTable);
                _childAdapter.Fill(_dataset, _childTable);

                MessageBox.Show("Date șterse cu succes");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare la ștergerea datelor: " + ex.Message);
                if (_sqlConnection.State == ConnectionState.Open)
                    _sqlConnection.Close();
            }
        }


        private void UpdateBtn_Click(object sender, EventArgs e)
        {
            try
            {
                if (_childBindingSource.Current == null)
                {
                    MessageBox.Show("Nu există un rând selectat pentru actualizare.");
                    return;
                }

                DataRowView childRowView = (DataRowView)_childBindingSource.Current;
                string childPrimaryKey = ConfigurationManager.AppSettings.Get("childPrimaryKey");

                // Get the UpdateQuery from configuration
                string updateQuery = ConfigurationManager.AppSettings.Get("UpdateQuery");
                SqlCommand sqlCommand = new SqlCommand(updateQuery, _sqlConnection);

                // Get the primary key value of the selected row
                object primaryKeyValue = childRowView[childPrimaryKey];
                sqlCommand.Parameters.AddWithValue("@" + childPrimaryKey.ToLower(), primaryKeyValue);

                // Get column names from configuration
                string childColumnNames = ConfigurationManager.AppSettings.Get("childColumnNames");
                List<string> columnNamesList = new List<string>(childColumnNames.Split(','));

                // Loop through each column and add parameter values
                foreach (string columnName in columnNamesList)
                {
                    string trimmedColumnName = columnName.Trim();
                    string parameterName = "@" + trimmedColumnName.ToLower();

                    // Handle foreign key
                    if (trimmedColumnName == _childForeignKey)
                    {
                        DataRowView parentRowView = (DataRowView)_parentBindingSource.Current;
                        sqlCommand.Parameters.AddWithValue(parameterName, parentRowView[_parentPrimaryKey]);
                    }
                    else
                    {
                        Control control = panel1.Controls["control" + trimmedColumnName];
                        if (control is TextBox textBox)
                        {
                            sqlCommand.Parameters.AddWithValue(parameterName, textBox.Text);
                        }
                        else if (control is DateTimePicker dateTimePicker)
                        {
                            sqlCommand.Parameters.AddWithValue(parameterName, dateTimePicker.Value);
                        }
                    }
                }

                _sqlConnection.Open();
                sqlCommand.ExecuteNonQuery();
                _sqlConnection.Close();

                // Refresh the dataset
                _dataset.Clear();
                _parentAdapter.Fill(_dataset, _parentTable);
                _childAdapter.Fill(_dataset, _childTable);

                MessageBox.Show("Date actualizate cu succes");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Eroare la actualizarea datelor: " + ex.Message);
                if (_sqlConnection.State == ConnectionState.Open)
                    _sqlConnection.Close();
            }
        }
    }
}