package db;

import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import constant.CommonConstants;

// JDBC - Java Database Connectivity
// This class will be our gateway in accesing our MySQL database
public class MyJDBC {
    // register new user to the database
    // true - register success
    // false - register failed
    public static boolean register(String username, String password){
        // first check if the username already existss in the database
        try{
            if(!checkUser(username)){
                // connect to the database
                Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);
                
                // create insert query
                PreparedStatement insertUser = connection.prepareStatement(
                    "INSERT INTO " + CommonConstants.DB_USERS_TABLE_NAME + " (username, password) VALUES (?, ?)"
                );

                // insert parameters in the insert query
                insertUser.setString(1, username);
                insertUser.setString(2, password);

                // update db with new user
                insertUser.executeUpdate();
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // check if username already exists in the database
    // false - username does not exist
    // true - username exists
    public static boolean checkUser(String username) {
        try{
            Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);
            
            PreparedStatement checkUserExists = connection.prepareStatement(
                "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME + " WHERE username = ?"
            );

            checkUserExists.setString(1, username);
            ResultSet resultSet = checkUserExists.executeQuery();

            // check to see if the result set is empty
            // if it is empty, it means that there was no data row that contains the username
            // (i.e user does not exist)

            if(!resultSet.isBeforeFirst()){
                return false;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    // validate login credentials by checking to see if username/password pair exists in the database
    public static boolean validateLogin(String username, String password){
        try{
            Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);
            
            // create select query
            PreparedStatement validateUser = connection.prepareStatement(
                "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME + " WHERE username = ? AND password = ?"
            );
            validateUser.setString(1, username);
            validateUser.setString(2, password);

            ResultSet resultSet = validateUser.executeQuery();

            if(!resultSet.isBeforeFirst()){
                return false;
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
