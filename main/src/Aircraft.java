public class Aircraft {


    private final int aid;
    private final String aircraftRegistration;
    private final String type;
    private final int paxCapacity;
    private final int holdCapacity;
    private String notes;


    public Aircraft(int aid, String aircraftRegistration, String type, int paxCapacity, int holdCapacity, String notes) {
        this.aid = aid;
        this.aircraftRegistration = aircraftRegistration;
        this.type = type;
        this.paxCapacity = paxCapacity;
        this.holdCapacity = holdCapacity;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "aid=" + aid +
                ", aircraftRegistration='" + aircraftRegistration + '\'' +
                ", type='" + type + '\'' +
                ", paxCapacity=" + paxCapacity +
                ", holdCapacity=" + holdCapacity +
                ", notes='" + notes + '\'' +
                '}';
    }


    public int getAid() {
        return aid;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public String getType() {
        return type;
    }

    public int getPaxCapacity() {
        return paxCapacity;
    }


    public int getHoldCapacity() {
        return holdCapacity;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}
