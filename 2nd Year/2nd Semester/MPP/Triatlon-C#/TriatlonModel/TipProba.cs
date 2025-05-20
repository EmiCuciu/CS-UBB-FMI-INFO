using System;

namespace TriatlonModel {
    public enum TipProba {
        NATATIE,
        CICLISM,
        ALERGARE
    }

    public static class TipProbaExtensions {
        public static string GetDenumire(this TipProba tipProba) {
            switch (tipProba) {
                case TipProba.NATATIE:
                    return "Natatie";
                case TipProba.CICLISM:
                    return "Ciclism";
                case TipProba.ALERGARE:
                    return "Alergare";
                default:
                    throw new ArgumentOutOfRangeException(nameof(tipProba), tipProba, null);
            }
        }
    }
}