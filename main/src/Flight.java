public class Flight {

    private final int fid;
    private final String flightNumber;
    private final char type;

    private int eta;
    private int etd;
    private final String origin;
    private final String destination;
    private String notes;
    private int aircraftId;

    private String date;

    public Flight(int fid, String flightNumber, char type, int eta, int etd, String origin, String destination, String notes, int aircraft, String date) {
        this.fid = fid;
        this.flightNumber = flightNumber;
        this.type = type;
        this.eta = eta;
        this.etd = etd;
        this.origin = origin;
        this.destination = destination;
        this.notes = notes;
        this.aircraftId = aircraft;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "fid=" + fid +
                ", flightNumber='" + flightNumber + '\'' +
                ", type=" + type +
                ", eta=" + eta +
                ", etd=" + etd +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", notes='" + notes + '\'' +
                ", aircraft=" + aircraftId +
                ", date='" + date + '\'' +
                '}';
    }

    public int getFid() {
        return fid;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public void setEtd(int etd) {
        this.etd = etd;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public char getType() {
        return type;
    }

    public int getEta() {
        return eta;
    }

    public int getEtd() {
        return etd;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getNotes() {
        return notes;
    }

    public int getAircraftId() {
        return aircraftId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
