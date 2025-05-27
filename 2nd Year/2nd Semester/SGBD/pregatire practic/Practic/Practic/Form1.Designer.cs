namespace Practic;

partial class Form1
{
    /// <summary>
    ///  Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    ///  Clean up any resources being used.
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
        GrupaDataGridView = new System.Windows.Forms.DataGridView();
        CopiiDataGridView = new System.Windows.Forms.DataGridView();
        Adauga = new System.Windows.Forms.Button();
        Sterge = new System.Windows.Forms.Button();
        Modifica = new System.Windows.Forms.Button();
        nume = new System.Windows.Forms.TextBox();
        varsta = new System.Windows.Forms.TextBox();
        cod_grupa = new System.Windows.Forms.TextBox();
        ((System.ComponentModel.ISupportInitialize)GrupaDataGridView).BeginInit();
        ((System.ComponentModel.ISupportInitialize)CopiiDataGridView).BeginInit();
        SuspendLayout();
        // 
        // GrupaDataGridView
        // 
        GrupaDataGridView.ColumnHeadersHeight = 29;
        GrupaDataGridView.Location = new System.Drawing.Point(12, 12);
        GrupaDataGridView.Name = "GrupaDataGridView";
        GrupaDataGridView.RowHeadersWidth = 51;
        GrupaDataGridView.Size = new System.Drawing.Size(508, 267);
        GrupaDataGridView.TabIndex = 0;
        //
        // CopiiDataGridView
        //
        CopiiDataGridView.ColumnHeadersHeight = 29;
        CopiiDataGridView.Location = new System.Drawing.Point(526, 12);
        CopiiDataGridView.Name = "CopiiDataGridView";
        CopiiDataGridView.RowHeadersWidth = 51;
        CopiiDataGridView.Size = new System.Drawing.Size(492, 267);
        CopiiDataGridView.TabIndex = 1;
        //
        // Adauga
        //
        Adauga.Location = new System.Drawing.Point(691, 360);
        Adauga.Name = "Adauga";
        Adauga.Size = new System.Drawing.Size(75, 41);
        Adauga.TabIndex = 5;
        Adauga.Text = "Adauga";
        Adauga.UseVisualStyleBackColor = true;
        Adauga.Click += Add_Click;
        //
        // Sterge
        //
        Sterge.Location = new System.Drawing.Point(691, 407);
        Sterge.Name = "Sterge";
        Sterge.Size = new System.Drawing.Size(75, 41);
        Sterge.TabIndex = 6;
        Sterge.Text = "Sterge";
        Sterge.UseVisualStyleBackColor = true;
        Sterge.Click += Sterge_Click;
        //
        // Modifica
        //
        Modifica.Location = new System.Drawing.Point(691, 454);
        Modifica.Name = "Modifica";
        Modifica.Size = new System.Drawing.Size(93, 40);
        Modifica.TabIndex = 7;
        Modifica.Text = "Modifica";
        Modifica.UseVisualStyleBackColor = true;
        Modifica.Click += Modifica_Click;
        // 
        // nume
        // 
        nume.Location = new System.Drawing.Point(158, 395);
        nume.Name = "nume";
        nume.PlaceholderText = "nume";
        nume.Size = new System.Drawing.Size(100, 27);
        nume.TabIndex = 8;
        // 
        // varsta
        // 
        varsta.Location = new System.Drawing.Point(158, 428);
        varsta.Name = "varsta";
        varsta.PlaceholderText = "varsta";
        varsta.Size = new System.Drawing.Size(100, 27);
        varsta.TabIndex = 9;
        // 
        // cod_grupa
        // 
        cod_grupa.Location = new System.Drawing.Point(158, 467);
        cod_grupa.Name = "cod_grupa";
        cod_grupa.PlaceholderText = "cod_grupa";
        cod_grupa.Size = new System.Drawing.Size(100, 27);
        cod_grupa.TabIndex = 10;
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        BackColor = System.Drawing.SystemColors.Control;
        ClientSize = new System.Drawing.Size(1030, 635);
        Controls.Add(cod_grupa);
        Controls.Add(varsta);
        Controls.Add(nume);
        Controls.Add(Modifica);
        Controls.Add(Sterge);
        Controls.Add(Adauga);
        Controls.Add(CopiiDataGridView);
        Controls.Add(GrupaDataGridView);
        Location = new System.Drawing.Point(19, 19);
        Text = "Form1";
        ((System.ComponentModel.ISupportInitialize)GrupaDataGridView).EndInit();
        ((System.ComponentModel.ISupportInitialize)CopiiDataGridView).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.TextBox cod_grupa;

    private System.Windows.Forms.TextBox nume;
    private System.Windows.Forms.TextBox varsta;

    private System.Windows.Forms.Button Modifica;

    private System.Windows.Forms.Button Sterge;

    private System.Windows.Forms.Button Adauga;

    private System.Windows.Forms.DataGridView CopiiDataGridView;

    private System.Windows.Forms.DataGridView GrupaDataGridView;

    #endregion
}