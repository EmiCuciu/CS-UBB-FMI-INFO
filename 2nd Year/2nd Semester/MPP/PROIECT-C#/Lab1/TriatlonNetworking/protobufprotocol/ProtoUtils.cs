using TriatlonModel;
using proto = Triatlon.Network.ProtobufProtocol;

namespace TriatlonNetworking.protobufprotocol
{
    public static class ProtoUtils
    {
        //* REQUESTS
        public static proto.Request CreateLoginRequest(Arbitru arbitru)
        {
            proto.Request request = new proto.Request
            {
                Type = proto.Request.Types.Type.Login,
                Arbitru = ToProto(arbitru)
            };

            return request;
        }

        public static proto.Request CreateLogoutRequest(Arbitru arbitru)
        {
            proto.Request request = new proto.Request
            {
                Type = proto.Request.Types.Type.Logout,
                Arbitru = ToProto(arbitru)
            };

            return request;
        }

        public static proto.Request CreateAddResultRequest(Participant participant, Arbitru arbitru, TipProba tipProba,
            int punctaj)
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.AddResult,
                Participant = ToProto(participant),
                Arbitru = ToProto(arbitru),
                TipProba = ToProto(tipProba),
                Punctaj = punctaj
            };
        }

        public static proto.Request CreateGetResultsForProbaRequest(TipProba tipProba)
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.GetResultsForProba,
                TipProba = ToProto(tipProba)
            };
        }

        public static proto.Request CreateGetAllParticipantsRequest()
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.GetAllParticipants
            };
        }

        public static proto.Request CreateCalculateTotalScoreRequest(Participant participant)
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.CalculateTotalScore,
                Participant = ToProto(participant)
            };
        }

        //? Probabil crapa, trebuie ceva de genul arbitruCredentials - uita-te la java
        public static proto.Request CreateFindArbitruRequest(string username, string password)
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.FindArbitru,
                Arbitru = new proto.Arbitru
                {
                    Username = username,
                    Password = password
                }
            };
        }

        public static proto.Request CreateEmptyRequest(proto.Request.Types.Type type)
        {
            return new proto.Request
            {
                Type = type
            };
        }

        public static proto.Request CreateRegisterRequest(Arbitru arbitru)
        {
            return new proto.Request
            {
                Type = proto.Request.Types.Type.Register,
                Arbitru = ToProto(arbitru)
            };
        }

        //* RESPONSES
        public static proto.Response CreateOkResponse()
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.Ok
            };
        }

        public static proto.Response CreateErrorResponse(string text)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.Error,
                Error = text
            };
        }

        public static proto.Response CreateArbitruLoggedInResponse(Arbitru arbitru)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.RefreeLoggedIn,
                Arbitru = ToProto(arbitru)
            };
        }

        public static proto.Response CreateArbitruLoggedOutResponse(Arbitru arbitru)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.RefreeLoggedOut,
                Arbitru = ToProto(arbitru)
            };
        }

        public static proto.Response CreateAddedResultResponse(Rezultat rezultat)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.ResultatAdded,
                Rezultat = ToProto(rezultat)
            };
        }

        public static proto.Response CreateGetAllParticipantsResponse(List<Participant> participants)
        {
            var builder = new proto.Response
            {
                Type = proto.Response.Types.Type.GetAllParticipants
            };

            foreach (var participant in participants)
            {
                builder.Participants.Add(ToProto(participant));
            }

            return builder;
        }

        public static proto.Response CreateGetResultsForProbaResponse(List<Rezultat> rezultate)
        {
            var builder = new proto.Response
            {
                Type = proto.Response.Types.Type.GetResultsForProba
            };

            foreach (var rezultat in rezultate)
            {
                builder.Rezultate.Add(ToProto(rezultat));
            }

            return builder;
        }

        public static proto.Response CreateGetAllProbeResponse(List<Proba> probe)
        {
            var builder = new proto.Response
            {
                Type = proto.Response.Types.Type.GetAllProbe
            };

            foreach (var proba in probe)
            {
                builder.Probe.Add(ToProto(proba));
            }

            return builder;
        }

        public static proto.Response CreateCalculateTotalScoreResponse(int punctaj)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.Ok,
                TotalScore = punctaj
            };
        }

        public static proto.Response CreateGetProbaForArbitruResponse(TipProba tipProba)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.GetProbaForArbitru,
                TipProba = ToProto(tipProba)
            };
        }

        public static proto.Response CreateFindArbitruResponse(Arbitru arbitru)
        {
            return new proto.Response
            {
                Type = proto.Response.Types.Type.FindArbitru,
                Arbitru = ToProto(arbitru)
            };
        }

        public static proto.Response CreateGetAllResultsResponse(List<Rezultat> rezultate)
        {
            var builder = new proto.Response
            {
                Type = proto.Response.Types.Type.GetAllResults
            };

            foreach (var rezultat in rezultate)
            {
                builder.Rezultate.Add(ToProto(rezultat));
            }

            return builder;
        }






        public static proto.Arbitru ToProto(Arbitru arbitru)
        {
            if (arbitru == null) return null;

            var builder = new proto.Arbitru
            {
                Username = arbitru.Username,
                Password = arbitru.Password
            };

            if (arbitru.Id != null) builder.Id = arbitru.Id;

            if (arbitru.FirstName != null) builder.FirstName = arbitru.FirstName;

            if (arbitru.LastName != null) builder.LastName = arbitru.LastName;

            return builder;
        }

        public static Arbitru FromProto(proto.Arbitru protoArbitru)
        {
            if (protoArbitru == null) return null;

            Arbitru arbitru = new Arbitru(
                protoArbitru.Id,
                protoArbitru.Username,
                protoArbitru.Password,
                protoArbitru.FirstName,
                protoArbitru.LastName
            );

            return arbitru;
        }

        public static proto.Participant ToProto(Participant participant)
        {
            if (participant == null) return null;

            var builder = new proto.Participant
            {
                Id = participant.Id,
                FirstName = participant.FirstName,
                LastName = participant.LastName
            };

            return builder;
        }

        public static Participant FromProto(proto.Participant protoParticipant)
        {
            if (protoParticipant == null) return null;

            Participant participant = new Participant(
                protoParticipant.Id,
                protoParticipant.FirstName,
                protoParticipant.LastName
            );

            return participant;
        }

        public static proto.TipProba ToProto(TipProba tipProba)
        {
            if (tipProba == null) return null;

            var builder = new proto.TipProba
            {
                Denumire = tipProba.GetDenumire() // posibil tipProba.ToString()
            };

            return builder;
        }

        public static TipProba FromProto(proto.TipProba protoTipProba)
        {
            switch (protoTipProba.Denumire)
            {
                case "Natatie":
                    return TipProba.NATATIE;
                case "Ciclism":
                    return TipProba.CICLISM;
                case "Alergare":
                    return TipProba.ALERGARE;
            }

            throw new ArgumentOutOfRangeException(nameof(protoTipProba), protoTipProba, null);
        }

        public static proto.Proba ToProto(Proba proba)
        {
            if (proba == null) return null;

            var builder = new proto.Proba
            {
                Id = proba.Id,
                TipProba = ToProto(proba.TipProba),
                Arbitru = ToProto(proba.Arbitru)
            };

            return builder;
        }

        public static Proba FromProto(proto.Proba protoProba)
        {
            if (protoProba == null) return null;

            return new Proba(
                protoProba.Id,
                FromProto(protoProba.TipProba),
                FromProto(protoProba.Arbitru)
            );
        }

        public static proto.Rezultat ToProto(Rezultat rezultat)
        {
            if (rezultat == null) return null;

            var builder = new proto.Rezultat
            {
                Id = rezultat.Id,
                Participant = ToProto(rezultat.Participant),
                Arbitru = ToProto(rezultat.Arbitru),
                TipProba = ToProto(rezultat.TipProba),
                Punctaj = rezultat.Points
            };

            return builder;
        }

        public static Rezultat FromProto(proto.Rezultat protoRezultat)
        {
            if (protoRezultat == null) return null;

            return new Rezultat(
                protoRezultat.Id,
                FromProto(protoRezultat.Participant),
                FromProto(protoRezultat.Arbitru),
                FromProto(protoRezultat.TipProba),
                protoRezultat.Punctaj
            );
        }
    }
}