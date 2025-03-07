using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace Laborator1 {
    public partial class Form1 : Form {
        SqlConnection cs = new SqlConnection("Data Source = DESKTOP-LI67DLG\\SQLEXPRESS;Initial Catalog=S1;Integrated Security=true;TrustServerCertificate=true");
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();
        public Form1() {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e) {
            try {
                da.InsertCommand = new SqlCommand("INSERT INTO Produse VALUES(@Nume, @Pret, @Producator)", cs);
                da.InsertCommand.Parameters.Add("@Nume", SqlDbType.VarChar).Value = NumeTextBox.Text;
                da.InsertCommand.Parameters.Add("@Pret", SqlDbType.Float).Value = Int32.Parse(PretTextBox.Text);
                da.InsertCommand.Parameters.Add("@Producator", SqlDbType.VarChar).Value = ProducatorTextBox.Text;
                cs.Open();
                da.InsertCommand.ExecuteNonQuery();
                MessageBox.Show("Produs adaugat cu succes!");
                cs.Close();

                ds.Clear();
                da.Fill(ds);
                dataGridView1.DataSource = ds.Tables[0];
            }
            catch (Exception ex) {
                MessageBox.Show("Error: " + ex.Message);
                cs.Close();
            }
            finally {
                cs.Close();
            }
        }

        private void dataGridView1_CellContentClick(object sender, EventArgs e) {
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
    }
}
