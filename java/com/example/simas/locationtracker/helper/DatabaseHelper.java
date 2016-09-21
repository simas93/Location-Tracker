package com.example.simas.locationtracker.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simas.locationtracker.model.Location;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "Location.db";
    private static final String TABLE_NAME = "Location_List";
    private static final int VERSION = 1;

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String TIME = "time";
    private static final String LOCATION_ADDRESS = "location_address";

    private static final String DROP_QUERY = "DROP TABLE IF EXISTS " +TABLE_NAME;

    private static final String GET_LOCATION_QUERY = "SELECT * FROM " +TABLE_NAME;

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            LATITUDE +" DOUBLE not null," +
            LONGITUDE + " DOUBLE not null," +
            TIME +" TEXT not null," +
            LOCATION_ADDRESS + " TEXT not null)";

    private static final String DELETE_OLDEST_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id IN (SELECT id FROM " +
            TABLE_NAME +" ORDER BY id ASC LIMIT 1)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_QUERY);
            onCreate(sqLiteDatabase);
    }

    public void addLocation (Location location) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LATITUDE, location.getLatitude());
        contentValues.put(LONGITUDE, location.getLongitude());
        contentValues.put(TIME, location.getTime());
        contentValues.put(LOCATION_ADDRESS, location.getLocationAddress());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Location> getAllLocation () {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_LOCATION_QUERY, null);
        List<Location> locationList = new ArrayList<>();
        if (cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                do {
                    Location location = new Location();
                    location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
                    location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
                    location.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                    location.setLocationAddress(cursor.getString(cursor.getColumnIndex(LOCATION_ADDRESS)));
                    locationList.add(location);
                } while (cursor.moveToNext());
            }
        }
        return locationList;
    }

    public void deleteLocation (){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(DELETE_OLDEST_QUERY);
    }

    public int getCount () {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(GET_LOCATION_QUERY, null);
        return cursor.getCount();
    }

    private static DatabaseHelper databaseInstance = null;

    public static synchronized DatabaseHelper getInstance (Context context) {
        if(databaseInstance == null){
            databaseInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseInstance;
    }
}
