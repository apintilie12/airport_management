import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class User implements Persistable {

    private int uid;
    private String username;
    private String password;
    private UserType userType;

    public User(int uid, String username, String password, UserType userType) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User() {
        this.uid = -1;
        this.username = null;
        this.password = null;
        this.userType = null;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public void saveToDatabase(Connection connection) {
        if (!isInDatabase(connection)) {
            String sql = "INSERT INTO users (username, password, user_type) VALUES(?, ?, ?);";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.username);
                statement.setString(2, this.password);
                statement.setString(3, this.userType.name());
                statement.execute();
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            String sql = "UPDATE users SET username = ?, password = ?, user_type = ? WHERE uid = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, this.username);
                statement.setString(2, this.password);
                statement.setString(3, this.userType.name());
                statement.setInt(4, this.uid);
                statement.execute();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if (uid == -1) {
            return false;
        }
        String sql = "SELECT uid FROM users WHERE uid = ?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.uid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getInt("uid") == this.uid) {
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
            String sql = "SELECT username FROM users WHERE username = ?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("username").equals((String) args[0])) {
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
            String sql = "SELECT * FROM users WHERE username = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String) args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    this.uid = rs.getInt("uid");
                    this.username = rs.getString("username");
                    this.password = rs.getString("password");
                    this.userType = UserType.valueOf(rs.getString("user_type"));
                }
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Vector<User> getUserVector(Connection connection, String order) {
        Vector<User> vector = new Vector<>();
        String sql = "SELECT * FROM users ";
        switch(order) {
            case "USERNAME ASC":
                sql += "ORDER BY username ASC";
                break;
            case "USERNAME DESC":
                sql += "ORDER BY username DESC";
                break;
            case "USER TYPE ASC":
                sql +="ORDER BY user_type ASC";
                break;
            case "USER TYPE DESC":
                sql += "ORDER BY user_type DESC";
                break;
            default:
                break;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int uid = rs.getInt("uid");
                String username = rs.getString("username");
                String password = rs.getString("password");
                UserType userType = UserType.valueOf(rs.getString("user_type"));
                vector.add(new User(uid, username, password, userType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}
