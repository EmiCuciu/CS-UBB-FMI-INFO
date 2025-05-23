using System.Configuration;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;

namespace Problema1;

public partial class Briose : Form
{
    private static string _con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private static DataSet _dataset;

    private static SqlDataAdapter _cofetarieAdapter;
    private static SqlDataAdapter _brioseAdapter;

    private static BindingSource _cofetarieBindingSource;
    private static BindingSource _brioseBindingSource;

    private static SqlCommandBuilder _cofetarieCommandBuilder;
    private static SqlCommandBuilder _brioseCommandBuilder;
    public Briose()
    {
        InitializeComponent();
        InitializeDataComponents();
        LoadData();
    }

    private void InitializeDataComponents()
    {
        _dataset = new DataSet();
        _cofetarieBindingSource = new BindingSource();
        _brioseBindingSource = new BindingSource();

        // Configurare adapter pentru Clienti
        _cofetarieAdapter = new SqlDataAdapter("SELECT * FROM Cofetarii", _con);
        _cofetarieCommandBuilder = new SqlCommandBuilder(_cofetarieAdapter);
        _cofetarieAdapter.InsertCommand = _cofetarieCommandBuilder.GetInsertCommand();
        _cofetarieAdapter.UpdateCommand = _cofetarieCommandBuilder.GetUpdateCommand();
        _cofetarieAdapter.DeleteCommand = _cofetarieCommandBuilder.GetDeleteCommand();

        // Configurare adapter pentru Comenzi_Clienti
        _brioseAdapter = new SqlDataAdapter("SELECT * FROM Briose", _con);
        _brioseCommandBuilder= new SqlCommandBuilder(_brioseAdapter);
        _brioseAdapter.InsertCommand = _brioseCommandBuilder.GetInsertCommand();
        _brioseAdapter.UpdateCommand = _brioseCommandBuilder.GetUpdateCommand();
        _brioseAdapter.DeleteCommand = _brioseCommandBuilder.GetDeleteCommand();
    }

    private void LoadData()
    {
        _cofetarieAdapter.Fill(_dataset, "Cofetarii");

        _brioseAdapter.Fill(_dataset, "Briose");

        DataRelation relation = new DataRelation(
            "Cofetarii_Briose",
            _dataset.Tables["Cofetarii"].Columns["cod_cofetarie"],
            _dataset.Tables["Briose"].Columns["cod_cofetarie"]);

        _dataset.Relations.Add(relation);

        _cofetarieBindingSource.DataSource = _dataset;
        _cofetarieBindingSource.DataMember = "Cofetarii";

        _brioseBindingSource.DataSource = _cofetarieBindingSource;
        _brioseBindingSource.DataMember = "Cofetarii_Briose";

        // Legare grid-uri la surse de date
        CofetariiDataGridView.DataSource = _cofetarieBindingSource;
        BrioseDataGridView.DataSource = _brioseBindingSource;

        ConfigureEditControls();
    }

    private void ConfigureEditControls()
    {
        BrioseDataGridView.SelectionChanged += (s, e) =>
        {
            if (BrioseDataGridView.CurrentRow != null)
            {
                DataRowView row = (DataRowView)_brioseBindingSource.Current;
                nume_briosa.Text = row["nume_briosa"].ToString();
                descriere.Text = row["descriere"].ToString();
                pret.Text = row["pret"].ToString();
                cod_cofetarie.Text = row["cod_cofetarie"].ToString();
            }
        };
    }

    private void Add_briosa_Click(object sender, EventArgs e)
    {
        try
        {
            DataRow newRow = _dataset.Tables["Briose"].NewRow();
            newRow["nume_briosa"] = nume_briosa.Text;
            newRow["descriere"] = descriere.Text;
            newRow["pret"] = decimal.Parse(pret.Text);
            newRow["cod_cofetarie"] = cod_cofetarie.Text;

            _dataset.Tables["Briose"].Rows.Add(newRow);
            _brioseAdapter.Update(_dataset, "Briose");

            _dataset.Tables["Briose"].Clear();
            _brioseAdapter.Fill(_dataset, "Briose");

            MessageBox.Show("Briosa added successfully.");
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding briosa: " + ex.Message);
        }
    }

    private void Delete_briosa_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Are you sure you want to delete this briosa?", "Delete", MessageBoxButtons.YesNo) == DialogResult.Yes)
        {
            try
            {
                _brioseBindingSource.RemoveCurrent();
                _brioseAdapter.Update(_dataset, "Briose");

                _dataset.Tables["Briose"].Clear();
                _brioseAdapter.Fill(_dataset, "Briose");

                MessageBox.Show("Briosa deleted successfully.");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error deleting briosa: " + ex.Message);
            }
        }
    }


    private void Update_briosa_Click(object sender, EventArgs e)
    {
        try
        {
            if (BrioseDataGridView.CurrentRow == null)
            {
                MessageBox.Show("Please select a briosa to update.");
                return;
            }

            DataRowView currentRow = (DataRowView)_brioseBindingSource.Current;

            currentRow["nume_briosa"] = nume_briosa.Text;
            currentRow["descriere"] = descriere.Text;
            currentRow["pret"] = decimal.Parse(pret.Text);

            _brioseBindingSource.EndEdit();
            _brioseAdapter.Update(_dataset, "Briose");

            _dataset.Tables["Briose"].Clear();
            _brioseAdapter.Fill(_dataset, "Briose");

            MessageBox.Show("Briosa updated successfully.");
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error updating briosa: " + ex.Message);
        }
    }
}