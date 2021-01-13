package com.example.app.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.app.db.DatabaseHelper;
import com.example.app.db.DatabaseContract.PersonEntry;
import com.example.app.model.Person;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the concrete {@link Repository} for the {@link Person} type.
 *
 * This class implements the default CRUD methods behavior using a {@link DatabaseHelper} instance
 * to perform operations on the apps SQLite database.
 */
public class PersonRepository implements Repository<Person> {

    private static final String TAG = Person.class.getName()+"_TAG"; // Logging TAG
    private static final String[] DEFAULT_PROJECTION = { // Selects all columns of Person's table.
            BaseColumns._ID,
            PersonEntry.COLUMN_NAME_FIRST,
            PersonEntry.COLUMN_NAME_LAST
    };

    private SQLiteDatabase database; // Instance that will hold the Writable/Readable SQlite database.
    private DatabaseHelper dbHelper; // Our SQLiteOpenHelper abstraction to retrieve our database.

    /**
     * Main constructor for this class.
     * Receives the Application's {@link Context} to be able to instantiate the {@link DatabaseHelper}.
     *
     * It uses a {@link WeakReference} to avoid accidental leaking if not released properly by calling
     * this class's {@link PersonRepository#release()} method.
     *
     * @param context the Application's {@link Context} instance.
     */
    public PersonRepository(Context context) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        dbHelper = new DatabaseHelper(contextWeakReference.get());
    }

    @Override
    public long insert(Person person) {
        // Step 1 - Get writable database instance since we are adding a new record.
        database = dbHelper.getWritableDatabase();

        // Step 2 - Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PersonEntry.COLUMN_NAME_FIRST, person.getFirstName());
        values.put(PersonEntry.COLUMN_NAME_LAST, person.getLastName());

        // Step 3 - Insert the new row, returning the primary key value of the new row
        long newRowId = database.insert(PersonEntry.TABLE_NAME, null, values);

        // Step 4 - If the new id results in -1, an error occurred and we print to console
        if(newRowId < 0) {
            Log.e(TAG, "Record for entry " + person + " was not saved in the database");
        }

        // Step 5 - Close the database
        database.close();

        return newRowId;
    }

    @Override
    public Person findById(int id) {
        // Step 1 - Get readable database instance since we are fetching a single record.
        database = dbHelper.getReadableDatabase();

        // Step 2 - Define the "WHERE" clause(s) and the where clause values array
        String where = PersonEntry._ID + "= ?";
        String[] whereArguments = {String.valueOf(id)};

        // Step 3 - Load the query results into a Cursor.
        Cursor cursor = database.query(
                PersonEntry.TABLE_NAME,
                DEFAULT_PROJECTION,
                where,
                whereArguments,
                null,
                null,
                null,
                null
        );

        // Prepare the result object
        Person person = null;

        // Step 4 - If we received a result, map it to the result object
        if(cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex(PersonEntry.COLUMN_NAME_FIRST));
            String lastName = cursor.getString(cursor.getColumnIndex(PersonEntry.COLUMN_NAME_LAST));
            person = new Person(id, firstName, lastName);
        }

        // Step 5 - Close the cursor since its not needed anymore
        cursor.close();

        // Step 6 - Close the database
        database.close();

        return person; // Return the result object
    }

    @Override
    public List<Person> findAll() {
        // Step 1 - Get readable database instance since we are just reading all records.
        database = dbHelper.getReadableDatabase();

        // Prepare the result object
        List<Person> people = new ArrayList<>();

        // Step 2 - Load the query results into a Cursor, since we are fetching all records no clauses are passed.
        Cursor cursor = database.query(
                PersonEntry.TABLE_NAME,
                DEFAULT_PROJECTION,
                null,
                null,
                null,
                null,
                null,
                null);

        // Step 3 - If we received a result, map it to the result list object
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(PersonEntry._ID));
            String firstName = cursor.getString(cursor.getColumnIndex(PersonEntry.COLUMN_NAME_FIRST));
            String lastName = cursor.getString(cursor.getColumnIndex(PersonEntry.COLUMN_NAME_LAST));
            people.add(new Person(id, firstName, lastName));
        }

        // Step 4 - Close the cursor since its not needed anymore
        cursor.close();

        // Step 5 - Close the database
        database.close();

        return people; // Return the result list object
    }

    @Override
    public boolean update(Person person) {
        // Step 1 - Get writable database instance since we are updating an existing record.
        database = dbHelper.getWritableDatabase();

        // Step 2 - Define the "WHERE" clause(s) and the where clause values array
        String where = PersonEntry._ID + "= ?";
        String[] whereArguments = {String.valueOf(person.getId())};

        // Step 3 - Create a new map of values, holding the updated record
        ContentValues values = new ContentValues();
        values.put(PersonEntry.COLUMN_NAME_FIRST, person.getFirstName());
        values.put(PersonEntry.COLUMN_NAME_LAST, person.getLastName());

        // Step 4 - Track how many records were affected by the update statement
        int affected = database.update(
                PersonEntry.TABLE_NAME,
                values,
                where,
                whereArguments
        ); // should at least affect +1 rows if 0 or -1 we will return false

        // Step 5 - Close the database
        database.close();

        return affected > 0; // Return the result of update operation
    }

    @Override
    public boolean delete(Person person) {
        // Step 1 - Get writable database instance since we are deleting an existing record.
        database = dbHelper.getWritableDatabase();

        // Step 2 - Define the "WHERE" clause(s) and the where clause values array
        String where = PersonEntry._ID + "= ?";
        String[] whereArguments = {String.valueOf(person.getId())};

        // Step 3 - Track how many records were affected by the update statement
        int affected = database.delete(
                PersonEntry.TABLE_NAME,
                where,
                whereArguments
        ); // should at least affect +1 rows if 0 or -1 we will return false

        // Step 4 - Close the database
        database.close();

        return affected > 0; // Return the result of delete operation
    }

    /**
     * Closes and releases the {@link DatabaseHelper} instance.
     */
    public void release() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}
