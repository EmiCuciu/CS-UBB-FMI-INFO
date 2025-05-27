using System.Configuration;
using System.Data;
using System.Data.SqlClient;

namespace Practic;

public partial class Form1 : Form
{
    private static string _con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private static DataSet _dataset;

    private static SqlDataAdapter _grupaAdapter;
    private static SqlDataAdapter _copiiAdapter;

    private static BindingSource _grupaBindingSource;
    private static BindingSource _copiiBindingSource;

    private static SqlCommandBuilder _grupaCommandBuilder;
    private static SqlCommandBuilder _copiiCommandBuilder;
    public Form1()
    {
        InitializeComponent();
        InitializeDataComponents();
        LoadData();
    }

    private void InitializeDataComponents()
    {
        _dataset = new DataSet();
        _grupaBindingSource = new BindingSource();
        _copiiBindingSource = new BindingSource();

        _grupaAdapter = new SqlDataAdapter("SELECT * FROM Grupa", _con);
        _grupaCommandBuilder = new SqlCommandBuilder(_grupaAdapter);

        _copiiAdapter = new SqlDataAdapter("SELECT * FROM Copii", _con);
        _copiiCommandBuilder= new SqlCommandBuilder(_copiiAdapter);

    }

    private void LoadData()
    {
        _grupaAdapter.Fill(_dataset, "Grupa");

        _copiiAdapter.Fill(_dataset, "Copii");

        DataRelation relation = new DataRelation(
            "Grupa_Copii",
            _dataset.Tables["Grupa"].Columns["cod_grupa"],
            _dataset.Tables["Copii"].Columns["cod_grupa"]);

        _dataset.Relations.Add(relation);

        _grupaBindingSource.DataSource = _dataset;
        _grupaBindingSource.DataMember = "Grupa";

        _copiiBindingSource.DataSource = _grupaBindingSource;
        _copiiBindingSource.DataMember = "Grupa_Copii";

        // Legare grid-uri la surse de date
        GrupaDataGridView.DataSource = _grupaBindingSource;
        CopiiDataGridView.DataSource = _copiiBindingSource;
    }

    private void Add_Click(object sender, EventArgs e)
    {
        try
        {
            DataRow newRow = _dataset.Tables["Copii"].NewRow();
            newRow["nume"] = nume.Text;
            newRow["varsta"] = decimal.Parse(varsta.Text);
            newRow["cod_grupa"] = cod_grupa.Text;

            _dataset.Tables["Copii"].Rows.Add(newRow);
            _copiiAdapter.Update(_dataset, "Copii");

            _dataset.Tables["Copii"].Clear();
            _copiiAdapter.Fill(_dataset, "Copii");

            MessageBox.Show("Copil added successfully.");
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error adding copil: " + ex.Message);
        }
    }

    private void Sterge_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Are you sure you want to delete this copil?", "Delete", MessageBoxButtons.YesNo) == DialogResult.Yes)
        {
            try
            {
                _copiiBindingSource.RemoveCurrent();
                _copiiAdapter.Update(_dataset, "Copii");

                _dataset.Tables["Copii"].Clear();
                _copiiAdapter.Fill(_dataset, "Copii");

                MessageBox.Show("Copil deleted successfully.");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error deleting Copil: " + ex.Message);
            }
        }
    }

    private void Modifica_Click(object sender, EventArgs e)
    {
        try
        {
            if (CopiiDataGridView.CurrentRow == null)
            {
                MessageBox.Show("Please select a Copil to update.");
                return;
            }

            DataRowView currentRow = (DataRowView)_copiiBindingSource.Current;

            currentRow["nume"] = nume.Text;
            currentRow["varsta"] = decimal.Parse(varsta.Text);

            _copiiBindingSource.EndEdit();
            _copiiAdapter.Update(_dataset, "Copii");

            _dataset.Tables["Copii"].Clear();
            _copiiAdapter.Fill(_dataset, "Copii");

            MessageBox.Show("Melodie updated successfully.");
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error updating Melodie: " + ex.Message);
        }
    }
}
