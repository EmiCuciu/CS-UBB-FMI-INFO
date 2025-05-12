namespace TriatlonNetworking.dto
{
    [Serializable]
    public enum TipProbaDTO
    {
        NATATIE,
        CICLISM,
        ALERGARE
    }

    // Extension methods for TipProbaDTO
    public static class TipProbaDTOExtensions
    {
        public static string GetDenumire(this TipProbaDTO tipProba)
        {
            return tipProba switch
            {
                TipProbaDTO.NATATIE => "Natatie",
                TipProbaDTO.CICLISM => "Ciclism",
                TipProbaDTO.ALERGARE => "Alergare",
                _ => throw new ArgumentException("Invalid TipProbaDTO value")
            };
        }
    }
}