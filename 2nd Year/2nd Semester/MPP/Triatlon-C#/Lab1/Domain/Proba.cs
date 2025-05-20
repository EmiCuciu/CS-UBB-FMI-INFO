namespace Lab1.Domain {
    public class Proba : Entity<int> {
        public TipProba TipProba { get; set; }
        public Arbitru Arbitru { get; set; }

        public Proba(int id, TipProba denumire, Arbitru arbitru) {
            this.Id = id;
            this.TipProba = denumire;
            this.Arbitru = arbitru;
        }

        public override string ToString() {
            return $"{this.TipProba.GetDenumire()} {this.Arbitru}";
        }
    }
}
