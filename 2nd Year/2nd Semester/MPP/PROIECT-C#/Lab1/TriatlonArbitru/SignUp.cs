using System;
using System.Windows.Forms;
using TriatlonServicess;

namespace TriatlonArbitru
{
    public partial class SignUp : Form
    {
        private static readonly log4net.ILog logger = log4net.LogManager.GetLogger(typeof(SignUp));
        public ITriatlonServices server3;

        public SignUp()
        {
            InitializeComponent();
        }

        private void registerButton_Click(object sender, EventArgs e)
        {
            string username = usernameField.Text.Trim();
            string password = passwordField.Text.Trim();
            string firstName = firstNameField.Text.Trim();
            string lastName = lastNameField.Text.Trim();

            // Validate input fields
            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password) ||
                string.IsNullOrEmpty(firstName) || string.IsNullOrEmpty(lastName))
            {
                Util.ShowWarning("Registration Error", "All fields are required!");
                return;
            }

            try
            {
                bool success = server3.Register(username, password, firstName, lastName);
                if (success)
                {
                    Util.ShowWarning("Success", "Registration successful! You can now login.");
                    BackToLogin();
                }
                else
                {
                    Util.ShowWarning("Registration Error", "Username already exists or registration failed!");
                }
            }
            catch (Exception ex)
            {
                logger.Error("Error during registration", ex);
                Util.ShowWarning("Error", ex.Message);
            }
        }

        private void backToLoginButton_Click(object sender, EventArgs e)
        {
            BackToLogin();
        }

        private void BackToLogin()
        {
            try
            {
                Login loginForm = new Login(this.server3);
                loginForm.Show();
                this.Hide();
            }
            catch (Exception ex)
            {
                logger.Error("Could not return to login window", ex);
                Util.ShowWarning("Error", "Could not return to login window: " + ex.Message);
            }
        }
    }
}