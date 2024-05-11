package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist_movie_entity")
public class WatchlistMovieEntity {
    @DatabaseField(columnName = "id", generatedId = true)
    private long id;
    @DatabaseField(columnName = "api_id")
    private String apiId;

    //A default constructor is needed so that the object can be returned from a DB query or so that a table can be created
    public WatchlistMovieEntity(){}
}
