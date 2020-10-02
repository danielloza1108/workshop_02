package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";
    private static final String REMOVE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    public User create(User user) {

        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int id) {
        try (Connection conn = DBUtil.connect()) {
            User user = new User();
            PreparedStatement statement =
                    conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setEmail(rs.getString(2));
                user.setUserName(rs.getString(3));
                user.setPassword(rs.getString(4));
            }
            if (user.getId() == 0) {
                return null;
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(REMOVE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll(){
        List<List<String>> result = new ArrayList<>();
        List<User> list = new ArrayList<>();
        try (Connection conn = DBUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(FIND_ALL_QUERY);
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                List<String> row = new ArrayList<>();
                int colNum = rs.getMetaData().getColumnCount();
                for (int i = 0; i < colNum; i++) {
                    String col = rs.getString(i+1);
                    row.add(col);
                }
                result.add(row);
            }

            for (List<String> strings : result) {
                User user1 = new User();
                user1.setId(Integer.parseInt(strings.get(0)));
                user1.setEmail(strings.get(1));
                user1.setUserName(strings.get(2));
                user1.setPassword(strings.get(3));
                list.add(user1);
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printAll(List<User> list){
        for (User user1 : list) {
            System.out.print(user1.getId() + " ");
            System.out.print(user1.getEmail() + " ");
            System.out.print(user1.getUserName() + " ");
            System.out.println(user1.getPassword());
        }
    }
}

