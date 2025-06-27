using System.Configuration;
using System.Data;
using System.Data.SqlClient;

namespace Problema2;

public partial class Form1 : Form
{
    private static string _con = ConfigurationManager.ConnectionStrings["cn"].ConnectionString;
    private static DataSet _dataset;

    private static SqlDataAdapter _artistAdapter;
    private static SqlDataAdapter _melodieAdapter;

    private static BindingSource _artistBindingSource;
    private static BindingSource _melodieBindingSource;

    private static SqlCommandBuilder _artistiCommandBuilder;
    private static SqlCommandBuilder _melodieCommandBuilder;
    public Form1()
    {
        InitializeComponent();
        InitializeDataComponents();
        LoadData();
    }

    private void InitializeDataComponents()
    {
        _dataset = new DataSet();
        _artistBindingSource = new BindingSource();
        _melodieBindingSource = new BindingSource();

        _artistAdapter = new SqlDataAdapter("SELECT * FROM Artisti", _con);
        _artistiCommandBuilder = new SqlCommandBuilder(_artistAdapter);
        _artistAdapter.InsertCommand = _artistiCommandBuilder.GetInsertCommand();
        _artistAdapter.UpdateCommand = _artistiCommandBuilder.GetUpdateCommand();
        _artistAdapter.DeleteCommand = _artistiCommandBuilder.GetDeleteCommand();

        // Configurare adapter pentru Comenzi_Clienti
        _melodieAdapter = new SqlDataAdapter("SELECT * FROM Melodii", _con);
        _melodieCommandBuilder= new SqlCommandBuilder(_melodieAdapter);
        _melodieAdapter.InsertCommand = _melodieCommandBuilder.GetInsertCommand();
        _melodieAdapter.UpdateCommand = _melodieCommandBuilder.GetUpdateCommand();
        _melodieAdapter.DeleteCommand = _melodieCommandBuilder.GetDeleteCommand();
    }

    private void LoadData()
    {
        _artistAdapter.Fill(_dataset, "Artisti");

        _melodieAdapter.Fill(_dataset, "Melodii");

        DataRelation relation = new DataRelation(
            "Artisti_Melodii",
            _dataset.Tables["Artisti"].Columns["cod_artist"],
            _dataset.Tables["Melodii"].Columns["cod_artist"]);

        _dataset.Relations.Add(relation);

        _artistBindingSource.DataSource = _dataset;
        _artistBindingSource.DataMember = "Artisti";

        _melodieBindingSource.DataSource = _artistBindingSource;
        _melodieBindingSource.DataMember = "Artisti_Melodii";

        // Legare grid-uri la surse de date
        ArtistiDataGridView.DataSource = _artistBindingSource;
        MelodiiGridView.DataSource = _melodieBindingSource;
    }
    private void Add_Click(object sender, EventArgs e)
    {
        try
        {
            DataRow newRow = _dataset.Tables["Melodii"].NewRow();
            newRow["titlu"] = titlu.Text;
            newRow["an_lansare"] = decimal.Parse(an_lansare.Text);

            string timeStr = durata.Text;
            newRow["durata"] = TimeSpan.Parse(timeStr);

            newRow["cod_artist"] = cod_artist.Text;

            _dataset.Tables["Melodii"].Rows.Add(newRow);
            _melodieAdapter.Update(_dataset, "Melodii");

            _dataset.Tables["Melodii"].Clear();
            _melodieAdapter.Fill(_dataset, "Melodii");

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
                _melodieBindingSource.RemoveCurrent();
                _melodieAdapter.Update(_dataset, "Melodii");

                _dataset.Tables["Melodii"].Clear();
                _melodieAdapter.Fill(_dataset, "Melodii");

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
            if (MelodiiGridView.CurrentRow == null)
            {
                MessageBox.Show("Please select a Melodie to update.");
                return;
            }

            DataRowView currentRow = (DataRowView)_melodieBindingSource.Current;

            currentRow["titlu"] = titlu.Text;
            currentRow["an_lansare"] = decimal.Parse(an_lansare.Text);

            string timeStr = durata.Text;
            currentRow["durata"] = TimeSpan.Parse(timeStr);

            _melodieBindingSource.EndEdit();
            _melodieAdapter.Update(_dataset, "Melodii");

            _dataset.Tables["Melodii"].Clear();
            _melodieAdapter.Fill(_dataset, "Melodii");

            MessageBox.Show("Melodie updated successfully.");
            // ClearFields();
        }
        catch (Exception ex)
        {
            MessageBox.Show("Error updating Melodie: " + ex.Message);
        }
    }

    private void ClearFields()
    {
        titlu.Text = string.Empty;
        an_lansare.Text = string.Empty;
        durata.Text = string.Empty;
        cod_artist.Text = string.Empty;
    }
}