package main;

import dao.UserDao;
import entity.User;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
       try{
            User firstUser = new User("Mario","mario@gmail.com", "Hasło123");
            UserDao userDao = new UserDao();
           System.out.println(Arrays.toString(userDao.findAll()));

       }catch (Exception exc){
           System.out.println(exc.getMessage());
       }
    }
}
