package data_access;

import entities.User;
import entities.CommonUserFactory;
import use_case.signup.SignupDataAccessInterface;
import use_case.edit_profile.EditProfileDataAccessInterface;
import use_case.login.LoginDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * File-based implementation of the DAO for storing user data.
 * This implementation persists users to a CSV file.
 * Implements multiple data access interfaces to serve different use cases.
 */
public class FileUserDataAccessObject implements
        SignupDataAccessInterface,
        EditProfileDataAccessInterface,
        LoginDataAccessInterface {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final String filePath;
    private final CommonUserFactory userFactory;

    /**
     * Creates a new FileUserDataAccessObject that persists to the given file.
     * @param filePath the path to the CSV file for storing users
     */
    public FileUserDataAccessObject(String filePath) {
        this.filePath = filePath;
        this.userFactory = new CommonUserFactory();
        loadUsers();
    }

    /**
     * Loads users from the CSV file.
     */
    private void loadUsers() {
        final File file = new File(filePath);
        if (!file.exists()) {
            return; // No file yet, start with empty storage
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split(",", -1); // -1 to include empty strings
                if (parts.length >= 4) {
                    final String username = parts[0];
                    final String fullName = parts[1];
                    final String email = parts[2];
                    final String password = parts[3];
                    // Bio and profilePicture are optional fields (for backward compatibility)
                    final String bio = parts.length > 4 ? unescapeCsv(parts[4]) : null;
                    final String profilePicture = parts.length > 5 ? unescapeCsv(parts[5]) : null;

                    final User user = userFactory.create(fullName, username, email, password);
                    if (bio != null) {
                        user.setBio(bio);
                    }
                    if (profilePicture != null) {
                        user.setProfilePicture(profilePicture);
                    }
                    usersByUsername.put(username, user);
                    usersByEmail.put(email, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from file: " + e.getMessage());
        }
    }

    /**
     * Saves all users to the CSV file.
     */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : usersByUsername.values()) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s%n",
                    escapeCsv(user.getUsername()),
                    escapeCsv(user.getFullName()),
                    escapeCsv(user.getEmail()),
                    escapeCsv(user.getPassword()),
                    escapeCsv(user.getBio()),
                    escapeCsv(user.getProfilePicture())
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving users to file: " + e.getMessage());
        }
    }

    /**
     * Escapes commas and newlines in CSV values.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", "\\,").replace("\n", "\\n");
    }

    /**
     * Unescapes CSV values (reverses escapeCsv).
     */
    private String unescapeCsv(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.replace("\\,", ",").replace("\\n", "\n");
    }

    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }

    @Override
    public void save(User user) {
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
        saveUsers(); // Persist to file immediately
    }

    /**
     * Gets a user by username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    /**
     * Gets a user by email.
     * @param email the email to search for
     * @return the user with the given email, or null if not found
     */
    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Gets all users.
     * @return a map of all users by username
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(usersByUsername);
    }

    /**
     * Clears all users from storage.
     * Useful for testing.
     */
    public void clear() {
        usersByUsername.clear();
        usersByEmail.clear();
        saveUsers();
    }

    /**
     * Updates a user's profile information.
     * @param currentUsername the current username of the user to update
     * @param newUsername the new username (can be same as current if not changing)
     * @param fullName the new full name
     * @param bio the new bio
     * @param profilePicture the new profile picture URL
     */
    @Override
    public void updateUserProfile(String currentUsername, String newUsername, String fullName, 
                                  String bio, String profilePicture) {
        User user = usersByUsername.get(currentUsername);
        if (user != null) {
            user.setFullName(fullName);
            user.setBio(bio);
            user.setProfilePicture(profilePicture);
            
            // If username is changing, update the maps
            if (!currentUsername.equals(newUsername)) {
                user.setUsername(newUsername);
                usersByUsername.remove(currentUsername);
                usersByUsername.put(newUsername, user);
                // Email map doesn't need updating since email didn't change
            }
            
            saveUsers(); // Persist changes
        }
    }

    /**
     * Updates a user's password.
     * @param username the username of the user
     * @param newPassword the new password
     */
    @Override
    public void updatePassword(String username, String newPassword) {
        User user = usersByUsername.get(username);
        if (user != null) {
            user.setPassword(newPassword);
            saveUsers(); // Persist changes
        }
    }
}
