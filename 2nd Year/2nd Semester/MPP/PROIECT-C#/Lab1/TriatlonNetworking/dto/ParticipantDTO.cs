namespace TriatlonNetworking.dto
{
    [Serializable]
    public class ParticipantDTO
    {
        public int? Id { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        private Dictionary<TipProbaDTO, int> Punctaje { get; set; }
        public int TotalPoints { get; private set; }

        public ParticipantDTO(int? id, string firstName, string lastName)
        {
            Id = id;
            FirstName = firstName;
            LastName = lastName;
            Punctaje = new Dictionary<TipProbaDTO, int>();
            TotalPoints = 0;
        }

        public int GetPunctajProba(TipProbaDTO tipProba)
        {
            return Punctaje.ContainsKey(tipProba) ? Punctaje[tipProba] : 0;
        }

        public void SetPunctajProba(TipProbaDTO tipProba, int punctaj)
        {
            Punctaje[tipProba] = punctaj;
            RecalculateTotalPoints();
        }

        private void RecalculateTotalPoints()
        {
            TotalPoints = Punctaje.Values.Sum();
        }

        public override string ToString()
        {
            return $"ParticipantDTO{{id={Id}, firstName='{FirstName}', lastName='{LastName}'}}";
        }
    }
}