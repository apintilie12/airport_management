public class Passenger {
    private final int pid;
    private final String firstName;
    private final String lastName;
    private String phoneNumber;
    private final Flight flight;

    public Passenger(int pid, String firstName, String lastName, String phoneNumber, Flight flight) {
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "pid=" + pid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", flight=" + flight +
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

    public Flight getFlight() {
        return flight;
    }
}
