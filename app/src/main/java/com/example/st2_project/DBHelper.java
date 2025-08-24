package com.example.st2_project;

//import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String databaseName = "movieDatabase";

    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY, username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL UNIQUE, gender TEXT, country TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

    public User getUser(String username, String password) {
        db = getReadableDatabase();

        //String[] rowDetails = {"id", "username", "password", "gender", "country"};
        String whereClause = "username = ? AND password = ?";
        String[] whereArgs = new String[] {username, password};

        Cursor cursor = db.query("user", null, whereClause, whereArgs, null, null, null);

        User user = null;

        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("country"))
            );
        }

        cursor.close();
        db.close();

        return user;
    }

    /*public void createNew(String name) {
        ContentValues row = new ContentValues();
        row.put("name", name);
        movieDatabase = getWritableDatabase();
        // NullColumnHack here means that if the ContentValues is empty, the database will insert a row with NULL values.
        movieDatabase.insert("movie", null, row);
        movieDatabase.close();
    }

    public Cursor fetchAll() {
        movieDatabase = getReadableDatabase();
        String[] rowDetails = {"name", "id"};
        Cursor cursor = movieDatabase.query("movie", rowDetails, null,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        movieDatabase.close();
        return cursor;
    }

    public void updateOne(int movieId, String newMovieName) {
        movieDatabase = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name", newMovieName);
        movieDatabase.update("movie", row, "id='" + movieId + "'", null);
        movieDatabase.close();
    }

    public void deleteOne(int movieId) {
        movieDatabase = getWritableDatabase();
        movieDatabase.delete("movie", "id='" + movieId + "'", null);
        movieDatabase.close();
    }*/
}
