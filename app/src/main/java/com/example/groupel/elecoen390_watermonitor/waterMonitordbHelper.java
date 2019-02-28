package com.example.groupel.elecoen390_watermonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class waterMonitordbHelper extends SQLiteOpenHelper {
    //TODO: SQLite table definitions for turbidity and user
    private static final String LOG = "waterMonitordbHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "waterMonitorDatabase";
    private static final String TABLE_SPECTRO = "spectrophotometry_samples";
    private static final String TABLE_SPECTROPOINTS = "spectrophotometry_points";//contains measurements ID

    //Shared column names
    private static final String KEY_ID = "id";

    //spectrophotometry samples table specific column names
    private static final String KEY_DATE = "date";

    //spectrophotometry points table specific column names
    private static final String KEY_MEASURE_ID = "measureId";
    private static final String KEY_WAVE = "wavelength";
    private static final String KEY_INTENSITY = "intensity";

    //spectrophotometry samples table contains: ID, date
    private static final String CREATE_TABLE_SPECTRO = "CREATE TABLE "
            + TABLE_SPECTRO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE
            + " TEXT" + ")";

    //spectrophotometry points contains: ID, sampleID, wavelength, intensity
    private static final String CREATE_TABLE_SPECTROPOINTS = "CREATE TABLE "
            + TABLE_SPECTROPOINTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MEASURE_ID
            + " INTEGER,"+ KEY_WAVE + " INTEGER," + KEY_INTENSITY + " INTEGER" + ")";

    private Context context;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd; HH:mm:ss");

    public waterMonitordbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SPECTRO);
        db.execSQL(CREATE_TABLE_SPECTROPOINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECTRO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECTROPOINTS);
        onCreate(db);
    }
    //TODO: Create, update, delete, get, getall for each table. Missing turbidity and user tables.

    /*
     * Methods to:
     *       create,
     *       get by id,
     *       getall,
     *       delete
     *           spectro measurement(individual sample) rows from spectro measurement table
     * */
    public long createSpectroMeasure(spectroMeasure new_measure){
        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, dateFormat.format(new_measure.getDate()));

        try {
            //return id
            id = db.insertOrThrow(TABLE_SPECTRO, null, values);
        }
        catch (SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to create spectrophotometry sample row.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }

    public spectroMeasure getSpectroMeasure(long measure_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SPECTRO + " WHERE "
                + KEY_ID + " = " + measure_id;

        Log.e(LOG, selectQuery);

        Cursor c = null;
        spectroMeasure sample = new spectroMeasure();
        try {
            c = db.query(TABLE_SPECTRO, null, KEY_ID + "=?",
                    new String[] { String.valueOf(measure_id) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    sample.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                    try {
                        sample.setDate(new java.sql.Date( dateFormat.parse(c.getString(c.getColumnIndex(KEY_DATE))).getDate() ));
                    }
                    catch(ParseException e){
                        Log.d(TAG, "Date Exception " + e);
                        Toast.makeText(context, "Failed to parse date \n Exception" + e, Toast.LENGTH_LONG).show();
                    }
                    return sample;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get spectrophotometry measurement row.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            if (c != null)
                c.close();
        }
        return sample;
    }

    public ArrayList<spectroMeasure> getAllSpectroMeasure(){
        ArrayList<spectroMeasure> allSamples = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SPECTRO, null, null, null,
                null,null, null);

        try {
            //if not empty
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        spectroMeasure sample = new spectroMeasure();
                        sample.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                        try {
                            sample.setDate(new java.sql.Date( dateFormat.parse(c.getString(c.getColumnIndex(KEY_DATE))).getDate() ));
                        }
                        catch(ParseException e){
                            Log.d(TAG, "Date Exception " + e);
                            Toast.makeText(context, "Failed to parse date \n Exception" + e, Toast.LENGTH_LONG).show();
                        }
                        allSamples.add(sample);
                    } while (c.moveToNext());
                }
                return allSamples;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all spectrophotometry measurement rows.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allSamples;
    }

    public void deleteSpectroMeasure(long measureID){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete all points with this samples' ID
        ArrayList<spectroPoint> allSpectroPoints = getAllSpectroPointsBySampleID(measureID);
        for (spectroPoint points : allSpectroPoints) {
            deleteSpectroPoint(points.getID());
        }
        //TODO: This may be bugged?
        db.delete(TABLE_SPECTRO, KEY_ID + "=?", new String[] { String.valueOf(measureID)});
        db.close();
    }

    /*
     * Methods to:
     *       create,
     *       get,
     *       getall,
     *       getall by measurement ID,
     *       delete
     *           spectro point rows from spectro point table
     * */
    public long createSpectroPoint(spectroPoint new_point){
        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        ContentValues values = new ContentValues();
        values.put(KEY_MEASURE_ID, new_point.getMeasurementID());
        values.put(KEY_WAVE, new_point.getWavelength());
        values.put(KEY_INTENSITY, new_point.getIntensity());

        try {
            //return id
            id = db.insertOrThrow(TABLE_SPECTROPOINTS, null, values);
        }
        catch (SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to create spectro point row.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }

    public spectroPoint getspectroPoint(long pointID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        spectroPoint point = new spectroPoint();
        try {
            c = db.query(TABLE_SPECTROPOINTS, null, KEY_ID + "=?",
                    new String[] { String.valueOf(pointID) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    point.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                    point.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                    point.setWavelength(c.getInt(c.getColumnIndex(KEY_WAVE)));
                    point.setIntensity(c.getInt(c.getColumnIndex(KEY_INTENSITY)));
                    return point;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get spectro point row.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            if (c != null)
                c.close();
        }
        return point;
    }

    public ArrayList<spectroPoint> getAllSpectroPoints(){
        ArrayList<spectroPoint> allPoints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_SPECTROPOINTS, null, null, null,
                    null,null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        spectroPoint point = new spectroPoint();
                        point.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                        point.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        point.setWavelength(c.getInt(c.getColumnIndex(KEY_WAVE)));
                        point.setIntensity(c.getInt(c.getColumnIndex(KEY_INTENSITY)));
                        allPoints.add(point);
                    } while (c.moveToNext());
                }
                return allPoints;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all spectro point rows.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allPoints;
    }

    public ArrayList<spectroPoint> getAllSpectroPointsBySampleID(long measureID){
        ArrayList<spectroPoint> allPoints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_SPECTROPOINTS, null, KEY_MEASURE_ID + "=?",
                    new String[] { String.valueOf(measureID) }, null, null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        spectroPoint point = new spectroPoint();
                        point.setID(c.getInt(c.getColumnIndex(KEY_ID)));
                        point.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        point.setWavelength(c.getInt(c.getColumnIndex(KEY_WAVE)));
                        point.setIntensity(c.getInt(c.getColumnIndex(KEY_INTENSITY)));
                        allPoints.add(point);
                    } while (c.moveToNext());
                }
                return allPoints;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all spectro point rows.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allPoints;
    }

    public void deleteSpectroPoint (long pointID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SPECTROPOINTS, KEY_ID + "=?", new String[] { String.valueOf(pointID) });
        db.close();
    }
}
