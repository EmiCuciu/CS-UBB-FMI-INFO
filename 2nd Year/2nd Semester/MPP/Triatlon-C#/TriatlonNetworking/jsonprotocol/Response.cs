using System.Text;
using TriatlonNetworking.dto;

namespace TriatlonNetworking.jsonprotocol;

[Serializable]
public class Response
{
    public ResponseType Type { get; set; }
    public string ErrorMessage { get; set; }

    // Individual DTOs
    public ArbitruDTO ArbitruDTO { get; set; }
    public ParticipantDTO ParticipantDTO { get; set; }
    public ProbaDTO ProbaDTO { get; set; }
    public RezultatDTO RezultatDTO { get; set; }

    // Arrays of DTOs
    public ArbitruDTO[] ArbitriiDTO { get; set; }
    public ParticipantDTO[] ParticipantiDTO { get; set; }
    public ProbaDTO[] ProbeDTO { get; set; }
    public RezultatDTO[] RezultateDTO { get; set; }

    public Response()
    {
    }

    public TipProbaDTO GetTipProba()
    {
        return ProbaDTO?.TipProba ?? default(TipProbaDTO);
    }

    public void SetTipProba(TipProbaDTO tipProba)
    {
        if (ProbaDTO != null)
        {
            ProbaDTO.TipProba = tipProba;
        }
    }

    public override string ToString()
    {
        var sb = new StringBuilder();
        sb.Append("Response{");
        sb.Append($"type={Type}, ");
        sb.Append($"errorMessage='{ErrorMessage}', ");
        sb.Append($"arbitruDTO={ArbitruDTO}, ");
        sb.Append(
            $"arbitriiDTO={string.Join(", ", (IEnumerable<ArbitruDTO>)(ArbitriiDTO ?? Array.Empty<ArbitruDTO>()))}, ");
        sb.Append($"participantDTO={ParticipantDTO}, ");
        sb.Append(
            $"participantiDTO={string.Join(", ", (IEnumerable<ParticipantDTO>)(ParticipantiDTO ?? Array.Empty<ParticipantDTO>()))}, ");
        sb.Append($"probaDTO={ProbaDTO}, ");
        sb.Append($"probeDTO={string.Join(", ", (IEnumerable<ProbaDTO>)(ProbeDTO ?? Array.Empty<ProbaDTO>()))}, ");
        sb.Append($"rezultatDTO={RezultatDTO}, ");
        sb.Append(
            $"rezultateDTO={string.Join(", ", (IEnumerable<RezultatDTO>)(RezultateDTO ?? Array.Empty<RezultatDTO>()))}");
        sb.Append("}");
        return sb.ToString();
    }
}