// MainForm.Designer.cs

using System.Windows.Forms;

partial class MainForm
{
    private System.Windows.Forms.Label arbitruNameLabel;
    private System.Windows.Forms.DataGridView participantsDataGridView;
    private System.Windows.Forms.Button logoutButton;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.ComboBox comboBox1;
    private System.Windows.Forms.TextBox textBox1;
    private System.Windows.Forms.Button button1;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.DataGridView resultDataGridView;
    private System.Windows.Forms.DataGridViewTextBoxColumn NameParticipants;
    private System.Windows.Forms.DataGridViewTextBoxColumn TotalPoints;
    private System.Windows.Forms.DataGridViewTextBoxColumn ResultName;
    private System.Windows.Forms.DataGridViewTextBoxColumn ResultTotalPoints;

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        this.arbitruNameLabel = new System.Windows.Forms.Label();
        this.participantsDataGridView = new System.Windows.Forms.DataGridView();
        this.logoutButton = new System.Windows.Forms.Button();
        this.label1 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.comboBox1 = new System.Windows.Forms.ComboBox();
        this.textBox1 = new System.Windows.Forms.TextBox();
        this.button1 = new System.Windows.Forms.Button();
        this.label3 = new System.Windows.Forms.Label();
        this.resultDataGridView = new System.Windows.Forms.DataGridView();
        this.ResultName = new System.Windows.Forms.DataGridViewTextBoxColumn();
        this.ResultTotalPoints = new System.Windows.Forms.DataGridViewTextBoxColumn();
        this.NameParticipants = new System.Windows.Forms.DataGridViewTextBoxColumn();
        this.TotalPoints = new System.Windows.Forms.DataGridViewTextBoxColumn();
        ((System.ComponentModel.ISupportInitialize)(this.participantsDataGridView)).BeginInit();
        ((System.ComponentModel.ISupportInitialize)(this.resultDataGridView)).BeginInit();
        this.SuspendLayout();
        // 
        // arbitruNameLabel
        // 
        this.arbitruNameLabel.AutoSize = true;
        this.arbitruNameLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.arbitruNameLabel.Location = new System.Drawing.Point(16, 11);
        this.arbitruNameLabel.Margin = new System.Windows.Forms.Padding(4, 0, 4, 0);
        this.arbitruNameLabel.Name = "arbitruNameLabel";
        this.arbitruNameLabel.Size = new System.Drawing.Size(237, 25);
        this.arbitruNameLabel.TabIndex = 0;
        this.arbitruNameLabel.Text = "Arbitru: Nume Prenume";
        // 
        // participantsDataGridView
        // 
        this.participantsDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.DisableResizing;
        this.participantsDataGridView.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] { this.NameParticipants, this.TotalPoints });
        this.participantsDataGridView.Location = new System.Drawing.Point(19, 90);
        this.participantsDataGridView.Margin = new System.Windows.Forms.Padding(4);
        this.participantsDataGridView.Name = "participantsDataGridView";
        this.participantsDataGridView.Size = new System.Drawing.Size(768, 189);
        this.participantsDataGridView.TabIndex = 1;
        // 
        // logoutButton
        // 
        this.logoutButton.Location = new System.Drawing.Point(666, 11);
        this.logoutButton.Margin = new System.Windows.Forms.Padding(4);
        this.logoutButton.Name = "logoutButton";
        this.logoutButton.Size = new System.Drawing.Size(121, 24);
        this.logoutButton.TabIndex = 5;
        this.logoutButton.Text = "Logout";
        this.logoutButton.UseVisualStyleBackColor = true;
        this.logoutButton.Click += new System.EventHandler(this.logoutButton_Click);
        // 
        // label1
        // 
        this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.label1.Location = new System.Drawing.Point(16, 63);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(100, 23);
        this.label1.TabIndex = 6;
        this.label1.Text = "Participants";
        // 
        // label2
        // 
        this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.label2.Location = new System.Drawing.Point(25, 313);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(100, 23);
        this.label2.TabIndex = 7;
        this.label2.Text = "AddResult :\r";
        // 
        // comboBox1
        // 
        this.comboBox1.FormattingEnabled = true;
        this.comboBox1.Location = new System.Drawing.Point(131, 312);
        this.comboBox1.Name = "comboBox1";
        this.comboBox1.Size = new System.Drawing.Size(171, 24);
        this.comboBox1.TabIndex = 8;
        // 
        // textBox1
        // 
        this.textBox1.Location = new System.Drawing.Point(308, 313);
        this.textBox1.Name = "textBox1";
        this.textBox1.Size = new System.Drawing.Size(141, 22);
        this.textBox1.TabIndex = 9;
        this.textBox1.Text = "Points";
        // 
        // button1
        // 
        this.button1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.button1.Location = new System.Drawing.Point(455, 308);
        this.button1.Name = "button1";
        this.button1.Size = new System.Drawing.Size(115, 31);
        this.button1.TabIndex = 10;
        this.button1.Text = "Add Result";
        this.button1.UseVisualStyleBackColor = true;
        this.button1.Click += new System.EventHandler(this.button1_Click);
        // 
        // label3
        // 
        this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.label3.Location = new System.Drawing.Point(19, 365);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(220, 23);
        this.label3.TabIndex = 11;
        this.label3.Text = "Results for Your Event";
        // 
        // resultDataGridView
        // 
        this.resultDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.DisableResizing;
        this.resultDataGridView.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] { this.ResultName, this.ResultTotalPoints });
        this.resultDataGridView.Location = new System.Drawing.Point(16, 391);
        this.resultDataGridView.Name = "resultDataGridView";
        this.resultDataGridView.RowTemplate.Height = 24;
        this.resultDataGridView.Size = new System.Drawing.Size(768, 237);
        this.resultDataGridView.TabIndex = 12;
        // 
        // ResultName
        // 
        this.ResultName.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
        this.ResultName.HeaderText = "Name";
        this.ResultName.Name = "ResultName";
        this.ResultName.Resizable = System.Windows.Forms.DataGridViewTriState.False;
        this.ResultName.Width = 384;
        // 
        // ResultTotalPoints
        // 
        this.ResultTotalPoints.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
        this.ResultTotalPoints.HeaderText = "TotalPoints";
        this.ResultTotalPoints.Name = "ResultTotalPoints";
        this.ResultTotalPoints.Resizable = System.Windows.Forms.DataGridViewTriState.False;
        this.ResultTotalPoints.Width = 384;
        // 
        // NameParticipants
        // 
        this.NameParticipants.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
        this.NameParticipants.HeaderText = "Name";
        this.NameParticipants.Name = "NameParticipants";
        this.NameParticipants.Resizable = System.Windows.Forms.DataGridViewTriState.False;
        this.NameParticipants.Width = 384;
        // 
        // TotalPoints
        // 
        this.TotalPoints.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
        this.TotalPoints.HeaderText = "Total Points";
        this.TotalPoints.Name = "TotalPoints";
        this.TotalPoints.Resizable = System.Windows.Forms.DataGridViewTriState.False;
        this.TotalPoints.Width = 384;
        // 
        // MainForm
        // 
        this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.AutoScroll = true;
        this.AutoSize = true;
        this.BackColor = System.Drawing.SystemColors.HighlightText;
        this.ClientSize = new System.Drawing.Size(800, 704);
        this.Controls.Add(this.resultDataGridView);
        this.Controls.Add(this.label3);
        this.Controls.Add(this.button1);
        this.Controls.Add(this.textBox1);
        this.Controls.Add(this.comboBox1);
        this.Controls.Add(this.label2);
        this.Controls.Add(this.label1);
        this.Controls.Add(this.logoutButton);
        this.Controls.Add(this.participantsDataGridView);
        this.Controls.Add(this.arbitruNameLabel);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
        this.Location = new System.Drawing.Point(15, 15);
        this.Margin = new System.Windows.Forms.Padding(4);
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "MainForm";
        this.Text = "Arbitru Table";
        ((System.ComponentModel.ISupportInitialize)(this.participantsDataGridView)).EndInit();
        ((System.ComponentModel.ISupportInitialize)(this.resultDataGridView)).EndInit();
        this.ResumeLayout(false);
        this.PerformLayout();
    }
}