using System;
using System.Data;
using System.Data.SqlClient;
using System.Windows.Forms;

namespace Laborator1 {
    public partial class Form1 : Form {
        SqlConnection cs = new SqlConnection("Data Source = DESKTOP-LI67DLG\\SQLEXPRESS;Initial Catalog=S1;Integrated Security=true;TrustServerCertificate=true");
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();

        public Form1() {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e) {
            try {
                da.SelectCommand = new SqlCommand("SELECT * FROM Produse", cs);
                ds.Clear();
                da.Fill(ds);
                dataGridView1.DataSource = ds.Tables[0];
            }
            catch (Exception ex) {
                MessageBox.Show("Error: " + ex.Message);
            }
        }

        private void button1_Click(object sender, EventArgs e) {
            //MessageBox.Show("Button clicked");
            Console.WriteLine("Button clicked");
        }
    }
}