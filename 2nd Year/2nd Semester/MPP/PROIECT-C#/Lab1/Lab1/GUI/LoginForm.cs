using System;
using System.Windows.Forms;
using Lab1.Domain;
using Lab1.Service;

public partial class LoginForm : Form
{
    private readonly AuthentificationService authService;
    private readonly ParticipantService participantService;
    private readonly ProbaService probaService;
    private readonly RezultatService rezultatService;
    private static int successfulLoginCount = 0;

    public LoginForm(AuthentificationService authService, ParticipantService participantService,
        ProbaService probaService, RezultatService rezultatService)
    {
        InitializeComponent();
        this.authService = authService;
        this.participantService = participantService;
        this.probaService = probaService;
        this.rezultatService = rezultatService;
    }

    private void loginButton_Click(object sender, EventArgs e)
    {
        string username = usernameField.Text.Trim();
        string password = passwordField.Text.Trim();

        if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
        {
            MessageBox.Show("Username and password cannot be empty", "Login Error", MessageBoxButtons.OK,
                MessageBoxIcon.Error);
            return;
        }

        Arbitru loggedInArbitru = authService.Login(username, password);
        if (loggedInArbitru != null)
        {
            successfulLoginCount++;
            if (successfulLoginCount == 3)
            {
                MainForm mainForm = new MainForm(authService, participantService, rezultatService, probaService, loggedInArbitru);
                mainForm.Show();
                MessageBox.Show("Maximum number of successful logins reached. Closing login window.", "Login Notification", MessageBoxButtons.OK, MessageBoxIcon.Information);
                this.Hide();
            }
            else
            {
                MainForm mainForm = new MainForm(authService, participantService, rezultatService, probaService, loggedInArbitru);
                mainForm.Show();
            }
        }
        else
        {
            MessageBox.Show("Invalid username or password", "Login Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void signUpButton_Click(object sender, EventArgs e)
    {
        SignUpForm signUpForm = new SignUpForm(authService);
        signUpForm.Show();
    }
}