import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Aircraft implements Persistable {


    private int aid;
    private String aircraftRegistration;
    private String type;
    private int paxCapacity;
    private int holdCapacity;
    private String notes;


    public Aircraft() {
        this.aid = -1;
    }

    public Aircraft(int aid, String aircraftRegistration, String type, int paxCapacity, int holdCapacity, String notes) {
        this.aid = aid;
        this.aircraftRegistration = aircraftRegistration;
        this.type = type;
        this.paxCapacity = paxCapacity;
        this.holdCapacity = holdCapacity;
        this.notes = notes;
    }

    @Override
    public void saveToDatabase(Connection connection) {
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO aircraft (aircraft_registration, type, pax_capacity, hold_capacity, notes) VALUES(?, ?, ?, ?, ?);";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.aircraftRegistration);
                statement.setString(2, this.type);
                statement.setInt(3, this.paxCapacity);
                statement.setInt(4, this.holdCapacity);
                statement.setString(5, this.notes);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE aircraft SET aircraft_registration = ?, type = ?, pax_capacity = ?, hold_capacity = ?, notes = ? WHERE aid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.aircraftRegistration);
                statement.setString(2, this.type);
                statement.setInt(3, this.paxCapacity);
                statement.setInt(4, this.holdCapacity);
                statement.setString(5, this.notes);
                statement.setInt(6, this.aid);
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
            String sql = "SELECT * FROM aircraft WHERE aircraft_registration = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.aid = rs.getInt("aid");
                    this.aircraftRegistration = rs.getString("aircraft_registration");
                    this.type = rs.getString("type");
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

    @Override
    public String toString() {
        return "Aircraft{" + "aid=" + aid + ", aircraftRegistration='" + aircraftRegistration + '\'' + ", type='" + type + '\'' + ", paxCapacity=" + paxCapacity + ", holdCapacity=" + holdCapacity + ", notes='" + notes + '\'' + '}';
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
}
