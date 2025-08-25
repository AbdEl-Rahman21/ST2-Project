package com.example.st2_project;

import android.content.ContentValues;
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
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public User getUser(String username, String password) {
        db = getReadableDatabase();

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

    public void addUser(User user) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("gender", user.getGender());
        values.put("country", user.getCountry());

        db.insert("user", null, values);

        db.close();
    }

    public void deleteUser(int userId) {
        db = this.getWritableDatabase();

        String whereClause = "id=?";
        String[] whereArgs = new String[] {String.valueOf(userId)};

        db.delete("user", whereClause, whereArgs);

        db.close();
    }

    public void editUser(int userId, User newUser) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", newUser.getUsername());
        values.put("password", newUser.getPassword());
        values.put("country", newUser.getCountry());

        String whereClause = "id=?";
        String[] whereArgs = new String[] {String.valueOf(userId)};

        db.update("user", values, whereClause, whereArgs);

        db.close();
    }
}
