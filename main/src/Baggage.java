import java.sql.Connection;
import java.sql.ResultSet;

public class Baggage{
    private final int bid;
    private final int weight;
    private final String category;

    public Baggage(int bid, int weight, String category) {
        this.bid = bid;
        this.weight = weight;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Baggage{" +
                "bid=" + bid +
                ", weight=" + weight +
                ", category='" + category + '\'' +
                '}';
    }

//    @Override
//    void saveToDatabase(Connection connection) {
//
//    }
//
//    @Override
//    public Baggage loadFromDatabase(ResultSet resultSet) {
//        if (resultSet != null) {
//            try {
//                int bid = resultSet.getInt("bid");
//                int weight = resultSet.getInt("weight");
//                String category = resultSet.getString("category");
//                return new Baggage(bid, weight, category);
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        return null;
//    }
//
//    @Override
//    void loadFromDatabase(Connection connection, Object... args) {
//        return;
//    }

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
