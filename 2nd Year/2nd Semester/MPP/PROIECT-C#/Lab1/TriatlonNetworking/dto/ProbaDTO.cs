using System;

namespace TriatlonNetworking.dto
{
    [Serializable]
    public class ProbaDTO
    {
        public int? Id { get; set; }
        public TipProbaDTO TipProba { get; set; }
        public ArbitruDTO Arbitru { get; set; }

        public ProbaDTO(int? id, TipProbaDTO tipProba, ArbitruDTO arbitru)
        {
            Id = id;
            TipProba = tipProba;
            Arbitru = arbitru;
        }
    }
}