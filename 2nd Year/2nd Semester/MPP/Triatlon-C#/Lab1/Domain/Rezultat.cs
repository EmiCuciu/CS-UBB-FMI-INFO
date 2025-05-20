namespace Lab1.Domain {
    public class Rezultat : Entity<int> {
        public Participant Participant { get; set; }
        public Arbitru Arbitru { get; set; }
        public TipProba TipProba { get; set; }
        public int Points { get; set; }

        public Rezultat(int id, Participant participant, Arbitru arbitru, TipProba proba, int points) {
            this.Id = id;
            this.Participant = participant;
            this.Arbitru = arbitru;
            this.TipProba = proba;
            this.Points = points;
        }

        public override string ToString() {
            return this.Participant + " " + this.Arbitru + " " + this.TipProba + " " + this.Points;
        }

        public override bool Equals(object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.GetType() != obj.GetType()) {
                return false;
            }
            Rezultat rezultat = (Rezultat)obj;
            return this.Id == rezultat.Id;
        }

        public override int GetHashCode() {
            return this.Id;
        }
    }
}
