package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:h2:file:./db/movierepositorydb";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static ConnectionSource connectionSource;

    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    // Singleton instance
    private static DatabaseManager instance;

    // Private constructor for singleton
    private DatabaseManager() {
        try {
            createConnectionSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e) {
            System.err.println("Error initializing the database manager: " + e.getMessage());
            closeConnection();
            throw new RuntimeException("Failed to initialize database connections", e);
        }
    }

    // Public method to get the singleton instance
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createConnectionSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DB_URL, USERNAME, PASSWORD);
    }

    private void createTables() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    // Ensure connections are closed properly
    public void closeConnection() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
