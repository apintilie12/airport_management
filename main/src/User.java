import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

public class User implements Persistable {

    private int uid;
    private String username;
    private String password;
    private String userType;

    public User(int uid, String username, String password, String userType) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(String username, String password, String userType) {
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
                statement.setString(3, this.userType);
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
                statement.setString(3, this.userType);
                statement.setInt(4, this.uid);
                statement.execute();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean isInDatabase(Connection connection) {
        if(uid == -1) {
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
        if(args.length > 0) {
            String sql = "SELECT username FROM users WHERE username = ?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, (String)args[0]);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    if (rs.getString("username").equals((String)args[0])) {
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
                    this.userType = rs.getString("user_type");
                }
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Vector<User> getUserVector(Connection connection) {
        return new Vector<>(getUserList(connection));
    }

    public static ArrayList<User> getUserList(Connection connection) {
        ArrayList<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int uid = rs.getInt("uid");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String userType = rs.getString("user_type");
                list.add(new User(uid, username, password, userType));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
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

    public String getUserType() {
        return userType;
    }
}
