using System.Configuration;
using System.Data;
using System.Data.SqlClient;

namespace Problema3;

public partial class Form1 : Form
{
    private static string _con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private static DataSet _dataset;

    private static SqlDataAdapter _producatorAdapter;
    private static SqlDataAdapter _biscuitiAdapter;

    private static BindingSource _producatorBindingSource;
    private static BindingSource _biscuitiBindingSource;

    private static SqlCommandBuilder _producatorCommandBuilder;
    private static SqlCommandBuilder _biscuitiCommandBuilder;
    public Form1()
    {
        InitializeComponent();
        InitializeDataComponents();
        LoadData();
    }

    private void InitializeDataComponents()
    {
        _dataset = new DataSet();
        _producatorBindingSource = new BindingSource();
        _biscuitiBindingSource = new BindingSource();

        _producatorAdapter = new SqlDataAdapter("SELECT * FROM Producatori", _con);
        _producatorCommandBuilder = new SqlCommandBuilder(_producatorAdapter);
        _producatorAdapter.InsertCommand = _producatorCommandBuilder.GetInsertCommand();
        _producatorAdapter.UpdateCommand = _producatorCommandBuilder.GetUpdateCommand();
        _producatorAdapter.DeleteCommand = _producatorCommandBuilder.GetDeleteCommand();

        // Configurare adapter pentru Comenzi_Clienti
        _biscuitiAdapter = new SqlDataAdapter("SELECT * FROM Biscuiti", _con);
        _biscuitiCommandBuilder= new SqlCommandBuilder(_biscuitiAdapter);
        _biscuitiAdapter.InsertCommand = _biscuitiCommandBuilder.GetInsertCommand();
        _biscuitiAdapter.UpdateCommand = _biscuitiCommandBuilder.GetUpdateCommand();
        _biscuitiAdapter.DeleteCommand = _biscuitiCommandBuilder.GetDeleteCommand();
    }

    private void LoadData()
    {
        _producatorAdapter.Fill(_dataset, "Producatori");

        _biscuitiAdapter.Fill(_dataset, "Biscuiti");

        DataRelation relation = new DataRelation(
            "Producatori_Biscuiti",
            _dataset.Tables["Producatori"].Columns["cod_p"],
            _dataset.Tables["Biscuiti"].Columns["cod_p"]);

        _dataset.Relations.Add(relation);

        _producatorBindingSource.DataSource = _dataset;
        _producatorBindingSource.DataMember = "Producatori";

        _biscuitiBindingSource.DataSource = _producatorBindingSource;
        _biscuitiBindingSource.DataMember = "Producatori_Biscuiti";

        // Legare grid-uri la surse de date
        ProducatoriDataGridView.DataSource = _producatorBindingSource;
        BiscuitiDataGridView.DataSource = _biscuitiBindingSource;
    }
    private void Add_Click(object sender, EventArgs e)
    {
        try
        {
            DataRow newRow = _dataset.Tables["Biscuiti"].NewRow();
            newRow["nume_b"] = nume_b.Text;
            newRow["nr_calorii"] = decimal.Parse(nr_calorii.Text);
            newRow["pret"] = decimal.Parse(Pret.Text);
            newRow["cod_p"] = cod_prod.Text;

            _dataset.Tables["Biscuiti"].Rows.Add(newRow);
            _biscuitiAdapter.Update(_dataset, "Biscuiti");

            _dataset.Tables["Biscuiti"].Clear();
            _biscuitiAdapter.Fill(_dataset, "Biscuiti");

            MessageBox.Show("Melodie added successfully.");
            // ClearFields();
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding melodie: " + ex.Message);
        }
    }

    private void Sterge_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Are you sure you want to delete this melodie?", "Delete", MessageBoxButtons.YesNo) == DialogResult.Yes)
        {
            try
            {
                _biscuitiBindingSource.RemoveCurrent();
                _biscuitiAdapter.Update(_dataset, "Biscuiti");

                _dataset.Tables["Biscuiti"].Clear();
                _biscuitiAdapter.Fill(_dataset, "Biscuiti");

                MessageBox.Show("Melodie deleted successfully.");
                // ClearFields();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error deleting Melodie: " + ex.Message);
            }
        }
    }

    private void Modifica_Click(object sender, EventArgs e)
    {
        try
        {
            if (BiscuitiDataGridView.CurrentRow == null)
            {
                MessageBox.Show("Please select a Melodie to update.");
                return;
            }

            DataRowView currentRow = (DataRowView)_biscuitiBindingSource.Current;

            currentRow["nume_b"] = nume_b.Text;
            currentRow["nr_calorii"] = decimal.Parse(nr_calorii.Text);
            currentRow["pret"] = decimal.Parse(Pret.Text);

            _biscuitiBindingSource.EndEdit();
            _biscuitiAdapter.Update(_dataset, "Biscuiti");

            _dataset.Tables["Biscuiti"].Clear();
            _biscuitiAdapter.Fill(_dataset, "Biscuiti");

            MessageBox.Show("Melodie updated successfully.");
            // ClearFields();
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error updating Melodie: " + ex.Message);
        }
    }

}