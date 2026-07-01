package app;

import view.MainMenuView;

/**
 * OWNER: Team 17
 * FEATURE GROUP: Application bootstrap
 * PURPOSE: Khởi động prototype console MVC, nạp seed data và chạy MainMenuView.
 */
public class Main {
    public static void main(String[] args) {
        AppContext context = new AppContext();
        SeedData.load(context);
        new MainMenuView(context).start();
    }
}