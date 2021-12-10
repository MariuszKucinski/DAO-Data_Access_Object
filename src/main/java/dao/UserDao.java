package dao;

import entity.User;
import org.mindrot.jbcrypt.BCrypt;
import utils.DBUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(userName, email, password) VALUE(?,?,?)";
    private static final String READ_USER_QUERY = "SELECT users.* FROM users WHERE id = ?";
    private static final String READ_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET userName = ?, email = ?, password = ?  WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";

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

    public  User readUser(int id){
        try(Connection connection = DBUtil.connect("users")){
            PreparedStatement preparedStatement = connection.prepareStatement(READ_USER_QUERY);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String[] columnNames = {"id","userName", "email", "password"};
            String[] userTable = new String[4];
            int i = 0;
            while(resultSet.next()){
                for(String columnName : columnNames){
                    userTable[i++] = resultSet.getString(columnName);
                }
            }
            return new User(Integer.parseInt(userTable[0]),userTable[1],userTable[2], userTable[3]);
        }catch (Exception exc){
            System.out.println(exc.getMessage());
            return null;
        }
    }

    public void updateUser(User user){
        try(Connection connection = DBUtil.connect("users")){
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3, hashPassword(user.getPassword()));
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }

    public void deleteUser(int id){
        try(Connection connection = DBUtil.connect("users")){
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }

    public User[] findAll(){
        try(Connection connection = DBUtil.connect("users")){
            User[] usersTable = new User[0];
            PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            String[] columnNames = {"id","userName", "email","password"};
            String[] tmpUser = new String[4];
            int i = 0;
             while(resultSet.next()){
                for(String columnName : columnNames){
                    tmpUser[i] = resultSet.getString(columnName);
                    i++;
                }
                usersTable = addToArray(new User(Integer.parseInt(tmpUser[0]), tmpUser[1], tmpUser[2], tmpUser[3]), usersTable);
                i = 0;
            }
             return usersTable;
        }catch (Exception exc){
            System.out.println(exc.getMessage());
            return null;
        }
    }

    public User[] addToArray(User user, User[] users){
        users = Arrays.copyOf(users, users.length+1);
        users[users.length-1] = user;
        return users;
    }

}
