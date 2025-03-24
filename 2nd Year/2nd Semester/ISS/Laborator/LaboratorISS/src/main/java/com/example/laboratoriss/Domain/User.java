package com.example.laboratoriss.Domain;

import java.util.Objects;

public class User extends Entity<Integer> {
    private String username;
    private String password;
    private UserType type;
    private String nume;
    private String prenume;
    private String sectie;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                type == user.type &&
                Objects.equals(nume, user.nume) &&
                Objects.equals(prenume, user.prenume) &&
                Objects.equals(sectie, user.sectie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, type, nume, prenume, sectie);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", sectie='" + sectie + '\'' +
                '}';
    }

    public String getSectie() {
        return sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }

}
