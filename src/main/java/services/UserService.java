package services;

import dao.UserDao;
import models.User;

import java.util.List;

/**
 * Provides services for managing user data,
 * delegating operations to the UserDao.
 */
public class UserService {

    /**
     * Data Access Object for user operations.
     */
    private final UserDao userDao;

    /**
     * Constructor that takes a UserDao instance.
     *
     * @param userDataAccessObject The UserDao instance to
     * use for user operations.
     */
    public UserService(final UserDao userDataAccessObject) {
        this.userDao = userDataAccessObject;
    }

    /**
     * Constructor that creates a new UserDao instance.
     */
    public UserService() {
        this.userDao = new UserDao();
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created user object.
     */
    public User createUser(final User user) {
        return userDao.createUser(user);
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID, or null if no such user exists.
     */
    public User getUserById(final int id) {
        return userDao.getUser(id);
    }

    /**
     * Updates an existing user.
     *
     * @param user The updated user data.
     */
    public void updateUser(final User user) {
        userDao.updateUser(user);
    }

    /**
     * Deletes a user.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(final int id) {
        userDao.deleteUser(id);
    }
}