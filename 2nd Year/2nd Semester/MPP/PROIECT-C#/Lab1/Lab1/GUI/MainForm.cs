using System;
using System.Linq;
using System.Windows.Forms;
using Lab1.Domain;
using Lab1.Service;

public partial class MainForm : Form
{
    private readonly AuthentificationService authService;
    private readonly ParticipantService participantService;
    private readonly RezultatService rezultatService;
    private readonly ProbaService probaService;
    private readonly Arbitru currentArbitru;
    private TipProba currentProba;

    public MainForm(AuthentificationService authService, ParticipantService participantService,
        RezultatService rezultatService, ProbaService probaService, Arbitru currentArbitru)
    {
        InitializeComponent();
        this.authService = authService;
        this.participantService = participantService;
        this.rezultatService = rezultatService;
        this.probaService = probaService;
        this.currentArbitru = currentArbitru;
        this.Load += new EventHandler(this.MainForm_Load);

        rezultatService.RezultatAdded += OnRezultatAdded;
    }

    private void MainForm_Load(object sender, EventArgs e)
    {
        if (currentArbitru != null)
        {
            var proba = probaService.GetProbaForArbitru(currentArbitru);
            if (proba.HasValue)
            {
                currentProba = proba.Value;
                arbitruNameLabel.Text =
                    $"Arbitru: {currentArbitru.FirstName} {currentArbitru.LastName}          Proba: {currentProba}";

                // Configure DataGridView columns
                participantsDataGridView.Columns[0].DataPropertyName = "FullName";
                participantsDataGridView.Columns[1].DataPropertyName = "TotalPoints";
                participantsDataGridView.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.None;
                participantsDataGridView.Columns[0].Width = 384;


                resultDataGridView.Columns[0].DataPropertyName = "FullName";
                resultDataGridView.Columns[1].DataPropertyName = "Points";
                resultDataGridView.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.None;
                resultDataGridView.Columns[0].Width = 384;

                // Load data
                LoadParticipants();
                LoadResultsForCurrentProba();

                // Configure ComboBox
                LoadComboBox();
            }
            else
            {
                MessageBox.Show("No proba found for the current arbitru", "Error", MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }
    }

    private void LoadParticipants()
    {
        var participants = participantService.GetAllParticipants();

        participantsDataGridView.DataSource = null;
        participantsDataGridView.DataSource = participants.Select(p => new
        {
            FullName = $"{p.LastName} {p.FirstName}",
            TotalPoints = participantService.CalculateTotalScore(p)
        }).ToList();

        // Restore column widths after setting the data source
        participantsDataGridView.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.None;
        participantsDataGridView.Columns[0].Width = 384;
        participantsDataGridView.Columns[1].Width = 384;
    }

    private void LoadResultsForCurrentProba()
    {
        var result = rezultatService.GetRezultateForProba(currentProba);

        // aduna punctajul participantilor si afiseaza-l o singura data
        foreach (var r in result)
        {
            var participant = participantService.GetParticipantById(r.Participant.Id);
            if (participant != null)
            {
                r.Participant = participant;
            }
        }

        resultDataGridView.DataSource = null;
        resultDataGridView.DataSource = result.Select(r => new
        {
            FullName = $"{r.Participant.LastName} {r.Participant.FirstName}",
            Points = r.Points
        }).ToList();

        // Restore column widths after setting the data source
        resultDataGridView.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.None;
        resultDataGridView.Columns[0].Width = 384;
        resultDataGridView.Columns[1].Width = 384;
    }

    private void LoadComboBox()
    {
        var participants = participantService.GetAllParticipants();

        comboBox1.DataSource = participants.Select(p => new
        {
            Participant = p,
            DisplayName = $"{p.FirstName} {p.LastName}"
        }).ToList();

        comboBox1.DisplayMember = "DisplayName";
        comboBox1.ValueMember = "Participant";
    }

    private void button1_Click(object sender, EventArgs e)
    {
        if (comboBox1.SelectedItem != null && int.TryParse(textBox1.Text, out int points))
        {
            var selectedItem = comboBox1.SelectedItem;
            var selectedParticipant =
                selectedItem.GetType().GetProperty("Participant").GetValue(selectedItem, null) as Participant;
            if (points < 0)
            {
                MessageBox.Show("Points must be a positive number", "Invalid points", MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
                return;
            }

            rezultatService.AddRezultat(selectedParticipant, currentArbitru, currentProba, points);
            textBox1.Clear();
            comboBox1.SelectedItem = null;
            LoadResultsForCurrentProba();
            LoadParticipants();
        }
        else
        {
            MessageBox.Show("Please select a participant and enter valid points.", "Error", MessageBoxButtons.OK,
                MessageBoxIcon.Error);
        }
    }

    private void logoutButton_Click(object sender, EventArgs e)
    {
        authService.Logout();
        this.Close();
    }

    // Event handler method
    private void OnRezultatAdded(object sender, EventArgs e)
    {
        // Use Invoke to update UI from a different thread if needed
        if (InvokeRequired)
        {
            Invoke(new Action(() => {
                LoadParticipants();
                LoadResultsForCurrentProba();
            }));
        }
        else
        {
            LoadParticipants();
            LoadResultsForCurrentProba();
        }
    }

    // Unsubscribe from the event when the form closes
    protected override void OnFormClosed(FormClosedEventArgs e)
    {
        rezultatService.RezultatAdded -= OnRezultatAdded;
        base.OnFormClosed(e);
    }
}