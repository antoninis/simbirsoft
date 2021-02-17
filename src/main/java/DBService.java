import java.sql.*;
import java.util.Map;

public class DBService {
    private static Connection connection=null;

    public static Connection getConnection() {
        String driver = "org.hsqldb.jdbc.JDBCDriver";
        String url = "jdbc:hsqldb:file:database/a";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void createDB() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS statistic ( word VARCHAR(30) PRIMARY KEY, count INT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearDB() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM statistic");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void saveToDb(Map<String, Integer> wordMap) {
        getConnection();
        createDB();
        clearDB();
        for (Map.Entry<String, Integer> pair : wordMap.entrySet()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO statistic ( word, count) VALUES (?, ?)")) {
                String word = pair.getKey();
                int value = pair.getValue();
                preparedStatement.setString(1, word);
                preparedStatement.setInt(2, value);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeDB();
    }

    public static void closeDB() {
        try {
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
