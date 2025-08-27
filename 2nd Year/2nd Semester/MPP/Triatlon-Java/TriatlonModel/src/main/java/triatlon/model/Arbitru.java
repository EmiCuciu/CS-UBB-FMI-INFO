package triatlon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Arbitru")
public class Arbitru extends Entiti<Integer> {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    public Arbitru() {
        super(0);
    }

    public Arbitru(Integer integer, String username, String password, String first_name, String last_name) {
        super(integer);
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Arbitru arbitru = (Arbitru) o;
        return Objects.equals(getUsername(), arbitru.getUsername()) && Objects.equals(getPassword(), arbitru.getPassword()) && Objects.equals(getFirst_name(), arbitru.getFirst_name()) && Objects.equals(getLast_name(), arbitru.getLast_name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getFirst_name(), getLast_name());
    }

    @Override
    public String toString() {
        return "Arbitru{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
