package org.example.lab8.domain;

public class Utilizator {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String path_to_profile_picture;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Utilizator() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPath_to_profile_picture() {
        return path_to_profile_picture;
    }

    public void setPath_to_profile_picture(String path_to_profile_picture) {
        this.path_to_profile_picture = path_to_profile_picture;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}