using System;
using System.Windows.Forms;
using TriatlonModel;
using TriatlonServicess;

namespace TriatlonArbitru
{
    public partial class Login : Form
    {
        private static readonly log4net.ILog logger = log4net.LogManager.GetLogger(typeof(Login));
        public ITriatlonServices server;

        public Login(ITriatlonServices server)
        {
            InitializeComponent();
            this.server = server;
        }

        private void loginButton_Click(object sender, EventArgs e)
        {
            string username = usernameField.Text.Trim();
            string password = passwordField.Text.Trim();

            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                MessageBox.Show("Username and password cannot be empty", "Login Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            try
            {
                Arbitru arbitru = server.FindArbitruByUsernameAndPassword(username, password);

                if (arbitru != null)
                {
                    logger.Info("User logged in successfully: " + username);

                    // Create controller and connect it to the server
                    ArbitruClientController controller = new ArbitruClientController();

                    // Create main form and pass it the controller
                    MainForm mainForm = new MainForm(server, arbitru, controller);

                    // Register the controller with the server
                    server.Login(arbitru, controller);

                    mainForm.Show();
                    this.Hide();
                }
                else
                {
                    MessageBox.Show("Invalid username or password", "Login Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
            catch (Exception ex)
            {
                logger.Error("Error during login", ex);
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void signUpButton_Click(object sender, EventArgs e)
        {
            try
            {
                SignUp signUpForm = new SignUp();
                signUpForm.server3 = this.server;
                signUpForm.Show();
                this.Hide();
            }
            catch (Exception ex)
            {
                logger.Error("Error opening sign up form", ex);
                MessageBox.Show(ex.Message, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}