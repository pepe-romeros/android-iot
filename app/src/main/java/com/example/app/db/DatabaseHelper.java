package com.example.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.db.DatabaseContract.PersonEntry;

/**
 * Custom implementation of the {@link SQLiteOpenHelper} Android class.
 *
 * This helper will create a database with the name Device.db.
 * In that database file it will create at this point in time one table mapped in the {@link DatabaseContract.PersonEntry} class.
 *
 * To add more tables, in addition to creating the respective Entry classes on the {@link DatabaseContract} class,
 * it is need to create the proper SQL DDL statements as shown with String variables SQL_CREATE_ENTRIES and SQL_DELETE_ENTRIES.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Current database version number
    private static final String DATABASE_NAME = "Device.db"; // Database file name i.e. name to search for in 'Device File Explorer'

    /**
     * Default constructor for this class.
     * Receives an instance of the application context to create the database file.
     *
     * Calls it's super constructor, passing the context, database name, an optional factory and the current version number.
     *
     * @param context Application's {@link Context} instance
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called the very first time the Helper is instantiated.
     * Will execute the SQL_DELETE_ENTRIES sql code.
     *
     * @param db {@link SQLiteDatabase} instance to perform sql statements
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the {@value DATABASE_VERSION} is incremented, e.g. from 1 to 2.
     * This provides the option to perform a migration, from existing records to the new schema.
     *
     * Database versions should only change when the table schema is altered or new tables are added.
     *
     * DANGER! Current migration strategy is to drop all existing tables along with their records.
     * Upgrading the database as is will result in all records being deleted.
     *
     * @param db {@link SQLiteDatabase} instance to perform sql statements
     * @param oldVersion Version number we are upgrading from.
     * @param newVersion Version number we are upgrading to.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * This provides the option to rollback to a previous database version schema.
     *
     * Database versions should only change when the table schema is altered or new tables are added.
     *
     * DANGER! Current migration strategy is to drop all existing tables along with their records.
     * Downgrading the database as is will result in all records being deleted.
     *
     * @param db {@link SQLiteDatabase} instance to perform sql statements
     * @param oldVersion Version number we are downgrading to.
     * @param newVersion Version number we are downgrading from.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // Standard SQL CREATE statement to create the Person SQLite table.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PersonEntry.TABLE_NAME + " (" +
                    PersonEntry._ID + " INTEGER PRIMARY KEY," +
                    PersonEntry.COLUMN_NAME_FIRST + " TEXT," +
                    PersonEntry.COLUMN_NAME_LAST + " TEXT)";

    // Standard SQL DELETE statement to delete the Person SQLite table.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PersonEntry.TABLE_NAME;
}
