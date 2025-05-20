using System;
using System.Collections.Generic;
using System.Linq;
using TriatlonModel;

namespace TriatlonNetworking.dto
{
    public static class DTOUtils
    {
        // Convert domain objects to DTOs

        public static ArbitruDTO GetDTO(Arbitru arbitru)
        {
            if (arbitru == null) return null;
            return new ArbitruDTO(
                arbitru.Id,
                arbitru.Username,
                arbitru.Password,
                arbitru.FirstName,
                arbitru.LastName
            );
        }

        public static ParticipantDTO GetDTO(Participant participant)
        {
            if (participant == null) return null;
            var dto = new ParticipantDTO(
                participant.Id,
                participant.FirstName,
                participant.LastName
            );

            // Copy punctaje from participant to DTO
            foreach (TipProba tipProba in Enum.GetValues(typeof(TipProba)))
            {
                int punctaj = participant.GetPunctajProba(tipProba);
                dto.SetPunctajProba(GetTipProbaDTO(tipProba), punctaj);
            }

            return dto;
        }

        public static RezultatDTO GetDTO(Rezultat rezultat)
        {
            if (rezultat == null) return null;
            return new RezultatDTO(
                rezultat.Id,
                GetDTO(rezultat.Participant),
                GetDTO(rezultat.Arbitru),
                GetTipProbaDTO(rezultat.TipProba),
                rezultat.Points
            );
        }

        public static ProbaDTO GetDTO(int? id, TipProba tipProba, Arbitru arbitru)
        {
            if (tipProba == null) return null;
            return new ProbaDTO(
                id,
                GetTipProbaDTO(tipProba),
                GetDTO(arbitru)
            );
        }

        public static TipProbaDTO GetTipProbaDTO(TipProba tipProba)
        {
            return tipProba switch
            {
                TipProba.NATATIE => TipProbaDTO.NATATIE,
                TipProba.CICLISM => TipProbaDTO.CICLISM,
                TipProba.ALERGARE => TipProbaDTO.ALERGARE,
                _ => throw new ArgumentException("Invalid TipProba value")
            };
        }

        // Convert arrays of domain objects to arrays of DTOs

        public static ArbitruDTO[] GetDTO(Arbitru[] arbitri)
        {
            if (arbitri == null) return null;
            return arbitri.Select(GetDTO).ToArray();
        }

        public static RezultatDTO[] GetDTO(Rezultat[] rezultate)
        {
            if (rezultate == null) return null;
            return rezultate.Select(GetDTO).ToArray();
        }

        // Convert DTOs to domain objects

        public static Arbitru GetFromDTO(ArbitruDTO dto)
        {
            if (dto == null) return null;
            return new Arbitru(
                dto.Id ?? 0,
                dto.Username,
                dto.Password,
                dto.FirstName,
                dto.LastName
            );
        }

        public static Participant GetFromDTO(ParticipantDTO dto)
        {
            if (dto == null) return null;
            var participant = new Participant(
                dto.Id ?? 0,
                dto.FirstName,
                dto.LastName
            );

            // Copy punctaje from DTO to participant
            foreach (TipProbaDTO tipProbaDTO in Enum.GetValues(typeof(TipProbaDTO)))
            {
                TipProba tipProba = GetFromDTO(tipProbaDTO);
                if (tipProba != null)
                {
                    int punctaj = dto.GetPunctajProba(tipProbaDTO);
                    participant.SetPunctajProba(tipProba, punctaj);
                }
            }

            return participant;
        }

        public static Rezultat GetFromDTO(RezultatDTO dto)
        {
            if (dto == null) return null;
            return new Rezultat(
                dto.Id ?? 0,
                GetFromDTO(dto.Participant),
                GetFromDTO(dto.Arbitru),
                GetFromDTO(dto.TipProba),
                dto.Punctaj
            );
        }

        public static TipProba GetFromDTO(TipProbaDTO tipProbaDTO)
        {
            return tipProbaDTO switch
            {
                TipProbaDTO.NATATIE => TipProba.NATATIE,
                TipProbaDTO.CICLISM => TipProba.CICLISM,
                TipProbaDTO.ALERGARE => TipProba.ALERGARE,
                _ => throw new ArgumentException("Invalid TipProbaDTO value")
            };
        }

        // Convert arrays of DTOs to arrays of domain objects

        public static Arbitru[] GetFromDTO(ArbitruDTO[] dtos)
        {
            if (dtos == null) return null;
            return dtos.Select(GetFromDTO).ToArray();
        }

        public static Rezultat[] GetFromDTO(RezultatDTO[] dtos)
        {
            if (dtos == null) return null;
            return dtos.Select(GetFromDTO).ToArray();
        }

        public static IEnumerable<Participant> GetFromDTO(ParticipantDTO[] participantiDTO)
        {
            if (participantiDTO == null) return null;
            var participanti = new Dictionary<int?, Participant>();
            foreach (var dto in participantiDTO)
            {
                if (dto.Id.HasValue)
                    participanti[dto.Id] = GetFromDTO(dto);
            }

            return participanti.Values;
        }
    }
}