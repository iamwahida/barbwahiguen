package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import java.sql.SQLException;
import java.util.List;
import com.j256.ormlite.support.ConnectionSource;


public class WatchlistRepository {

    private Dao<WatchlistMovieEntity, String> watchlistDao;

    public WatchlistRepository(ConnectionSource connectionSource) throws SQLException {
        watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
    }

    public List<WatchlistMovieEntity> getAllWatchlistMovies() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public void addMovieToWatchlist(String apiId) throws SQLException {
        WatchlistMovieEntity watchlistMovie = new WatchlistMovieEntity();
        watchlistMovie.setApiId(apiId);
        watchlistDao.createIfNotExists(watchlistMovie);
    }

    public void deleteMovieFromWatchlist(String apiId) throws SQLException {
        DeleteBuilder<WatchlistMovieEntity, String> deleteBuilder = watchlistDao.deleteBuilder();
        deleteBuilder.where().eq("apiId", apiId);
        deleteBuilder.delete();
    }
    public void clearWatchlist() throws SQLException {
        watchlistDao.deleteBuilder().delete();
    }


}
