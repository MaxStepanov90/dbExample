package com.stepanov.db.servlets;

import com.stepanov.db.dao.UsersDao;
import com.stepanov.db.dao.UsersDaoJdbcImpl;
import com.stepanov.db.dao.UsersDaoJdbcTemplateImpl;
import com.stepanov.db.model.User;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@WebServlet("/users")
public class UsersServletWithDao extends HttpServlet {

    private UsersDao usersDao;

    @Override
    public void init() throws ServletException {
        Properties properties = new Properties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            properties.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/classes/db.properties")));
            String dbUrl = properties.getProperty("db.url");
            String dbUserName = properties.getProperty("db.user");
            String dbPassword = properties.getProperty("db.password");
            String dbDriverClassName = properties.getProperty("db.DriverClassName");

            dataSource.setDriverClassName(dbDriverClassName);
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(dbUserName);
            dataSource.setPassword(dbPassword);

            usersDao = new UsersDaoJdbcTemplateImpl(dataSource);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> user = usersDao.find(2);
        int i = 0;




        List<User> users = null;
        if (req.getParameter("firstName") !=null){
            String firstName = req.getParameter("firstName");
            users = usersDao.findAllByFirstName(firstName);
        } else {
            users = usersDao.findAll();
        }

        req.setAttribute("usersFromServer",users);
        req.getServletContext().getRequestDispatcher("/jsp/users.jsp").forward(req,resp);
    }
}
