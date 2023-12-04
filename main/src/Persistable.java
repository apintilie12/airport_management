import java.sql.Connection;
import java.sql.ResultSet;

public interface Persistable {
    void saveToDatabase(Connection connection);

    boolean isInDatabase(Connection connection);

    boolean isInDatabase(Connection connection, Object... args);

    void loadFromDatabase(Connection connection, Object... args);
}
