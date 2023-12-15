import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class Aircraft implements Persistable {


    private int aid;
    private String aircraftRegistration;
    private String type;
    private String manufacturer;
    private int paxCapacity;
    private int holdCapacity;
    private String notes;
    private ArrayList<Baggage> baggageList;

    public Aircraft() {
        this.aid = -1;
        this.aircraftRegistration = null;
    }

    public Aircraft(int aid, String aircraftRegistration, String type, int paxCapacity, int holdCapacity, String notes) {
        this.aid = aid;
        this.aircraftRegistration = aircraftRegistration;
        this.type = type;
        this.paxCapacity = paxCapacity;
        this.holdCapacity = holdCapacity;
        this.notes = notes;
        this.baggageList = new ArrayList<Baggage>();
    }

    @Override
    public void saveToDatabase(Connection connection) {
        for(Baggage b : this.baggageList) {
            b.saveToDatabase(connection);
        }
        baggageList.clear();
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO aircraft (aircraft_registration, notes, type_id) VALUES(?, ?, (SELECT id FROM aircraft_type WHERE name = ?));";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.aircraftRegistration);
                statement.setString(2, this.notes);
                statement.setString(3, this.type);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE aircraft SET aircraft_registration = ?, notes = ?, type_id = (SELECT id FROM aircraft_type WHERE name = ?) WHERE aid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.aircraftRegistration);
                statement.setString(2, this.notes);
                statement.setString(3, this.type);
                statement.setInt(4, this.aid);
                statement.execute();
                statement.close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if (aid == -1) {
            return false;
        }
        String sql = "SELECT aid FROM aircraft WHERE aid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.aid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("aid") == this.aid) {
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
            String sql = "SELECT aircraft_registration FROM aircraft WHERE aircraft_registration = ?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("aircraft_registration").equals((String) args[0])) {
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
            String sql = "SELECT * FROM airplanes WHERE aircraft_registration = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.aid = rs.getInt("aid");
                    this.aircraftRegistration = rs.getString("aircraft_registration");
                    this.type = rs.getString("type");
                    this.manufacturer = rs.getString("manufacturer");
                    this.paxCapacity = rs.getInt("pax_capacity");
                    this.holdCapacity = rs.getInt("hold_capacity");
                    this.notes = rs.getString("notes");
                }
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Vector<Aircraft> getAircraftVector(Connection connection) {
        Vector<Aircraft> airplanes = new Vector<>();
        String sql = "SELECT * FROM airplanes";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int aid = rs.getInt("aid");
                String aircraftRegistration = rs.getString("aircraft_registration");
                String notes = rs.getString("notes");
                String type = rs.getString("type");
                int paxCapacity = rs.getInt("pax_capacity");
                int holdCapacity = rs.getInt("hold_capacity");
                airplanes.add(new Aircraft(aid, aircraftRegistration, type, paxCapacity, holdCapacity, notes));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return airplanes;
    }


    @Override
    public String toString() {
        return "Aircraft{" + "aid=" + aid + ", aircraftRegistration='" + aircraftRegistration + '\'' + ", type='" + type + '\'' + ", paxCapacity=" + paxCapacity + ", holdCapacity=" + holdCapacity + ", notes='" + notes + '\'' + '}';
    }


    public int getAid() {
        return aid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
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

    public void setAid(int aid) {
        this.aid = aid;
    }

    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaxCapacity(int paxCapacity) {
        this.paxCapacity = paxCapacity;
    }

    public void setHoldCapacity(int holdCapacity) {
        this.holdCapacity = holdCapacity;
    }

    public void addBaggage(Baggage baggage) {
        this.baggageList.add(baggage);
    }

    public void loadBaggage(Connection connection) {
        if(this.isInDatabase(connection)) {
            String sql = "SELECT * FROM baggage WHERE fid = (SELECT fid FROM flights WHERE aircraft_registration = ? LIMIT 1)";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.aircraftRegistration);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int bid = rs.getInt("bid");
                    int weight = rs.getInt("weight");
                    String category = rs.getString("category");
                    int pid = rs.getInt("pid");
                    int fid = rs.getInt("fid");
                    this.baggageList.add(new Baggage(bid, weight, category, pid, fid));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {

        }
    }
}


