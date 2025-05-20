using System;
using System.Windows.Forms;
using Lab1.Service;

public partial class SignUpForm : Form
{
    private readonly AuthentificationService authService;

    public SignUpForm(AuthentificationService authService)
    {
        InitializeComponent();
        this.authService = authService;
    }

    private void registerButton_Click(object sender, EventArgs e)
    {
        string username = usernameField.Text.Trim();
        string password = passwordField.Text.Trim();
        string firstName = firstNameField.Text.Trim();
        string lastName = lastNameField.Text.Trim();

        if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password) || string.IsNullOrEmpty(firstName) || string.IsNullOrEmpty(lastName))
        {
            MessageBox.Show("All fields are required", "Registration Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            return;
        }

        bool registered = authService.Register(username, password, firstName, lastName);
        if (registered)
        {
            MessageBox.Show("Your account has been created. You can now log in.", "Registration Successful", MessageBoxButtons.OK, MessageBoxIcon.Information);
            LoginForm loginForm = new LoginForm(authService, null, null, null); // Pass actual services
            loginForm.Show();
            this.Hide();
        }
        else
        {
            MessageBox.Show("Username may already be taken", "Registration Failed", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void backToLoginButton_Click(object sender, EventArgs e)
    {
        LoginForm loginForm = new LoginForm(authService, null, null, null); // Pass actual services
        loginForm.Show();
        this.Hide();
    }
}