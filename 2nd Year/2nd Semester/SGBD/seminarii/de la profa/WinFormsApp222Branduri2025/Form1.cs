using Microsoft.Data.SqlClient;
using System.Data;
namespace WinFormsApp222Branduri2025
{
    public partial class Form1 : Form
    {
        DataSet ds = new DataSet();
        SqlDataAdapter parentAdapter = new SqlDataAdapter();
        SqlDataAdapter childAdapter = new SqlDataAdapter();
        string connectionString = @"Server=EMI\SQLEXPRESS;Database=Seminar2222SGBD2025;Integrated Security=true;
        TrustServerCertificate=true;";
        BindingSource bsParent = new BindingSource();
        BindingSource bsChild = new BindingSource();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection con = new SqlConnection(connectionString))
                {
                    con.Open();
                    parentAdapter.SelectCommand = new SqlCommand("SELECT * FROM Branduri;", con);
                    childAdapter.SelectCommand = new SqlCommand("SELECT * FROM Produse;", con);
                    parentAdapter.Fill(ds, "Branduri");
                    childAdapter.Fill(ds, "Produse");
                    DataColumn pkColumn = ds.Tables["Branduri"].Columns["cod_b"];
                    DataColumn fkColumn = ds.Tables["Produse"].Columns["cod_b"];
                    DataRelation relation = new DataRelation("FK_Branduri_Produse", pkColumn, fkColumn, true);
                    ds.Relations.Add(relation);
                    bsParent.DataSource = ds.Tables["Branduri"];
                    dataGridViewParent.DataSource = bsParent;
                    textBox1.DataBindings.Add("Text", bsParent, "nume", true);
                    bsChild.DataSource = bsParent;
                    bsChild.DataMember = "FK_Branduri_Produse";
                    dataGridViewChild.DataSource = bsChild;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                using(SqlConnection con = new SqlConnection(connectionString))
                {
                    /* daca lucram doar cu SqlDataAdapter, nu este necesara deschiderea conexiunii, deoarece
                     * SqlDataAdapter deschide si inchide automat conexiunea la apelul metodei Fill()
                     */
                    if (ds.Tables.Contains("Produse"))
                    {
                        ds.Tables["Produse"].Clear();
                    }
                    if (ds.Tables.Contains("Branduri"))
                    {
                        ds.Tables["Branduri"].Clear();
                    }
                    parentAdapter.SelectCommand.Connection = con;
                    childAdapter.SelectCommand.Connection = con;
                    parentAdapter.Fill(ds, "Branduri");
                    childAdapter.Fill(ds, "Produse");
                }
            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
