package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository(Dao<WatchlistMovieEntity, Long> dao) {
        this.dao = dao;
    }

    public WatchlistRepository() {}

    public boolean addToWatchList(String apiId) {
        try {
            QueryBuilder<WatchlistMovieEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq("api_id", apiId);
            List<WatchlistMovieEntity> existingMovie = queryBuilder.query();

            if (existingMovie.isEmpty()) {
                WatchlistMovieEntity movieEntity = new WatchlistMovieEntity();
                movieEntity.setApiId(apiId);
                dao.create(movieEntity);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
