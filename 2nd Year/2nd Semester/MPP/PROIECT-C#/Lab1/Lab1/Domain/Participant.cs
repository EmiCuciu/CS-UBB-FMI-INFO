using System;
using System.Collections.Generic;
using System.Linq;

namespace Lab1.Domain {
    public class Participant : Entity<int> {
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public Dictionary<TipProba, int> punctaje;

        public Participant(int id, string firstName, string lastName) {
            this.Id = id;
            this.FirstName = firstName;
            this.LastName = lastName;
            this.punctaje = new Dictionary<TipProba, int>();

            foreach (TipProba tipProba in Enum.GetValues(typeof(TipProba))) {
                punctaje[tipProba] = 0;
            }
        }

        public int GetPunctajProba(TipProba tipProba) {
            return punctaje.TryGetValue(tipProba, out int punctaj) ? punctaj : 0;
        }

        public void SetPunctajProba(TipProba tipProba, int punctaj) {
            punctaje[tipProba] = punctaj;
        }

        public int GetPunctajTotal() {
            return punctaje.Values.Sum();
        }

        public override bool Equals(object obj) {
            if (obj is Participant other) {
                return FirstName == other.FirstName && LastName == other.LastName;
            }
            return false;
        }

        public override int GetHashCode() {
            unchecked {
                int hash = 17;
                hash = hash * 23 + (FirstName != null ? FirstName.GetHashCode() : 0);
                hash = hash * 23 + (LastName != null ? LastName.GetHashCode() : 0);
                return hash;
            }
        }
    }
}
