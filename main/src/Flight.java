public class Flight {

    private final int fid;
    private final String flightNumber;
    private final char type;

    private int eta;
    private int etd;
    private final String origin;
    private final String destination;
    private String notes;
    private Aircraft aircraft;

    private String date;

    public Flight(int fid, String flightNumber, char type, int eta, int etd, String origin, String destination, String notes, Aircraft aircraft, String date) {
        this.fid = fid;
        this.flightNumber = flightNumber;
        this.type = type;
        this.eta = eta;
        this.etd = etd;
        this.origin = origin;
        this.destination = destination;
        this.notes = notes;
        this.aircraft = aircraft;
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
                ", aircraft=" + aircraft +
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

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
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

    public Aircraft getAircraft() {
        return aircraft;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
