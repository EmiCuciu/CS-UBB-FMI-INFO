package domain;

import java.time.LocalDateTime;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Prietenie extends Entity<Long> {

    LocalDateTime date;

    Tuplu<Long, Long> users;

    public Prietenie(Long idUser1, Long idUser2) {
        users = new Tuplu<>(min(idUser1, idUser2), max(idUser1, idUser2));
        date = LocalDateTime.now();
    }

    public Prietenie(Long idUser1, Long idUser2, LocalDateTime time) {
        users = new Tuplu<>(min(idUser1, idUser2), max(idUser1, idUser2));
        date = time;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getIdUser1() {
        return users.getLeft();
    }

    public Long getIdUser2() {
        return users.getRight();
    }

    public void setIdUser1(Long idUser1) {
        users.setLeft(idUser1);
    }

    public void setIdUser2(Long idUser2) {
        users.setRight(idUser2);
    }

}
