package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Intrebari")
public class Intrebare extends Entity<Integer> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "text")
    private String text;

    @Column(name = "raspuns_corect")
    private String raspunsCorect;

    @Column(name = "nivel")
    private int nivel;

    public Intrebare() {}

    public Intrebare(String text, String raspunsCorect, int nivel) {
        this.text = text;
        this.raspunsCorect = raspunsCorect;
        this.nivel = nivel;
    }

    public Intrebare(int id, String text, String raspunsCorect, int nivel) {
        this.setId(id);
        this.text = text;
        this.raspunsCorect = raspunsCorect;
        this.nivel = nivel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRaspunsCorect() {
        return raspunsCorect;
    }

    public void setRaspunsCorect(String raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Intrebare{" +
                "id=" + getId() +
                ", text='" + text + '\'' +
                ", raspunsCorect='" + raspunsCorect + '\'' +
                ", nivel=" + nivel +
                '}';
    }
}