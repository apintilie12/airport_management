import java.sql.Connection;
import java.sql.ResultSet;

public abstract class Persistable {
    abstract void saveToDatabase(Connection connection);

    abstract Persistable loadFromDatabase(ResultSet resultSet);

    abstract boolean isInDatabase(Connection connection);

    abstract boolean isInDatabase(Connection connection, Object... args);

    abstract void loadFromDatabase(Connection connection, Object... args);
}
