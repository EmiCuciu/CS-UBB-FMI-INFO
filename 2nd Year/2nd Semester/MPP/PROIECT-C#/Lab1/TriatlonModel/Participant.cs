namespace TriatlonModel
{
    [Serializable]
    public class Participant : Entity<int>
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public Dictionary<TipProba, int> punctaje;

        public Participant(int id, string firstName, string lastName)
        {
            this.Id = id;
            this.FirstName = firstName;
            this.LastName = lastName;
            this.punctaje = new Dictionary<TipProba, int>();

            foreach (TipProba tipProba in Enum.GetValues(typeof(TipProba)))
            {
                punctaje[tipProba] = 0;
            }
        }

        public int GetPunctajProba(TipProba tipProba)
        {
            return punctaje.TryGetValue(tipProba, out int punctaj) ? punctaj : 0;
        }

        public void SetPunctajProba(TipProba tipProba, int punctaj)
        {
            punctaje[tipProba] = punctaj;
        }

        public int GetPunctajTotal()
        {
            return punctaje.Values.Sum();
        }

        public override bool Equals(object obj)
        {
            if (obj is Participant other)
            {
                return FirstName == other.FirstName && LastName == other.LastName;
            }

            return false;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(FirstName, LastName);
        }

        public override string ToString()
        {
            return $"{FirstName} {LastName}";
        }
    }
}