package utils;

import java.sql.*;

public class DBUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/database?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PSWD = "Coderslab123";

    public static Connection connect(String databaseName) throws SQLException {
        Connection dbConnection = DriverManager.getConnection(DB_URL.replace("database", databaseName), DB_USER, DB_PSWD);
        return dbConnection;
    }

    public static void insert(Connection connection, String query, String... params){
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i< params.length; i++){
                preparedStatement.setString(i+1, params[i]);
            }
            preparedStatement.executeUpdate();
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }

    public static void printData(Connection connection, String query, String... columnTables){
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    for(String columnName : columnTables){
                        System.out.println(resultSet.getString(columnName));
                    }
                }
            }catch(Exception exc){
                System.out.println(exc.getMessage());
            }
    }

    public static void printDataWithInfo(Connection connection, String query,String infoIfEmpty, String... columnTables){
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder stringBuilder = new StringBuilder();
            while(resultSet.next()){
                for(String columnName : columnTables){
                    stringBuilder.append(resultSet.getString(columnName));
                }
                if(stringBuilder.toString().equals("")){
                    System.out.println(infoIfEmpty);
                }else{
                   stringBuilder.toString();
                }
            }
        }catch(Exception exc){
            System.out.println(exc.getMessage());
        }
    }


    public static void remove(Connection connection, String query, int id){
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }catch (Exception exc){
            System.out.println(exc.getMessage());
        }
    }
}
