using System.ComponentModel;

namespace TriatlonArbitru;

partial class Login
{
    private System.Windows.Forms.TextBox usernameField;
    private System.Windows.Forms.TextBox passwordField;
    private System.Windows.Forms.Button loginButton;
    private System.Windows.Forms.Button signUpButton;
    private System.Windows.Forms.Label label1;

    /// <summary>
    /// Required designer variable.
    /// </summary>
    private IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }

        base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        this.usernameField = new System.Windows.Forms.TextBox();
        this.passwordField = new System.Windows.Forms.TextBox();
        this.loginButton = new System.Windows.Forms.Button();
        this.signUpButton = new System.Windows.Forms.Button();
        this.label1 = new System.Windows.Forms.Label();
        this.SuspendLayout();
        //
        // usernameField
        //
        this.usernameField.Location = new System.Drawing.Point(133, 62);
        this.usernameField.Margin = new System.Windows.Forms.Padding(4);
        this.usernameField.Name = "usernameField";
        this.usernameField.Size = new System.Drawing.Size(265, 22);
        this.usernameField.TabIndex = 0;
        this.usernameField.Text = "username";
        this.usernameField.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
        //
        // passwordField
        //
        this.passwordField.Location = new System.Drawing.Point(133, 123);
        this.passwordField.Margin = new System.Windows.Forms.Padding(4);
        this.passwordField.Name = "passwordField";
        this.passwordField.PasswordChar = '*';
        this.passwordField.Size = new System.Drawing.Size(265, 22);
        this.passwordField.TabIndex = 1;
        this.passwordField.Text = "password";
        this.passwordField.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
        //
        // loginButton
        //
        this.loginButton.Location = new System.Drawing.Point(133, 185);
        this.loginButton.Margin = new System.Windows.Forms.Padding(4);
        this.loginButton.Name = "loginButton";
        this.loginButton.Size = new System.Drawing.Size(100, 28);
        this.loginButton.TabIndex = 2;
        this.loginButton.Text = "Login";
        this.loginButton.UseVisualStyleBackColor = true;
        this.loginButton.Click += new System.EventHandler(this.loginButton_Click);
        //
        // signUpButton
        //
        this.signUpButton.Location = new System.Drawing.Point(300, 185);
        this.signUpButton.Margin = new System.Windows.Forms.Padding(4);
        this.signUpButton.Name = "signUpButton";
        this.signUpButton.Size = new System.Drawing.Size(100, 28);
        this.signUpButton.TabIndex = 3;
        this.signUpButton.Text = "Sign Up";
        this.signUpButton.UseVisualStyleBackColor = true;
        this.signUpButton.Click += new System.EventHandler(this.signUpButton_Click);
        //
        // label1
        //
        this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.label1.Location = new System.Drawing.Point(124, 9);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(286, 23);
        this.label1.TabIndex = 4;
        this.label1.Text = "Triatlon Competition Login";
        //
        // LoginForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.AutoSize = true;
        this.ClientSize = new System.Drawing.Size(533, 308);
        this.Controls.Add(this.label1);
        this.Controls.Add(this.signUpButton);
        this.Controls.Add(this.loginButton);
        this.Controls.Add(this.passwordField);
        this.Controls.Add(this.usernameField);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
        this.Margin = new System.Windows.Forms.Padding(4);
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "LoginForm";
        this.Text = "Login";
        this.ResumeLayout(false);
        this.PerformLayout();
    }

    #endregion
}