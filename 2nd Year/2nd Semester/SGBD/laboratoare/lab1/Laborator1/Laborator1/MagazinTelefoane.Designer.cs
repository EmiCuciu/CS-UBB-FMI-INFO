namespace Laborator1 {
    partial class MagazinTelefoane {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.Clienti_dataGridView = new System.Windows.Forms.DataGridView();
            this.ComenziClienti_dataGridView = new System.Windows.Forms.DataGridView();
            this.dateTimePicker1 = new System.Windows.Forms.DateTimePicker();
            this.Add_Button = new System.Windows.Forms.Button();
            this.Delete_Button = new System.Windows.Forms.Button();
            this.Update_Button = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.Clienti_dataGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.ComenziClienti_dataGridView)).BeginInit();
            this.SuspendLayout();
            // 
            // Clienti_dataGridView
            // 
            this.Clienti_dataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.Clienti_dataGridView.Location = new System.Drawing.Point(12, 28);
            this.Clienti_dataGridView.Name = "Clienti_dataGridView";
            this.Clienti_dataGridView.RowHeadersWidth = 51;
            this.Clienti_dataGridView.RowTemplate.Height = 24;
            this.Clienti_dataGridView.Size = new System.Drawing.Size(614, 452);
            this.Clienti_dataGridView.TabIndex = 0;
            // 
            // ComenziClienti_dataGridView
            // 
            this.ComenziClienti_dataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.ComenziClienti_dataGridView.Location = new System.Drawing.Point(632, 28);
            this.ComenziClienti_dataGridView.Name = "ComenziClienti_dataGridView";
            this.ComenziClienti_dataGridView.RowHeadersWidth = 51;
            this.ComenziClienti_dataGridView.RowTemplate.Height = 24;
            this.ComenziClienti_dataGridView.Size = new System.Drawing.Size(618, 452);
            this.ComenziClienti_dataGridView.TabIndex = 1;
            // 
            // dateTimePicker1
            // 
            this.dateTimePicker1.Location = new System.Drawing.Point(821, 525);
            this.dateTimePicker1.Name = "dateTimePicker1";
            this.dateTimePicker1.Size = new System.Drawing.Size(237, 22);
            this.dateTimePicker1.TabIndex = 2;
            // 
            // Add_Button
            // 
            this.Add_Button.Location = new System.Drawing.Point(169, 496);
            this.Add_Button.Name = "Add_Button";
            this.Add_Button.Size = new System.Drawing.Size(178, 84);
            this.Add_Button.TabIndex = 3;
            this.Add_Button.Text = "Adaugare";
            this.Add_Button.UseVisualStyleBackColor = true;
            this.Add_Button.Click += new System.EventHandler(this.Add_Button_Click);
            // 
            // Delete_Button
            // 
            this.Delete_Button.Location = new System.Drawing.Point(415, 496);
            this.Delete_Button.Name = "Delete_Button";
            this.Delete_Button.Size = new System.Drawing.Size(173, 84);
            this.Delete_Button.TabIndex = 4;
            this.Delete_Button.Text = "Sterge";
            this.Delete_Button.UseVisualStyleBackColor = true;
            this.Delete_Button.Click += new System.EventHandler(this.Delete_Button_Click);
            // 
            // Update_Button
            // 
            this.Update_Button.Location = new System.Drawing.Point(637, 496);
            this.Update_Button.Name = "Update_Button";
            this.Update_Button.Size = new System.Drawing.Size(178, 84);
            this.Update_Button.TabIndex = 5;
            this.Update_Button.Text = "Actualizeaza";
            this.Update_Button.UseVisualStyleBackColor = true;
            this.Update_Button.Click += new System.EventHandler(this.Update_Button_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(290, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(57, 16);
            this.label1.TabIndex = 8;
            this.label1.Text = "CLIENTI";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(938, 9);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(120, 16);
            this.label2.TabIndex = 9;
            this.label2.Text = "COMENZI CLIENTI";
            // 
            // MagazinTelefoane
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1262, 673);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.Update_Button);
            this.Controls.Add(this.Delete_Button);
            this.Controls.Add(this.Add_Button);
            this.Controls.Add(this.dateTimePicker1);
            this.Controls.Add(this.ComenziClienti_dataGridView);
            this.Controls.Add(this.Clienti_dataGridView);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.Name = "MagazinTelefoane";
            this.Text = "MagazinTelefoane";
            ((System.ComponentModel.ISupportInitialize)(this.Clienti_dataGridView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.ComenziClienti_dataGridView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView Clienti_dataGridView;
        private System.Windows.Forms.DataGridView ComenziClienti_dataGridView;
        private System.Windows.Forms.DateTimePicker dateTimePicker1;
        private System.Windows.Forms.Button Add_Button;
        private System.Windows.Forms.Button Delete_Button;
        private System.Windows.Forms.Button Update_Button;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
    }
}

