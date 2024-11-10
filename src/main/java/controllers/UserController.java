package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.UserService;

/**
 * Servlet to handle user-related requests.
 */
@WebServlet(urlPatterns = {"/users/*"})
public class UserController extends HttpServlet {
    /**
     * Provides access to user-related services for managing user data.
     */
    private final UserService userService = new UserService();

    /**
     * Logger for the UserController class.
     */
    private static final Logger LOG = LogManager.
            getLogger(UserController.class);

    /**
     * Handles GET requests for users.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response)
            throws IOException {
        String path = request.getPathInfo();
        if (path == null || path.isEmpty()) {
            LOG.info("Request to get all users.");
            List<User> users = userService.getAllUsers();
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(users));
        } else if (path.startsWith("/")) {
            try {
                int userId = Integer.parseInt(path.substring(1));
                LOG.info("Request to get user with ID: {}", userId);
                User user = userService.getUserById(userId);
                LOG.info("Requested user: {}", user);
                response.getWriter().write(new Gson().toJson(user));
                if (user == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    LOG.error("User not found.");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                LOG.error("Invalid ID format: {}", e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("Invalid request format: {}", path);
        }
    }

    /**
     * Handles POST requests for creating new users.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws IOException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonBody = sb.toString();
            User newUser = new Gson().fromJson(jsonBody, User.class);
            LOG.info("Request to create user: {}", newUser);
            User createdUser = userService.createUser(newUser);
            LOG.info("User created: {}", createdUser);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("Error creating user: Error reading request body.");
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("Error creating user: Invalid JSON format.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("Error creating user.");
        }
    }

    /**
     * Handles PUT requests for updating existing users.
     *
     * @param req   The HTTP request.
     * @param resp The HTTP response.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPut(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.error("Missing user ID in request.");
            return;
        }

        try {
            int userId = Integer.parseInt(path.substring(1));
            LOG.info("Request to update user with ID: {}", userId);
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonBody = sb.toString();
            User updatedUser = new Gson().fromJson(jsonBody, User.class);

            User existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                LOG.error("User not found.");
                return;
            }
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setLogin(updatedUser.getLogin());
            existingUser.setEmail(updatedUser.getEmail());
            userService.updateUser(existingUser);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(new Gson().toJson(existingUser));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid user ID format.");
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid JSON format.");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An unexpected error occurred.");
        }
    }

    /**
     * Handles DELETE requests for deleting users.
     *
     * @param req   The HTTP request.
     * @param resp The HTTP response.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doDelete(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws IOException {
        String path = req.getPathInfo();
        if (path.startsWith("/")) {
            try {
                int userId = Integer.parseInt(path.substring(1));
                userService.deleteUser(userId);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Wrong ID format: " + path);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}