namespace Problema1;

partial class Briose
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
        CofetariiDataGridView = new System.Windows.Forms.DataGridView();
        BrioseDataGridView = new System.Windows.Forms.DataGridView();
        Add_briosa = new System.Windows.Forms.Button();
        Delete_briosa = new System.Windows.Forms.Button();
        Update_briosa = new System.Windows.Forms.Button();
        nume_briosa = new System.Windows.Forms.TextBox();
        descriere = new System.Windows.Forms.TextBox();
        pret = new System.Windows.Forms.TextBox();
        cod_cofetarie = new System.Windows.Forms.TextBox();
        ((System.ComponentModel.ISupportInitialize)CofetariiDataGridView).BeginInit();
        ((System.ComponentModel.ISupportInitialize)BrioseDataGridView).BeginInit();
        SuspendLayout();
        //
        // CofetariiDataGridView
        //
        CofetariiDataGridView.ColumnHeadersHeight = 29;
        CofetariiDataGridView.Location = new System.Drawing.Point(12, 12);
        CofetariiDataGridView.Name = "CofetariiDataGridView";
        CofetariiDataGridView.RowHeadersWidth = 51;
        CofetariiDataGridView.Size = new System.Drawing.Size(458, 194);
        CofetariiDataGridView.TabIndex = 0;
        //
        // BrioseDataGridView
        //
        BrioseDataGridView.ColumnHeadersHeight = 29;
        BrioseDataGridView.Location = new System.Drawing.Point(512, 12);
        BrioseDataGridView.Name = "BrioseDataGridView";
        BrioseDataGridView.RowHeadersWidth = 51;
        BrioseDataGridView.Size = new System.Drawing.Size(464, 194);
        BrioseDataGridView.TabIndex = 1;
        //
        // Add_briosa
        //
        Add_briosa.Location = new System.Drawing.Point(752, 414);
        Add_briosa.Name = "Add_briosa";
        Add_briosa.Size = new System.Drawing.Size(75, 34);
        Add_briosa.TabIndex = 2;
        Add_briosa.Text = "Add";
        Add_briosa.UseVisualStyleBackColor = true;
        Add_briosa.Click += Add_briosa_Click;
        //
        // Delete_briosa
        //
        Delete_briosa.Location = new System.Drawing.Point(752, 454);
        Delete_briosa.Name = "Delete_briosa";
        Delete_briosa.Size = new System.Drawing.Size(75, 30);
        Delete_briosa.TabIndex = 3;
        Delete_briosa.Text = "Delete";
        Delete_briosa.UseVisualStyleBackColor = true;
        Delete_briosa.Click += Delete_briosa_Click;
        //
        // Update_briosa
        //
        Update_briosa.Location = new System.Drawing.Point(752, 490);
        Update_briosa.Name = "Update_briosa";
        Update_briosa.Size = new System.Drawing.Size(75, 29);
        Update_briosa.TabIndex = 4;
        Update_briosa.Text = "Update";
        Update_briosa.UseVisualStyleBackColor = true;
        Update_briosa.Click += Update_briosa_Click;
        //
        // nume_briosa
        //
        nume_briosa.Location = new System.Drawing.Point(207, 378);
        nume_briosa.Name = "nume_briosa";
        nume_briosa.PlaceholderText = "nume_briosa";
        nume_briosa.Size = new System.Drawing.Size(100, 27);
        nume_briosa.TabIndex = 6;
        //
        // descriere
        //
        descriere.Location = new System.Drawing.Point(207, 411);
        descriere.Name = "descriere";
        descriere.PlaceholderText = "descriere";
        descriere.Size = new System.Drawing.Size(100, 27);
        descriere.TabIndex = 7;
        //
        // pret
        //
        pret.Location = new System.Drawing.Point(207, 444);
        pret.Name = "pret";
        pret.PlaceholderText = "pret";
        pret.Size = new System.Drawing.Size(100, 27);
        pret.TabIndex = 8;
        //
        // cod_cofetarie
        //
        cod_cofetarie.Location = new System.Drawing.Point(207, 479);
        cod_cofetarie.Name = "cod_cofetarie";
        cod_cofetarie.PlaceholderText = "cod_cofetarie";
        cod_cofetarie.Size = new System.Drawing.Size(100, 27);
        cod_cofetarie.TabIndex = 9;
        //
        // Briose
        //
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(982, 953);
        Controls.Add(cod_cofetarie);
        Controls.Add(pret);
        Controls.Add(descriere);
        Controls.Add(nume_briosa);
        Controls.Add(Update_briosa);
        Controls.Add(Delete_briosa);
        Controls.Add(Add_briosa);
        Controls.Add(BrioseDataGridView);
        Controls.Add(CofetariiDataGridView);
        Text = "Form1";
        ((System.ComponentModel.ISupportInitialize)CofetariiDataGridView).EndInit();
        ((System.ComponentModel.ISupportInitialize)BrioseDataGridView).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.Button Delete_briosa;

    private System.Windows.Forms.TextBox cod_cofetarie;

    private System.Windows.Forms.TextBox pret;

    private System.Windows.Forms.TextBox descriere;

    private System.Windows.Forms.TextBox nume_briosa;

    private System.Windows.Forms.Button Add_briosa;
    private System.Windows.Forms.Button Update_briosa;

    private System.Windows.Forms.DataGridView BrioseDataGridView;

    private System.Windows.Forms.DataGridView CofetariiDataGridView;

    #endregion
}