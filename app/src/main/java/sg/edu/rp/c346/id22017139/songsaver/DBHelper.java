package sg.edu.rp.c346.id22017139.songsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "songs.db";

    private static final String TABLE_SONG = "song";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SINGER = "singer";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_STARS = "stars";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_SONG + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SINGER + " TEXT,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_STARS + " TEXT)";
        db.execSQL(createTableSql);
        Log.i("info", "Created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        // Create table(s) again
        onCreate(db);
    }

    public void insertSong(String title, String singer, int year, String stars) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SINGER, singer);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_STARS, stars);
        db.insert(TABLE_SONG, null, values);
        db.close();
    }

    public ArrayList<Song> getSongContent() {
        ArrayList<Song> songs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_SINGER, COLUMN_YEAR, COLUMN_STARS};
        Cursor cursor = db.query(TABLE_SONG, columns, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String singer = cursor.getString(2);
                int year = cursor.getInt(3);
                String stars = cursor.getString(4);
                Song song = new Song(id, title, singer, year, stars);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }

    public ArrayList<Song> getFilteredSongs() {
        ArrayList<Song> filteredSongs = new ArrayList<>();
        ArrayList<Song> allSongs = getSongContent();

        // Perform your filtering logic here
        // For example, let's filter songs with a rating of 5 stars
        for (Song song : allSongs) {
            if (song.getStars().equals("5")) {
                filteredSongs.add(song);
            }
        }

        return filteredSongs;
    }

    public ArrayList<Song> getSongsByYear(int year) {
        ArrayList<Song> songsByYear = new ArrayList<>();
        ArrayList<Song> allSongs = getSongContent();

        // Retrieve songs that match the specified year
        for (Song song : allSongs) {
            if (song.getYear() == year) {
                songsByYear.add(song);
            }
        }

        return songsByYear;
    }
}
