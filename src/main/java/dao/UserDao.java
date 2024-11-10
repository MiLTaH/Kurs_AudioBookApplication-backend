package dao;

import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entities.
 */
public class UserDao {
    /**
     * A connection to the database. This connection is established in the
     * constructor and is used for all database interactions within the DAO.
     */
    private Connection connection;

    /**
     * Constructor for UserDao.
     * Establishes a connection to the database.
     */
    public UserDao() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:8079/AudioBook",
                    "postgres",
                    "KakTakNuKakTak");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all users from the database.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt =
                     connection.prepareStatement("SELECT * FROM Users")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("login"),
                        rs.getInt("idUser"),
                        rs.getString("password"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID, or null if no such user exists.
     */
    public User getUser(final int id) {
        User user = null;
        try (PreparedStatement stmt =
                     connection.prepareStatement(
                             "SELECT * FROM Users WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("login"),
                        rs.getInt("idUser"),
                        rs.getString("password"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user to create.
     *
     * Returns the created user object with the assigned ID.
     * @return The created user object with the assigned ID.
     */
    public User createUser(final User user) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Users (login, password, email) VALUES (?, ?, ?)");
             PreparedStatement selectId = connection.
                     prepareStatement("SELECT LAST_INSERT_ROWID()")
        ) {
            final int nameIndex = 1;
            final int passwordIndex = 2;
            final int emailIndex = 3;
            stmt.setString(nameIndex, user.getLogin());
            stmt.setString(passwordIndex, user.getPassword());
            stmt.setString(emailIndex, user.getEmail());
            stmt.executeUpdate();
            try (ResultSet rs = selectId.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    user.setIdUser(userId);
                } else {
                    throw new SQLException("Пользователь не создан");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param updateUser The updated user data.
     */
    public void updateUser(final User updateUser) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE Users SET login = ?, "
                        + "password = ?, email = ? WHERE id = ?")) {
            final int nameIndex = 1;
            final int passwordIndex = 2;
            final int emailIndex = 3;
            final int idIndex = 4;
            stmt.setString(nameIndex, updateUser.getLogin());
            stmt.setString(passwordIndex, updateUser.getPassword());
            stmt.setString(emailIndex, updateUser.getEmail());
            stmt.setInt(idIndex, updateUser.getIdUser());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(final int id) {
        try (PreparedStatement stmt =
                     connection.prepareStatement(
                             "DELETE FROM Users WHERE id = ?"
                     )) {
            final int idIndex = 1;
            stmt.setInt(idIndex, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
