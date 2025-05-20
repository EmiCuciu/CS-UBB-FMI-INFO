using Newtonsoft.Json;

namespace TriatlonModel
{
    [Serializable]
    public class Proba : Entity<int>
    {
        [JsonProperty("tip_proba")]
        public TipProba TipProba { get; set; }

        [JsonProperty("arbitru")]
        public Arbitru Arbitru { get; set; }

        [JsonProperty("id")]
        public new int Id
        {
            get => base.Id;
            set => base.Id = value;
        }

        public Proba(int id, TipProba tipProba, Arbitru arbitru)
        {
            this.Id = id;
            this.TipProba = tipProba;
            this.Arbitru = arbitru;
        }

        public override string ToString()
        {
            return $"{this.TipProba.GetDenumire()} {this.Arbitru}";
        }
    }
}