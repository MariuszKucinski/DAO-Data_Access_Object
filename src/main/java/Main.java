import dao.UserDao;
import entity.User;

public class Main {
    public static void main(String[] args) {
        User firstUser = new User("Mario","mario@gmail.com", "Hasło123");
        UserDao userDao = new UserDao();
        userDao.createUser(firstUser);
    }
}
