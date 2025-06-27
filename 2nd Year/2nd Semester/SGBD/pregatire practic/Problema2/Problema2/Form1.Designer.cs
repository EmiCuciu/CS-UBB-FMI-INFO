namespace Problema2;

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
        ArtistiDataGridView = new System.Windows.Forms.DataGridView();
        MelodiiGridView = new System.Windows.Forms.DataGridView();
        Sterge = new System.Windows.Forms.Button();
        Modifica = new System.Windows.Forms.Button();
        Add = new System.Windows.Forms.Button();
        titlu = new System.Windows.Forms.TextBox();
        an_lansare = new System.Windows.Forms.TextBox();
        cod_artist = new System.Windows.Forms.TextBox();
        durata = new System.Windows.Forms.MaskedTextBox();
        ((System.ComponentModel.ISupportInitialize)ArtistiDataGridView).BeginInit();
        ((System.ComponentModel.ISupportInitialize)MelodiiGridView).BeginInit();
        SuspendLayout();
        //
        // ArtistiDataGridView
        //
        ArtistiDataGridView.ColumnHeadersHeight = 29;
        ArtistiDataGridView.Location = new System.Drawing.Point(12, 35);
        ArtistiDataGridView.Name = "ArtistiDataGridView";
        ArtistiDataGridView.RowHeadersWidth = 51;
        ArtistiDataGridView.Size = new System.Drawing.Size(419, 169);
        ArtistiDataGridView.TabIndex = 0;
        //
        // MelodiiGridView
        //
        MelodiiGridView.ColumnHeadersHeight = 29;
        MelodiiGridView.Location = new System.Drawing.Point(489, 35);
        MelodiiGridView.Name = "MelodiiGridView";
        MelodiiGridView.RowHeadersWidth = 51;
        MelodiiGridView.Size = new System.Drawing.Size(441, 167);
        MelodiiGridView.TabIndex = 1;
        //
        // Sterge
        //
        Sterge.Location = new System.Drawing.Point(627, 370);
        Sterge.Name = "Sterge";
        Sterge.Size = new System.Drawing.Size(91, 42);
        Sterge.TabIndex = 3;
        Sterge.Text = "Sterge";
        Sterge.UseVisualStyleBackColor = true;
        Sterge.Click += Sterge_Click;
        //
        // Modifica
        //
        Modifica.Location = new System.Drawing.Point(627, 418);
        Modifica.Name = "Modifica";
        Modifica.Size = new System.Drawing.Size(91, 36);
        Modifica.TabIndex = 4;
        Modifica.Text = "Modifica";
        Modifica.UseVisualStyleBackColor = true;
        Modifica.Click += Modifica_Click;
        //
        // Add
        //
        Add.Location = new System.Drawing.Point(627, 325);
        Add.Name = "Add";
        Add.Size = new System.Drawing.Size(91, 39);
        Add.TabIndex = 5;
        Add.Text = "Add";
        Add.UseVisualStyleBackColor = true;
        Add.Click += Add_Click;
        //
        // titlu
        //
        titlu.Location = new System.Drawing.Point(291, 301);
        titlu.Name = "titlu";
        titlu.PlaceholderText = "titlu";
        titlu.Size = new System.Drawing.Size(100, 27);
        titlu.TabIndex = 6;
        //
        // an_lansare
        //
        an_lansare.Location = new System.Drawing.Point(288, 361);
        an_lansare.Name = "an_lansare";
        an_lansare.PlaceholderText = "an_lansare";
        an_lansare.Size = new System.Drawing.Size(100, 27);
        an_lansare.TabIndex = 7;
        //
        // cod_artist
        //
        cod_artist.Location = new System.Drawing.Point(285, 475);
        cod_artist.Name = "cod_artist";
        cod_artist.PlaceholderText = "cod_artist";
        cod_artist.Size = new System.Drawing.Size(100, 27);
        cod_artist.TabIndex = 9;
        //
        // durata
        //
        durata.Location = new System.Drawing.Point(290, 427);
        durata.Mask = "00:00:00";
        durata.Name = "durata";
        durata.Size = new System.Drawing.Size(100, 27);
        durata.TabIndex = 10;
        //
        // Form1
        //
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        BackColor = System.Drawing.SystemColors.Control;
        ClientSize = new System.Drawing.Size(942, 527);
        Controls.Add(durata);
        Controls.Add(cod_artist);
        Controls.Add(an_lansare);
        Controls.Add(titlu);
        Controls.Add(Add);
        Controls.Add(Modifica);
        Controls.Add(Sterge);
        Controls.Add(MelodiiGridView);
        Controls.Add(ArtistiDataGridView);
        Location = new System.Drawing.Point(19, 19);
        ((System.ComponentModel.ISupportInitialize)ArtistiDataGridView).EndInit();
        ((System.ComponentModel.ISupportInitialize)MelodiiGridView).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.MaskedTextBox durata;

    private System.Windows.Forms.TextBox cod_artist;

    private System.Windows.Forms.TextBox an_lansare;

    private System.Windows.Forms.TextBox titlu;

    private System.Windows.Forms.Button Add;

    private System.Windows.Forms.Button Modifica;

    private System.Windows.Forms.Button Sterge;

    private System.Windows.Forms.DataGridView MelodiiGridView;

    private System.Windows.Forms.DataGridView ArtistiDataGridView;

    #endregion
}