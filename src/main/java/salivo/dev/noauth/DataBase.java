package salivo.dev.noauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.slf4j.Logger;
import java.sql.ResultSet;


public class DataBase {
    public static void init(Logger logger) {
        logger.info("DB initialization");
        // З'єднання з базою даних (H2)
        Connection connection = null;
        try {
            // Завантаження драйвера SQLite JDBC
            Class.forName("org.sqlite.JDBC");

            // З'єднання з базою даних (якщо база не існує, вона буде автоматично створена)
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, nickname TEXT, discord_id TEXT, last_join TEXT, ip_address TEXT)";
            statement.executeUpdate(createTableSQL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static String getDiscordID(String nickname) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            // SQL-запит для отримання віку за ім'ям
            String query = "SELECT discord_id FROM users WHERE nickname = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nickname);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("discord_id");
            } else {
                return "-1"; // Повертаємо -1, якщо запис не знайдено
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-2"; // Повертаємо -2 в разі виникнення помилки
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getLastJoin(String nickname) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            // SQL-запит для отримання віку за ім'ям
            String query = "SELECT last_join FROM users WHERE nickname = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nickname);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("last_join");
            } else {
                return "-1"; // Повертаємо -1, якщо запис не знайдено
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-2"; // Повертаємо -2 в разі виникнення помилки
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getNickname(String discord_id) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            // SQL-запит для отримання віку за ім'ям
            String query = "SELECT nickname FROM users WHERE discord_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, discord_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nickname");
            } else {
                return "-1"; // Повертаємо -1, якщо запис не знайдено
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-2"; // Повертаємо -2 в разі виникнення помилки
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void addNewUser(String nickname, String discord_id, String last_join, String ip_address){
        String insertQuery = "INSERT INTO users (nickname, discord_id, last_join, ip_address) VALUES (?, ?, ?, ?)";
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, nickname);
            preparedStatement.setString(2, discord_id);
            preparedStatement.setString(3, last_join);
            preparedStatement.setString(4, ip_address);

            // Виконання запиту
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void UpdateUserData(String nickname, String discord_id, String last_join, String ip_address) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            // SQL-запит для оновлення користувача за ім'ям
            String updateQuery = "UPDATE users SET discord_id = ?, last_join = ?, ip_address = ? WHERE name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, discord_id);
            preparedStatement.setString(2, last_join);
            preparedStatement.setString(3, ip_address);
            preparedStatement.setString(4, nickname);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
