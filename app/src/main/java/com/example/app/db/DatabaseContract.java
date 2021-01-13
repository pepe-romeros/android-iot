package com.example.app.db;

import android.provider.BaseColumns;

/**
 * Abstracts the SQLite tables as Entry classes that implement the BaseColumns interface.
 * This way we standardize the SQLite table names, and provide them with a default ID column.
 *
 * To add more tables to the database add another static Entry class as demonstrated with the PersonEntry class.
 */
public final class DatabaseContract {

    private DatabaseContract() {
        // Private empty constructor to avoid accidental instantiation
    }

    /**
     * This class maps the SQLite table to store {@link com.example.app.model.Person} rows.
     */
    public static class PersonEntry implements BaseColumns {
        public static final String TABLE_NAME = "people";
        public static final String COLUMN_NAME_FIRST= "firstName";
        public static final String COLUMN_NAME_LAST = "lastName";
    }

}
