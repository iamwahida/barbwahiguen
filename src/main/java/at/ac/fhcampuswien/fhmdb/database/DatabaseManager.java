package at.ac.fhcampuswien.fhmdb.database;

//In this class we define the basics to allow the program to connect to the database
//Instead of a server database, we choose an embedded database that runs locally on the machine, saves data
//into files, and upon start brings the data into memory

/*Possible locations of the database (when embedded):
jdbc:h2:[file:][<path>]<databaseName>
jdbc:h2:~/test
jdbc:h2:file:/data/sample
jdbc:h2:file:C:/data/sample (Windows)
 */

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;


public class DatabaseManager {

    public static final String DB_URL = "jdbc:h2:file: ./db/movierepositorydb";
    public static final String username = "user";
    public static final String password = "password";
    public static ConnectionSource connectionSource;

    Dao <MovieEntity, Long> movieDao;
    Dao <WatchlistMovieEntity, Long> watchlistDao;

    //Ensure only one database instance is present at any time
    private static DatabaseManager singleInstance;
    //Connect to the database and create the necessary tables to store single movie data & watchlist
    private DatabaseManager(){
       try {
           createConnectionSource();
           movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
           createTables();
           watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
       } catch (SQLException e) {
            System.out.println(e.getMessage());
       }
    }
    public static DatabaseManager getDatabaseSingleInstance(){
        if(singleInstance == null){
            return singleInstance = new DatabaseManager();
        }
        return singleInstance;
    }

    void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    ConnectionSource getConnectionSource(){ return connectionSource;}

    //<T> use generics to pass any class reference
    //TODO: Write better Exception Handling
    void createTables(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistDao;
    }

    /*
    Dao getMovieDao(){
        return movieDao;
    }
     */
}
