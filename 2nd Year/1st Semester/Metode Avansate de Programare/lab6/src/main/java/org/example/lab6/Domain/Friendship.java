package org.example.lab6.Domain;

public class Friendship extends Entity<Tuple<Long,Long>>{

        public Friendship(Long id1, Long id2) {
            setId(new Tuple<>(id1, id2));
        }

        public Friendship(Tuple<Long, Long> id) {
            setId(id);
        }

        public Long getId1() {
            return getId().getLeft();
        }

        public Long getId2() {
            return getId().getRight();
        }

        public void setId1(Long id1) {
            setId(new Tuple<>(id1, getId2()));
        }

        public void setId2(Long id2) {
            setId(new Tuple<>(getId1(), id2));
        }

        @Override
        public String toString() {
            return "Prietenie intre: " + getId1() + " si " + getId2();
        }

        @Override
        public boolean equals(Object obj) {
            return this.getId().equals(((Friendship) obj).getId());
        }

        @Override
        public int hashCode() {
            return getId().hashCode();
        }
}
