using TriatlonNetworking.dto;

namespace TriatlonNetworking.jsonprotocol;

public class Request
{
    public RequestType Type { get; set; }
    public ArbitruDTO ArbitruDTO { get; set; }
    public ParticipantDTO ParticipantDTO { get; set; }
    public RezultatDTO RezultatDTO { get; set; }
    public ProbaDTO ProbaDTO { get; set; }
    public TipProbaDTO TipProbaDTO { get; set; }

    public Request() { }

    public override string ToString()
    {
        return $"Request{{type={Type}, arbitruDTO={ArbitruDTO}, participantDTO={ParticipantDTO}, rezultatDTO={RezultatDTO}, probaDTO={ProbaDTO}, tipProbaDTO={TipProbaDTO}}}";
    }
}