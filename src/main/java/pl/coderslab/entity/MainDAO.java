package pl.coderslab.entity;

import java.util.ArrayList;
import java.util.List;

public class MainDAO {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        List<User> list = new ArrayList<>();
        list = userDAO.findAll();
        userDAO.printAll(list);

    }
}
