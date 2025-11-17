export interface MafiotProps {
    id? : string;
    nume : string;
    prenume : string;
    balanta : string;
    photo? : string; // base64 data for server
    photoPath? : string; // local filesystem path
    latitude? : number; // location latitude
    longitude? : number; // location longitude
    locationName? : string; // human-readable location name (optional)
}