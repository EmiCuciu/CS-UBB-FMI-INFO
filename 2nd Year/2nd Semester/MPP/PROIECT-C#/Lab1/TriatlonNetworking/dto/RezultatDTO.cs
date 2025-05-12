using System;

namespace TriatlonNetworking.dto
{
    [Serializable]
    public class RezultatDTO
    {
        public int? Id { get; set; }
        public ParticipantDTO Participant { get; set; }
        public ArbitruDTO Arbitru { get; set; }
        public TipProbaDTO TipProba { get; set; }
        public int Punctaj { get; set; }

        public RezultatDTO(int? id, ParticipantDTO participant, ArbitruDTO arbitru, TipProbaDTO tipProba, int punctaj)
        {
            Id = id;
            Participant = participant;
            Arbitru = arbitru;
            TipProba = tipProba;
            Punctaj = punctaj;
        }
    }
}