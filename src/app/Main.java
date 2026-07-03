package app;

import view.MainMenuView;


public class Main {
    public static void main(String[] args) {
        AppContext context = new AppContext();
        SeedData.load(context);
        new MainMenuView(context).start();
    }
}
