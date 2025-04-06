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
        private void InitializeComponent()
        {
            this.Parent_dataGridView = new System.Windows.Forms.DataGridView();
            this.Child_dataGridView = new System.Windows.Forms.DataGridView();
            this.ConnectBtn = new System.Windows.Forms.Button();
            this.parentLabel = new System.Windows.Forms.Label();
            this.childLabel = new System.Windows.Forms.Label();
            this.panel1 = new System.Windows.Forms.Panel();
            this.AdaugaBtn = new System.Windows.Forms.Button();
            this.StergeBtn = new System.Windows.Forms.Button();
            this.UpdateBtn = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.Parent_dataGridView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.Child_dataGridView)).BeginInit();
            this.SuspendLayout();
            //
            // Parent_dataGridView
            //
            this.Parent_dataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.Parent_dataGridView.Location = new System.Drawing.Point(12, 55);
            this.Parent_dataGridView.Name = "Parent_dataGridView";
            this.Parent_dataGridView.RowHeadersWidth = 51;
            this.Parent_dataGridView.RowTemplate.Height = 24;
            this.Parent_dataGridView.Size = new System.Drawing.Size(608, 316);
            this.Parent_dataGridView.TabIndex = 0;
            //
            // Child_dataGridView
            //
            this.Child_dataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.Child_dataGridView.Location = new System.Drawing.Point(639, 55);
            this.Child_dataGridView.Name = "Child_dataGridView";
            this.Child_dataGridView.RowHeadersWidth = 51;
            this.Child_dataGridView.RowTemplate.Height = 24;
            this.Child_dataGridView.Size = new System.Drawing.Size(611, 316);
            this.Child_dataGridView.TabIndex = 1;
            //
            // ConnectBtn
            //
            this.ConnectBtn.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.ConnectBtn.Location = new System.Drawing.Point(546, 12);
            this.ConnectBtn.Name = "ConnectBtn";
            this.ConnectBtn.Size = new System.Drawing.Size(159, 37);
            this.ConnectBtn.TabIndex = 11;
            this.ConnectBtn.Text = "Connect";
            this.ConnectBtn.UseVisualStyleBackColor = true;
            this.ConnectBtn.Click += new System.EventHandler(this.ConnectBtn_Click);
            //
            // parentLabel
            //
            this.parentLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.parentLabel.Location = new System.Drawing.Point(195, 21);
            this.parentLabel.Name = "parentLabel";
            this.parentLabel.Size = new System.Drawing.Size(138, 28);
            this.parentLabel.TabIndex = 12;
            //
            // childLabel
            //
            this.childLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.childLabel.Location = new System.Drawing.Point(908, 26);
            this.childLabel.Name = "childLabel";
            this.childLabel.Size = new System.Drawing.Size(100, 23);
            this.childLabel.TabIndex = 13;
            //
            // panel1
            //
            this.panel1.Location = new System.Drawing.Point(639, 381);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(610, 284);
            this.panel1.TabIndex = 14;
            //
            // AdaugaBtn
            //
            this.AdaugaBtn.Location = new System.Drawing.Point(237, 407);
            this.AdaugaBtn.Name = "AdaugaBtn";
            this.AdaugaBtn.Size = new System.Drawing.Size(140, 49);
            this.AdaugaBtn.TabIndex = 15;
            this.AdaugaBtn.Text = "Adauga";
            this.AdaugaBtn.UseVisualStyleBackColor = true;
            this.AdaugaBtn.Click += new System.EventHandler(this.AdaugaBtn_Click);
            //
            // StergeBtn
            //
            this.StergeBtn.Location = new System.Drawing.Point(237, 462);
            this.StergeBtn.Name = "StergeBtn";
            this.StergeBtn.Size = new System.Drawing.Size(140, 46);
            this.StergeBtn.TabIndex = 16;
            this.StergeBtn.Text = "Sterge";
            this.StergeBtn.UseVisualStyleBackColor = true;
            this.StergeBtn.Click += new System.EventHandler(this.StergeBtn_Click);
            //
            // UpdateBtn
            //
            this.UpdateBtn.Location = new System.Drawing.Point(237, 514);
            this.UpdateBtn.Name = "UpdateBtn";
            this.UpdateBtn.Size = new System.Drawing.Size(140, 40);
            this.UpdateBtn.TabIndex = 17;
            this.UpdateBtn.Text = "Update";
            this.UpdateBtn.UseVisualStyleBackColor = true;
            this.UpdateBtn.Click += new System.EventHandler(this.UpdateBtn_Click);
            //
            // MagazinTelefoane
            //
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Control;
            this.ClientSize = new System.Drawing.Size(1262, 673);
            this.Controls.Add(this.UpdateBtn);
            this.Controls.Add(this.StergeBtn);
            this.Controls.Add(this.AdaugaBtn);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.childLabel);
            this.Controls.Add(this.parentLabel);
            this.Controls.Add(this.ConnectBtn);
            this.Controls.Add(this.Child_dataGridView);
            this.Controls.Add(this.Parent_dataGridView);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Location = new System.Drawing.Point(15, 15);
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.Name = "MagazinTelefoane";
            ((System.ComponentModel.ISupportInitialize)(this.Parent_dataGridView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.Child_dataGridView)).EndInit();
            this.ResumeLayout(false);
        }

        private System.Windows.Forms.Button AdaugaBtn;
        private System.Windows.Forms.Button StergeBtn;
        private System.Windows.Forms.Button UpdateBtn;

        private System.Windows.Forms.Panel panel1;

        private System.Windows.Forms.Label parentLabel;

        private System.Windows.Forms.Button ConnectBtn;

        #endregion

        private System.Windows.Forms.DataGridView Parent_dataGridView;
        private System.Windows.Forms.DataGridView Child_dataGridView;
        private System.Windows.Forms.Label childLabel;
    }
}

