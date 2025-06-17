package util;

public class LoginSession {
    private static String currentUser;

    public static void login(String username) {
        currentUser = username;
    }

    public static void logout() {
        currentUser = null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
