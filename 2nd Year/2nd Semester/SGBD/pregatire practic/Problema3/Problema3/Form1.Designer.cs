namespace Problema3;

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
        ProducatoriDataGridView = new System.Windows.Forms.DataGridView();
        BiscuitiDataGridView = new System.Windows.Forms.DataGridView();
        nr_calorii = new System.Windows.Forms.TextBox();
        Pret = new System.Windows.Forms.TextBox();
        cod_prod = new System.Windows.Forms.TextBox();
        nume_b = new System.Windows.Forms.TextBox();
        Adauga = new System.Windows.Forms.Button();
        Sterge = new System.Windows.Forms.Button();
        Modifica = new System.Windows.Forms.Button();
        ((System.ComponentModel.ISupportInitialize)ProducatoriDataGridView).BeginInit();
        ((System.ComponentModel.ISupportInitialize)BiscuitiDataGridView).BeginInit();
        SuspendLayout();
        // 
        // ProducatoriDataGridView
        // 
        ProducatoriDataGridView.ColumnHeadersHeight = 29;
        ProducatoriDataGridView.Location = new System.Drawing.Point(12, 60);
        ProducatoriDataGridView.Name = "ProducatoriDataGridView";
        ProducatoriDataGridView.RowHeadersWidth = 51;
        ProducatoriDataGridView.Size = new System.Drawing.Size(524, 150);
        ProducatoriDataGridView.TabIndex = 0;
        //
        // BiscuitiDataGridView
        //
        BiscuitiDataGridView.ColumnHeadersHeight = 29;
        BiscuitiDataGridView.Location = new System.Drawing.Point(553, 60);
        BiscuitiDataGridView.Name = "BiscuitiDataGridView";
        BiscuitiDataGridView.RowHeadersWidth = 51;
        BiscuitiDataGridView.Size = new System.Drawing.Size(541, 150);
        BiscuitiDataGridView.TabIndex = 1;
        //
        // nr_calorii
        //
        nr_calorii.Location = new System.Drawing.Point(127, 295);
        nr_calorii.Name = "nr_calorii";
        nr_calorii.PlaceholderText = "nr_calorii";
        nr_calorii.Size = new System.Drawing.Size(100, 27);
        nr_calorii.TabIndex = 2;
        //
        // Pret
        //
        Pret.Location = new System.Drawing.Point(127, 328);
        Pret.Name = "Pret";
        Pret.PlaceholderText = "Pret";
        Pret.Size = new System.Drawing.Size(100, 27);
        Pret.TabIndex = 3;
        //
        // cod_prod
        //
        cod_prod.Location = new System.Drawing.Point(127, 361);
        cod_prod.Name = "cod_prod";
        cod_prod.PlaceholderText = "cod_prod";
        cod_prod.Size = new System.Drawing.Size(100, 27);
        cod_prod.TabIndex = 4;
        //
        // nume_b
        //
        nume_b.Location = new System.Drawing.Point(127, 262);
        nume_b.Name = "nume_b";
        nume_b.PlaceholderText = "nume_b";
        nume_b.Size = new System.Drawing.Size(100, 27);
        nume_b.TabIndex = 5;
        //
        // Adauga
        //
        Adauga.Location = new System.Drawing.Point(629, 255);
        Adauga.Name = "Adauga";
        Adauga.Size = new System.Drawing.Size(75, 41);
        Adauga.TabIndex = 6;
        Adauga.Text = "Adauga";
        Adauga.UseVisualStyleBackColor = true;
        Adauga.Click += new System.EventHandler(Add_Click);
        //
        // Sterge
        //
        Sterge.Location = new System.Drawing.Point(629, 306);
        Sterge.Name = "Sterge";
        Sterge.Size = new System.Drawing.Size(75, 39);
        Sterge.TabIndex = 7;
        Sterge.Text = "Sterge";
        Sterge.UseVisualStyleBackColor = true;
        Sterge.Click += new System.EventHandler(Sterge_Click);
        //
        // Modifica
        //
        Modifica.Location = new System.Drawing.Point(629, 351);
        Modifica.Name = "Modifica";
        Modifica.Size = new System.Drawing.Size(89, 30);
        Modifica.TabIndex = 8;
        Modifica.Text = "Modifica";
        Modifica.UseVisualStyleBackColor = true;
        Modifica.Click += new System.EventHandler(Modifica_Click);
        //
        // Form1
        //
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(1106, 696);
        Controls.Add(Modifica);
        Controls.Add(Sterge);
        Controls.Add(Adauga);
        Controls.Add(nume_b);
        Controls.Add(cod_prod);
        Controls.Add(Pret);
        Controls.Add(nr_calorii);
        Controls.Add(BiscuitiDataGridView);
        Controls.Add(ProducatoriDataGridView);
        Text = "Form1";
        ((System.ComponentModel.ISupportInitialize)ProducatoriDataGridView).EndInit();
        ((System.ComponentModel.ISupportInitialize)BiscuitiDataGridView).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.TextBox Pret;
    private System.Windows.Forms.TextBox cod_prod;

    private System.Windows.Forms.Button Modifica;

    private System.Windows.Forms.Button Adauga;

    private System.Windows.Forms.DataGridView BiscuitiDataGridView;
    private System.Windows.Forms.Button Sterge;

    private System.Windows.Forms.Button button3;

    private System.Windows.Forms.TextBox nume_b;
    private System.Windows.Forms.Button button1;

    private System.Windows.Forms.TextBox textBox3;

    private System.Windows.Forms.TextBox textBox2;

    private System.Windows.Forms.TextBox nr_calorii;

    private System.Windows.Forms.DataGridView ProducatoriDataGridView;

    #endregion
}