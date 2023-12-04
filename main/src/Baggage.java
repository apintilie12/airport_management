import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Baggage implements Persistable {
    private int bid;
    private int weight;
    private String category;
    private int pid;
    private int fid;

    public Baggage() {
        this.bid = -1;
    }

    public Baggage(int bid, int weight, String category, int pid, int fid) {
        this.bid = bid;
        this.weight = weight;
        this.category = category;
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
                statement.setString(2, this.category);
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
                statement.setString(2, this.category);
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
                this.category = rs.getString("category");
                this.pid = rs.getInt("pid");
                this.fid = rs.getInt("fid");
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
                ", category='" + category + '\'' +
                '}';
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getCategory() {
        return category;
    }

}
