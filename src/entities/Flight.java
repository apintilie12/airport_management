import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private int maxPax;
    private int maxBags;
    private Vector<Passenger> passengers;
    private Vector<Baggage> baggages;

    public Flight() {
        this.fid = -1;
        this.flightNumber = null;
        this.type = null;
        this.passengers = new Vector<>();
        this.baggages = new Vector<>();
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
        this.passengers = new Vector<>();
        this.baggages = new Vector<>();
    }

    @Override
    public void saveToDatabase(Connection connection) {
        for(Passenger p : passengers) {
            p.saveToDatabase(connection);
        }
        for(Baggage b : baggages) {
            b.saveToDatabase(connection);
        }
        passengers.clear();
        baggages.clear();
        if(!isInDatabase(connection)) {
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
            } catch(Exception e) {
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

            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if(fid == -1) {
            return false;
        }
        String sql = "SELECT fid FROM flights WHERE fid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.fid);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                if(rs.getInt("fid") == this.fid) {
                    return true;
                }
            }
            statement.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isInDatabase(Connection connection, Object... args) {
        if(args.length > 0) {
            String sql = "SELECT flight_number FROM flights WHERE flight_number = ?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    if(rs.getString("flight_number").equals((String) args[0])) {
                        statement.close();
                        return true;
                    }
                }
                statement.close();
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void loadFromDatabase(Connection connection, Object... args) {
        if(args.length > 0) {
            String sql = "SELECT * FROM flights WHERE flight_number = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
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
                sql = "SELECT pax_capacity, hold_capacity FROM airplanes WHERE aircraft_registration = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, aircraftReg);
                rs = statement.executeQuery();
                if(rs.next()) {
                    this.maxPax = rs.getInt("pax_capacity");
                    this.maxBags = rs.getInt("hold_capacity");
                }
                statement.close();
            } catch(Exception e) {
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

    public static Vector<Flight> getFlightVector(Connection connection, String order, String flType) {
        Vector<Flight> flights = new Vector<>();
        String sql = "SELECT * FROM flights ";
        if(flType.equals("arrival")) {
            sql += " WHERE type = 'ARRIVAL' ";
        } else if(flType.equals("departure")){
            sql += " WHERE type = 'DEPARTURE' ";
        }
        switch(order) {
            case "FLIGHT NUMBER ASC":
                sql += " ORDER BY flight_number ASC";
                break;
            case "FLIGHT NUMBER DESC":
                sql += " ORDER BY flight_number DESC";
                break;
            case "ARRIVALS FIRST":
                sql += " ORDER BY type ASC";
                break;
            case "DEPARTURES FIRST":
                sql += " ORDER BY type DESC";
                break;
            default:
                break;

        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
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
                Flight fl = new Flight(fid, flightNumber, type, eta, etd, origin, destination, notes, aircraftRegistration, date);
                sql = "SELECT pax_capacity, hold_capacity FROM airplanes WHERE aircraft_registration = ?";
                PreparedStatement secondStatement = connection.prepareStatement(sql);
                secondStatement.setString(1, aircraftRegistration);
                ResultSet secondRs = secondStatement.executeQuery();
                if(secondRs.next()) {
                    fl.setMaxPax(secondRs.getInt("pax_capacity"));
                    fl.setMaxBags(secondRs.getInt("hold_capacity"));
                }
                secondStatement.close();
                flights.add(fl);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return flights;
    }

    public void addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
    }

    public void loadPassengers(Connection conn, String order) {
        if(this.isInDatabase(conn)) {
            passengers.clear();
            try {
                String sql = "SELECT * FROM passengers WHERE fid = ? ";
                switch(order) {
                    case "FIRST NAME ASC":
                        sql += " ORDER BY first_name ASC";
                        break;
                    case "FIRST NAME DESC":
                        sql += " ORDER BY first_name DESC";
                        break;
                    case "LAST NAME ASC":
                        sql += " ORDER BY last_name ASC";
                        break;
                    case "LAST NAME DESC":
                        sql += " ORDER BY last_name DESC";
                        break;
                    case "PHONE NO":
                        sql += " ORDER BY phone_number DESC";
                    default:
                        break;
                }
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, this.fid);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    int pid = rs.getInt("pid");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNo = rs.getString("phone_number");
                    passengers.add(new Passenger(pid, firstName, lastName, phoneNo, this.fid));
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addBaggage(Baggage baggage) {
        this.baggages.add(baggage);
    }

    public void loadBaggages(Connection conn, String order) {
        baggages.clear();
        if(this.isInDatabase(conn)) {
            String sql = "SELECT * FROM baggage WHERE fid = ? ";
            String[] bagSort = {"DEFAULT", "WEIGHT ASC", "WEIGHT DESC", "TYPE ASC", "TYPE DESC"};
            switch(order) {
                case "WEIGHT ASC":
                    sql += " ORDER BY weight ASC";
                    break;
                case "WEIGHT DESC":
                    sql += " ORDER BY weight DESC";
                    break;
                case "TYPE ASC":
                    sql += " ORDER BY category ASC";
                    break;
                case "TYPE DESC":
                    sql += " ORDER BY category DESC";
                    break;
                default:
                    break;
            }
            try {
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, this.fid);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    int bid = rs.getInt("bid");
                    int weight = rs.getInt("weight");
                    BaggageType type = BaggageType.valueOf(rs.getString("category"));
                    int pid = rs.getInt("pid");
                    int fid = rs.getInt("fid");
                    Baggage baggage = new Baggage(bid, weight, type, pid, fid);
                    sql = "SELECT first_name, last_name FROM passengers JOIN main.baggage b on passengers.pid = b.pid WHERE b.bid = ?";
                    PreparedStatement secondStatement = conn.prepareStatement(sql);
                    secondStatement.setInt(1, bid);
                    ResultSet secondRs = secondStatement.executeQuery();
                    if(secondRs.next()) {
                        baggage.setOwnerName(secondRs.getString("first_name") + " " + secondRs.getString("last_name"));
                    }
                    secondStatement.close();
                    this.baggages.add(baggage);
                }
                statement.close();
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Vector<Passenger> getPassengers() {
        return this.passengers;
    }

    public Vector<Baggage> getBaggages() {
        return baggages;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public int getMaxPax() {
        return maxPax;
    }

    public void setMaxPax(int maxPax) {
        this.maxPax = maxPax;
    }

    public int getMaxBags() {
        return maxBags;
    }

    public void setMaxBags(int maxBags) {
        this.maxBags = maxBags;
    }
}
