package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist_movie_entity")
public class WatchlistMovieEntity {
    @DatabaseField(columnName = "id", generatedId = true)
    private long id;
    @DatabaseField(columnName = "api_id")
    private String apiId;

    public WatchlistMovieEntity() {}

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
