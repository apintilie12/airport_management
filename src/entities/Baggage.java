import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Baggage implements Persistable {
    private int bid;
    private int weight;
    private BaggageType type;
    private int pid;
    private int fid;
    private String ownerName;

    public Baggage() {
        this.bid = -1;
    }

    public Baggage(int bid, int weight, BaggageType type, int pid, int fid) {
        this.bid = bid;
        this.weight = weight;
        this.type = type;
        this.pid = pid;
        this.fid = fid;
    }

    @Override
    public void saveToDatabase(Connection connection) {
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO baggage (weight, category, pid, fid) VALUES(?, ?, ?, ?);";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, this.weight);
                statement.setString(2, this.type.name());
                statement.setInt(3, this.pid);
                statement.setInt(4, fid);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE baggage SET weight = ?, category = ?, pid = ?, fid = ? WHERE bid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, this.weight);
                statement.setString(2, this.type.name());
                statement.setInt(3, this.pid);
                statement.setInt(4, fid);
                statement.setInt(5, this.bid);
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if (bid == -1) {
            return false;
        }
        String sql = "SELECT bid FROM baggage WHERE bid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.bid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("uid") == this.bid) {
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
        String sql = "SELECT * FROM baggage WHERE bid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.bid = rs.getInt("bid");
                this.weight = rs.getInt("weight");
                this.type = BaggageType.valueOf(rs.getString("category"));
                this.pid = rs.getInt("pid");
                this.fid = rs.getInt("fid");
            }
            statement.close();
            sql = "SELECT first_name, last_name FROM passengers JOIN main.baggage b on passengers.pid = b.pid WHERE b.bid = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, bid);
            rs = statement.executeQuery();
            if(rs.next()) {
                this.ownerName = rs.getString("first_name") + " " + rs.getString("last_name");
            }
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Baggage{" +
                "bid=" + bid +
                ", weight=" + weight +
                ", category='" + type + '\'' +
                ", owner='" + ownerName + '\'' +
                '}';
    }

    public void autoSetCategory() {
        if(0 < weight && weight <= 10) {
            type = BaggageType.CABIN;
        } else if(10 < weight && weight <= 20) {
            type = BaggageType.HOLD;
        } else if(20 < weight && weight <= 32) {
            type = BaggageType.HEAVY;
        }
    }
    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setType(BaggageType type) {
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getBid() {
        return bid;
    }

    public int getWeight() {
        return weight;
    }

    public BaggageType getType() {
        return type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
