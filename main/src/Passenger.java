public class Passenger {
    private final int pid;
    private final String firstName;
    private final String lastName;
    private String phoneNumber;
    private final int flightId;

    public Passenger(int pid, String firstName, String lastName, String phoneNumber, int flight) {
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.flightId = flight;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "pid=" + pid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", flight=" + flightId +
                '}';
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPid() {
        return pid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getFlightId() {
        return flightId;
    }
}
