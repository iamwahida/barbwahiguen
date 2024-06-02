package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.h2.jdbc.JdbcSQLNonTransientConnectionException;

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
    private DatabaseManager() throws SQLException {
        try {
            createConnectionSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (JdbcSQLNonTransientConnectionException e) {
            System.err.println("Database file is already in use: " + e.getMessage());
            closeConnection();
            // Log the exception and proceed without crashing
        } catch (SQLException e) {
            System.err.println("Error initializing the database manager: " + e.getMessage());
            closeConnection();
            // Log the exception and proceed without crashing
        }
    }

    // Public method to get the singleton instance
    public static synchronized DatabaseManager getInstance() throws SQLException {
        if (instance != null) {
            if (!isDatabaseAccessible()) {
                //Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
                throw new SQLException("Database is already in use or cannot be accessed.");
            }
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createConnectionSource() throws SQLException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, USERNAME, PASSWORD);
        } catch (JdbcSQLNonTransientConnectionException e) {
            System.err.println("Database file is already in use: " + e.getMessage());
            throw e;
        }
    }

    private void createTables() throws SQLException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw e;
        }
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    // Ensure connections are closed properly
    public static void closeConnection() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error closing the database connection: " + e.getMessage());
            }
        }
    }


    // Method to check if the database file is open
    public static boolean isDatabaseAccessible() {
        ConnectionSource testConnection = null;
        try {
            testConnection = new JdbcConnectionSource(DB_URL, USERNAME, PASSWORD);
            return true; //Database connection is possible
        } catch (JdbcSQLNonTransientConnectionException e) {
            System.err.println("Database file is already in use: " + e.getMessage());
            return false; //Connection is not possible
        } catch (Exception e) {
            System.err.println("Error accessing the database: " + e.getMessage());
            return false; //Connection is not possible
        } finally { //Close connection either way
            if (testConnection != null) {
                try {
                    testConnection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing test connection: " + e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
