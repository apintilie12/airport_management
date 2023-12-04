import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

public class Flight implements Persistable {

    private int fid;
    private String flightNumber;
    private String type;

    private int eta;
    private int etd;
    private String origin;
    private String destination;
    private String notes;
    private String aircraftReg;

    private String date;

    public Flight() {
        this.fid = -1;
    }

    public Flight(int fid, String flightNumber, String type, int eta, int etd, String origin, String destination, String notes, String aircraft, String date) {
        this.fid = fid;
        this.flightNumber = flightNumber;
        this.type = type;
        this.eta = eta;
        this.etd = etd;
        this.origin = origin;
        this.destination = destination;
        this.notes = notes;
        this.aircraftReg = aircraft;
        this.date = date;
    }

    @Override
    public void saveToDatabase(Connection connection) {
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO flights (flight_number, type, eta, etd, origin, destination, notes, aircraft_registration, date) VALUES(?, ?, ?, ?, ? ,? ,? ,? ,?);";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.flightNumber);
                statement.setString(2, this.type);
                statement.setInt(3, this.eta);
                statement.setInt(4, this.etd);
                statement.setString(5, this.origin);
                statement.setString(6, this.destination);
                statement.setString(7, this.notes);
                statement.setString(8, this.aircraftReg);
                statement.setString(9, this.date);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE flights SET flight_number = ?, type = ?, eta = ?, etd = ?, origin = ?, destination = ?, notes = ?, aircraft_registration = ?, date = ? WHERE fid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.flightNumber);
                statement.setString(2, this.type);
                statement.setInt(3, this.eta);
                statement.setInt(4, this.etd);
                statement.setString(5, this.origin);
                statement.setString(6, this.destination);
                statement.setString(7, this.notes);
                statement.setString(8, this.aircraftReg);
                statement.setString(9, this.date);
                statement.setInt(10, this.fid);
                statement.execute();
                statement.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if (fid == -1) {
            return false;
        }
        String sql = "SELECT fid FROM flights WHERE fid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.fid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("fid") == this.fid) {
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
        if (args.length > 0) {
            String sql = "SELECT flight_number FROM flights WHERE flight_number = ?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("flight_number").equals((String) args[0])) {
                        statement.close();
                        return true;
                    }
                }
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void loadFromDatabase(Connection connection, Object... args) {
        if (args.length > 0) {
            String sql = "SELECT * FROM flights WHERE flight_number = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.fid = rs.getInt("fid");
                    this.flightNumber = rs.getString("flight_number");
                    this.type = rs.getString("type");
                    this.eta = rs.getInt("eta");
                    this.etd = rs.getInt("etd");
                    this.origin = rs.getString("origin");
                    this.destination = rs.getString("destination");
                    this.notes = rs.getString("notes");
                    this.aircraftReg = rs.getString("aircraft_registration");
                    this.date = rs.getString("date");
                }
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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
                ", aircraft=" + aircraftReg +
                ", date='" + date + '\'' +
                '}';
    }

    public static Vector<Flight> getFlightVector(Connection connection) {
        Vector<Flight> flights = new Vector<>();
        String sql = "SELECT * FROM flights";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int fid = rs.getInt("fid");
                String flightNumber = rs.getString("flight_number");
                String type = rs.getString("type");
                int eta = rs.getInt("eta");
                int etd = rs.getInt("etd");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");
                String notes = rs.getString("notes");
                String aircraftRegistration = rs.getString("aircraft_registration");
                String date = rs.getString("date");
                flights.add(new Flight(fid, flightNumber, type, eta, etd, origin, destination, notes, aircraftRegistration, date));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flights;
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

    public void setAircraftReg(String aircraftReg) {
        this.aircraftReg = aircraftReg;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getType() {
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

    public String getAircraftReg() {
        return aircraftReg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
