package pl.coderslab.entity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    public static String DB_URL = "jdbc:mysql://localhost:3306/";
    public static String DB_USER = "root";
    public static String DB_PASS = "coderslab";

    public static Connection connect() throws SQLException {
        return connect("workshop2");
    }
    public static Connection connect(String database) throws SQLException {
        String connStr = String.format("%s%s?useSSL=false&characterSet=utf8mb4&serverTimezone=UTC",DB_URL,database);
        Connection connection = DriverManager.getConnection(connStr,DB_USER,DB_PASS);
        return connection;
    }

    public static void insert(Connection conn, String query, String... params) {
        try ( PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<List<String>> select(Connection conn, String query, String... params){
        List<List<String>> result = new ArrayList<>();
        try ( PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                List<String> row = new ArrayList<>();
                int colNum = rs.getMetaData().getColumnCount();
                for (int i = 0; i < colNum; i++) {
                    String col = rs.getString(i+1);
                    row.add(col);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static String printData(Connection conn, String query, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            String str = "";
            while (resultSet.next()) {
                for (String param : columnNames) {
                    str = resultSet.getString(param);
                }
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String DELETE_QUERY = "DELETE FROM tableName where id = ?";


    public static void remove(Connection conn, String tableName, int id) {

        try (PreparedStatement statement =
                     conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName));) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
