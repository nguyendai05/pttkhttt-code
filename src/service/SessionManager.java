package service;

import model.UserAccount;

import java.util.Optional;


public class SessionManager {
    private UserAccount currentUser;



    public void login(UserAccount user) {
        this.currentUser = user;
    }



    public void logout() {
        this.currentUser = null;
    }

    public Optional<UserAccount> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
