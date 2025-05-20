using System;
using System.Collections.Generic;
using System.Windows.Forms;
using TriatlonModel;
using TriatlonServicess;

namespace TriatlonArbitru
{
    public partial class MainForm : Form, ITriatlonObserver
    {
        private readonly ITriatlonServices server;
        private readonly Arbitru loggedInArbitru;
        private readonly ArbitruClientController controller;
        private List<Participant> participanti;
        private static readonly log4net.ILog logger = log4net.LogManager.GetLogger(typeof(MainForm));

        public MainForm(ITriatlonServices server, Arbitru arbitru, ArbitruClientController controller)
        {
            InitializeComponent();
            this.server = server;
            this.loggedInArbitru = arbitru;
            this.controller = controller;
            this.arbitruNameLabel.Text = $"Welcome, {arbitru.FirstName} {arbitru.LastName}!";

            try
            {
                // Subscribe to controller events
                this.controller.OnRezultatAdded += OnRezultatAddedHandler;
                this.controller.OnArbitruLoggedIn += OnArbitruLoggedInHandler;
                this.controller.OnArbitruLoggedOut += OnArbitruLoggedOutHandler;

                // Load initial data
                LoadParticipants();
                LoadResults();
                PopulateParticipantsComboBox();
            }
            catch (Exception ex)
            {
                logger.Error("Error initializing form", ex);
                MessageBox.Show($"Error initializing form: {ex.Message}", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // Event handlers for controller events
        private void OnRezultatAddedHandler(Rezultat rezultat)
        {
            if (InvokeRequired)
            {
                BeginInvoke(new Action(() => OnRezultatAddedHandler(rezultat)));
                return;
            }

            logger.Info($"Received notification about new result for participant: {rezultat.Participant.FirstName} {rezultat.Participant.LastName}");

            // Refresh data to reflect new result
            LoadParticipants();
            LoadResults();
        }

        private void OnArbitruLoggedInHandler(Arbitru arbitru)
        {
            logger.Info($"Arbitru logged in: {arbitru.FirstName} {arbitru.LastName}");
        }

        private void OnArbitruLoggedOutHandler(Arbitru arbitru)
        {
            logger.Info($"Arbitru logged out: {arbitru.FirstName} {arbitru.LastName}");
        }

        private void LoadParticipants()
        {
            try
            {
                participanti = server.GetAllParticipants();
                UpdateParticipantsDataGridView();
            }
            catch (Exception ex)
            {
                logger.Error("Error loading participants", ex);
                MessageBox.Show($"Error loading participants: {ex.Message}", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void UpdateParticipantsDataGridView()
        {
            participantsDataGridView.Rows.Clear();
            foreach (var participant in participanti)
            {
                participantsDataGridView.Rows.Add(
                    $"{participant.LastName} {participant.FirstName}",
                    server.CalculateTotalScore(participant));
            }
        }

        private void LoadResults()
        {
            try
            {
                TipProba tipProba = server.GetProbaForArbitru(loggedInArbitru);
                List<Rezultat> rezultate = server.GetResultateForProba(tipProba);

                UpdateResultsDataGridView(rezultate);
            }
            catch (Exception ex)
            {
                logger.Error("Error loading results", ex);
                MessageBox.Show($"Error loading results: {ex.Message}", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void UpdateResultsDataGridView(List<Rezultat> rezultate)
        {
            resultDataGridView.Rows.Clear();
            foreach (var rezultat in rezultate)
            {
                resultDataGridView.Rows.Add(
                    $"{rezultat.Participant.LastName} {rezultat.Participant.FirstName}",
                    rezultat.Points);
            }
        }

        private void PopulateParticipantsComboBox()
        {
            comboBox1.Items.Clear();
            foreach (var participant in participanti)
            {
                comboBox1.Items.Add($"{participant.LastName} {participant.FirstName}");
            }

            if (comboBox1.Items.Count > 0)
                comboBox1.SelectedIndex = 0;
        }

        private void addResultButton_Click(object sender, EventArgs e)
        {
            try
            {
                if (comboBox1.SelectedIndex == -1)
                {
                    MessageBox.Show("Please select a participant", "Warning",
                        MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                if (!int.TryParse(textBox1.Text, out int points))
                {
                    MessageBox.Show("Please enter a valid number for points", "Warning",
                        MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }

                Participant selectedParticipant = participanti[comboBox1.SelectedIndex];

                TipProba tipProba = server.GetProbaForArbitru(loggedInArbitru);

                server.AddRezultat(selectedParticipant, loggedInArbitru, tipProba, points);

                textBox1.Text = "";
                MessageBox.Show("Result added successfully!", "Success",
                    MessageBoxButtons.OK, MessageBoxIcon.Information);

            }
            catch (Exception ex)
            {
                logger.Error("Error adding result", ex);
                MessageBox.Show($"Error adding result: {ex.Message}", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            try
            {
                this.controller.OnRezultatAdded -= OnRezultatAddedHandler;
                this.controller.OnArbitruLoggedIn -= OnArbitruLoggedInHandler;
                this.controller.OnArbitruLoggedOut -= OnArbitruLoggedOutHandler;

                server.Logout(loggedInArbitru, controller);
                this.Close();

                Login loginForm = new Login(server);
                loginForm.Show();
            }
            catch (Exception ex)
            {
                logger.Error("Error logging out", ex);
                MessageBox.Show($"Error logging out: {ex.Message}", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // ITriatlonObserver interface implementation
        public void ArbitruLoggedIn(Arbitru arbitru)
        {
            // We're using the controller events instead
        }

        public void ArbitruLoggedOut(Arbitru arbitru)
        {
            // We're using the controller events instead
        }

        public void RezultatAdded(Rezultat rezultat)
        {
            // We're using the controller events instead
        }

        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            base.OnFormClosing(e);
            try
            {
                // Unsubscribe from events
                this.controller.OnRezultatAdded -= OnRezultatAddedHandler;
                this.controller.OnArbitruLoggedIn -= OnArbitruLoggedInHandler;
                this.controller.OnArbitruLoggedOut -= OnArbitruLoggedOutHandler;

                server.Logout(loggedInArbitru, controller);
            }
            catch (Exception ex)
            {
                logger.Error("Error during form closing", ex);
                // Ignore exceptions during form closing
            }
        }
    }
}