package dao;

import entity.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.DBUtil;

import java.sql.*;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(userName, email, password) VALUE(?,?,?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET ? = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE * FROM users WHERE id = ?";

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User createUser(User user){
        try (Connection connection = DBUtil.connect("users")) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
              user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
