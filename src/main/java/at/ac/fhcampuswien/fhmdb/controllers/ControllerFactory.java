package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.controllers.MainController;
import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
