partial class SignUpForm
{
    private System.Windows.Forms.TextBox usernameField;
    private System.Windows.Forms.TextBox passwordField;
    private System.Windows.Forms.TextBox firstNameField;
    private System.Windows.Forms.TextBox lastNameField;
    private System.Windows.Forms.Button registerButton;
    private System.Windows.Forms.Button backToLoginButton;

    private void InitializeComponent()
    {
        this.usernameField = new System.Windows.Forms.TextBox();
        this.passwordField = new System.Windows.Forms.TextBox();
        this.firstNameField = new System.Windows.Forms.TextBox();
        this.lastNameField = new System.Windows.Forms.TextBox();
        this.registerButton = new System.Windows.Forms.Button();
        this.backToLoginButton = new System.Windows.Forms.Button();
        this.SuspendLayout();
        //
        // usernameField
        //
        this.usernameField.Location = new System.Drawing.Point(100, 50);
        this.usernameField.Name = "usernameField";
        this.usernameField.Size = new System.Drawing.Size(200, 20);
        this.usernameField.TabIndex = 0;
        //
        // passwordField
        //
        this.passwordField.Location = new System.Drawing.Point(100, 100);
        this.passwordField.Name = "passwordField";
        this.passwordField.Size = new System.Drawing.Size(200, 20);
        this.passwordField.TabIndex = 1;
        //
        // firstNameField
        //
        this.firstNameField.Location = new System.Drawing.Point(100, 150);
        this.firstNameField.Name = "firstNameField";
        this.firstNameField.Size = new System.Drawing.Size(200, 20);
        this.firstNameField.TabIndex = 2;
        //
        // lastNameField
        //
        this.lastNameField.Location = new System.Drawing.Point(100, 200);
        this.lastNameField.Name = "lastNameField";
        this.lastNameField.Size = new System.Drawing.Size(200, 20);
        this.lastNameField.TabIndex = 3;
        //
        // registerButton
        //
        this.registerButton.Location = new System.Drawing.Point(100, 250);
        this.registerButton.Name = "registerButton";
        this.registerButton.Size = new System.Drawing.Size(75, 23);
        this.registerButton.TabIndex = 4;
        this.registerButton.Text = "Register";
        this.registerButton.UseVisualStyleBackColor = true;
        this.registerButton.Click += new System.EventHandler(this.registerButton_Click);
        //
        // backToLoginButton
        //
        this.backToLoginButton.Location = new System.Drawing.Point(225, 250);
        this.backToLoginButton.Name = "backToLoginButton";
        this.backToLoginButton.Size = new System.Drawing.Size(75, 23);
        this.backToLoginButton.TabIndex = 5;
        this.backToLoginButton.Text = "Back to Login";
        this.backToLoginButton.UseVisualStyleBackColor = true;
        this.backToLoginButton.Click += new System.EventHandler(this.backToLoginButton_Click);
        //
        // SignUpForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(400, 300);
        this.Controls.Add(this.backToLoginButton);
        this.Controls.Add(this.registerButton);
        this.Controls.Add(this.lastNameField);
        this.Controls.Add(this.firstNameField);
        this.Controls.Add(this.passwordField);
        this.Controls.Add(this.usernameField);
        this.Name = "SignUpForm";
        this.Text = "Sign Up";
        this.ResumeLayout(false);
        this.PerformLayout();
    }
}