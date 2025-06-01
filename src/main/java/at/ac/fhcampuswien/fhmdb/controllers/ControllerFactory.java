package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import javafx.util.Callback;


public class ControllerFactory implements Callback<Class<?>, Object> {
    @Override
    public Object call(Class<?> aClass) {
        try {
            if (aClass == MainController.class) {
                if (MainController.getInstance() == null) {
                    return aClass.getDeclaredConstructor().newInstance();
                } else {
                    return MainController.getInstance();
                }
            } else if (aClass == MovieListController.class) {
                if (MovieListController.getInstance() == null) {
                    return aClass.getDeclaredConstructor().newInstance();
                } else {
                    return MovieListController.getInstance();
                }
            } else if (aClass == WatchlistController.class) {
                if (WatchlistController.getInstance() == null) {
                    return aClass.getDeclaredConstructor().newInstance();
                } else {
                    return WatchlistController.getInstance();
                }
            } else {
                return aClass.getDeclaredConstructor().newInstance(); // fallback
            }
        // ReflectiveOperationException handles reflection-related instantiation errors
        } catch (ReflectiveOperationException e) {
            System.err.println("Error creating controller: " + aClass.getSimpleName());
            e.printStackTrace();

            // Show user dialog in the UI
            new UserDialog("ERROR", "❌ Please restart the app.").show();

            throw new RuntimeException("Controller creation failed for " + aClass.getSimpleName(), e);
        }

    }
}
