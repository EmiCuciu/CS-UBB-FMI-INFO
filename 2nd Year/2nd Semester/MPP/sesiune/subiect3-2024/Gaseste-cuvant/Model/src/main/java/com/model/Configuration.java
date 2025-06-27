package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Configurations")
public class Configuration extends Entiti<Integer> {

    @Column(name = "letters")
    private String letters;

    @Column(name = "word1")
    private String word1;

    @Column(name = "word2")
    private String word2;

    @Column(name = "word3")
    private String word3;

    @Column(name = "word4")
    private String word4;

    public Configuration() {
        super(0);
    }

    public Configuration(Integer id, String letters, String word1, String word2, String word3, String word4) {
        super(id);
        this.letters = letters;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
        this.word4 = word4;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(letters, that.letters) && Objects.equals(word1, that.word1) && Objects.equals(word2, that.word2) && Objects.equals(word3, that.word3) && Objects.equals(word4, that.word4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letters, word1, word2, word3, word4);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "letters='" + letters + '\'' +
                ", word1='" + word1 + '\'' +
                ", word2='" + word2 + '\'' +
                ", word3='" + word3 + '\'' +
                ", word4='" + word4 + '\'' +
                '}';
    }
}
