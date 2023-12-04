import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Passenger implements Persistable {
    private int pid;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int flightId;

    public Passenger() {
        this.pid = -1;
    }

    public Passenger(int pid, String firstName, String lastName, String phoneNumber, int flight) {
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.flightId = flight;
    }

    @Override
    public void saveToDatabase(Connection connection) {
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO passengers (first_name, last_name, phone_number, fid) VALUES(?, ?, ?, ?);";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.firstName);
                statement.setString(2, this.lastName);
                statement.setString(3, this.phoneNumber);
                statement.setInt(4, this.flightId);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE passengers SET first_name = ?, last_name = ?, phone_number = ?, fid = ? WHERE pid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.firstName);
                statement.setString(2, this.lastName);
                statement.setString(3, this.phoneNumber);
                statement.setInt(4, this.flightId);
                statement.setInt(5, this.pid);
                statement.execute();
                statement.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if (pid == -1) {
            return false;
        }
        String sql = "SELECT pid FROM passengers WHERE pid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.pid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("pid") == this.pid) {
                    return true;
                }
            }
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isInDatabase(Connection connection, Object... args) {
        return isInDatabase(connection);
    }

    @Override
    public void loadFromDatabase(Connection connection, Object... args) {
        String sql = "SELECT * FROM passengers WHERE pid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.pid = rs.getInt("pid");
                this.firstName = rs.getString("first_name");
                this.lastName = rs.getString("last_name");
                this.phoneNumber = rs.getString("phone_number");
                this.flightId = rs.getInt("fid");
            }
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
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
