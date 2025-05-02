package com.example.laboratoriss.Domain;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "User")
public class User extends Entity<Integer> {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @Column(name = "nume")
    private String nume;

    @Column(name = "prenume")
    private String prenume;

    @Column(name = "sectie")
    private String sectie;

    public User() {
        // Default constructor required by Hibernate
    }

    public User(Integer integer, String username, String password, UserType type, String nume, String prenume) {
        super(integer);
        this.username = username;
        this.password = password;
        this.type = type;
        this.nume = nume;
        this.prenume = prenume;
    }

    public User(Integer integer, String username, String password, UserType type, String nume, String prenume, String sectie) {
        super(integer);
        this.username = username;
        this.password = password;
        this.type = type;
        this.nume = nume;
        this.prenume = prenume;
        this.sectie = sectie;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getSectie() {
        return sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}