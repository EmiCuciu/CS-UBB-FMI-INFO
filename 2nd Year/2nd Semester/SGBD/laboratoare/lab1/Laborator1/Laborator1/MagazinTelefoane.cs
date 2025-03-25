using System;
using System.Data;
using System.Data.SqlClient;
using System.Windows.Forms;

namespace Laborator1 {
    public partial class MagazinTelefoane : Form {
        private SqlConnection cs = new SqlConnection("Data Source = EMI\\SQLEXPRESS;Initial Catalog=MagazinTelefoane;Integrated Security=true;TrustServerCertificate=true");
        private SqlDataAdapter clientiAdapter;
        private SqlDataAdapter comenziAdapter;
        private DataSet dataset;
        private BindingSource clientiBindingSource;
        private BindingSource comenziBindingSource;

        public MagazinTelefoane() {
            InitializeComponent();
            InitializeDataComponents();
            LoadData();
        }

        private void InitializeDataComponents() {
            dataset = new DataSet();
            clientiBindingSource = new BindingSource();
            comenziBindingSource = new BindingSource();

            // Configurare adapter pentru Clienti
            clientiAdapter = new SqlDataAdapter("SELECT * FROM Clienti", cs);
            SqlCommandBuilder clientiCommandBuilder = new SqlCommandBuilder(clientiAdapter);
            clientiAdapter.InsertCommand = clientiCommandBuilder.GetInsertCommand();
            clientiAdapter.UpdateCommand = clientiCommandBuilder.GetUpdateCommand();
            clientiAdapter.DeleteCommand = clientiCommandBuilder.GetDeleteCommand();

            // Configurare adapter pentru Comenzi_Clienti
            comenziAdapter = new SqlDataAdapter("SELECT * FROM Comenzi_Clienti", cs);
            SqlCommandBuilder comenziCommandBuilder = new SqlCommandBuilder(comenziAdapter);
            comenziAdapter.InsertCommand = comenziCommandBuilder.GetInsertCommand();
            comenziAdapter.UpdateCommand = comenziCommandBuilder.GetUpdateCommand();
            comenziAdapter.DeleteCommand = comenziCommandBuilder.GetDeleteCommand();
        }

        private void LoadData() {
            try {
                // Încărcare date Clienti
                clientiAdapter.Fill(dataset, "Clienti");

                // Încărcare date Comenzi_Clienti
                comenziAdapter.Fill(dataset, "Comenzi_Clienti");

                // Definire relație între tabele
                DataRelation relation = new DataRelation(
                    "Comenzi_Clienti",
                    dataset.Tables["Clienti"].Columns["ID_Client"],
                    dataset.Tables["Comenzi_Clienti"].Columns["ID_Client"]);

                dataset.Relations.Add(relation);

                // Configurare binding sources
                clientiBindingSource.DataSource = dataset;
                clientiBindingSource.DataMember = "Clienti";

                comenziBindingSource.DataSource = clientiBindingSource;
                comenziBindingSource.DataMember = "Comenzi_Clienti";

                // Legare grid-uri la surse de date
                Clienti_dataGridView.DataSource = clientiBindingSource;
                ComenziClienti_dataGridView.DataSource = comenziBindingSource;

                ConfigureEditControls();
            }
            catch (Exception ex) {
                MessageBox.Show("Eroare la încărcarea datelor: " + ex.Message);
            }
        }

        private void ConfigureEditControls() {
            dateTimePicker1.DataBindings.Add("Value", comenziBindingSource, "Data_Comenzi", true);
        }

        private void Add_Button_Click(object sender, EventArgs e) {
            try {
                int idClient = (int)((DataRowView)clientiBindingSource.Current)["ID_Client"];

                DataRow newRow = dataset.Tables["Comenzi_Clienti"].NewRow();
                newRow["Data_Comenzi"] = DateTime.Now;
                newRow["ID_Client"] = idClient;

                dataset.Tables["Comenzi_Clienti"].Rows.Add(newRow);
                comenziAdapter.Update(dataset, "Comenzi_Clienti");

                dataset.Tables["Comenzi_Clienti"].Clear();
                comenziAdapter.Fill(dataset, "Comenzi_Clienti");

                MessageBox.Show("Date adăugate cu succes");

            }
            catch (Exception ex) {
                MessageBox.Show("Eroare la adăugarea comenzii" + ex.Message);
            }
        }

        private void Update_Button_Click(object sender, EventArgs e) {
            try {
                comenziBindingSource.EndEdit();
                comenziAdapter.Update(dataset, "Comenzi_Clienti");
                MessageBox.Show("Date actualizate cu succes");
            }
            catch (Exception ex) {
                MessageBox.Show("Eroare la actualizarea comenzii" + ex.Message);
            }
        }

        private void Delete_Button_Click(object sender, EventArgs e) {
            if (MessageBox.Show("Sunteți sigur că doriți să ștergeți comanda?", "Ștergere comandă", MessageBoxButtons.YesNo) == DialogResult.Yes) {
                try {
                    comenziBindingSource.RemoveCurrent();
                    comenziAdapter.Update(dataset, "Comenzi_Clienti");
                    MessageBox.Show("Comandă ștearsă cu succes");
                }
                catch (Exception ex) {
                    MessageBox.Show("Eroare la ștergerea comenzii" + ex.Message);
                }
            }
        }
    }
}
