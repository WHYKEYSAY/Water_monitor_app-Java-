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
    private static final String LOG = "waterMonitordbHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "waterMonitorDatabase";
    private static final String TABLE_SPECTRO = "spectrophotometry_samples";
    private static final String TABLE_SPECTROPOINTS = "spectrophotometry_points";//contains measurements ID
    private static final String TABLE_USER = "users";
    private static final String TABLE_TURBIDITY = "turbidity";
    private static final String TABLE_CHEMICALS = "chemicals";

    //Shared column names
    private static final String KEY_ID = "id";
    private static final String KEY_MEASURE_ID = "measureId";

    //spectrophotometry samples table and turbidity specific column names
    private static final String KEY_DATE = "date";

    //spectrophotometry points table specific column names
    private static final String KEY_WAVE = "wavelength";
    private static final String KEY_INTENSITY = "intensity";

    //Table and user table column names
    private static final String KEY_NAME = "name";

    //User table specific column names
    private static final String KEY_PASS = "pass";
    private static final String KEY_CONFIG = "config";
    
    //Turbidity table specific column names
    private static final String KEY_TUR = "tur";
    private static final String KEY_TUR_DATE = "tur_date";

    //Chemicals table specific column names
    private static final String KEY_PRES = "presence";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CON = "concentration";

    //spectrophotometry samples table contains: ID, date
    private static final String CREATE_TABLE_SPECTRO = "CREATE TABLE "
            + TABLE_SPECTRO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE
            + " TEXT" + ")";

    //spectrophotometry points contains: ID, measureID, wavelength, intensity
    private static final String CREATE_TABLE_SPECTROPOINTS = "CREATE TABLE "
            + TABLE_SPECTROPOINTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MEASURE_ID
            + " INTEGER,"+ KEY_WAVE + " INTEGER," + KEY_INTENSITY + " INTEGER" + ")";
    
    //User table creation statement (ID,name,pass,config)
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" 
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_PASS + " TEXT NOT NULL,"
            + KEY_CONFIG + " TEXT NOT NULL" + ")";
    
    //Turbidity table creation statement (ID,measureID,tur,date)
    private static final String CREATE_TABLE_TURBIDITY = "CREATE TABLE " + TABLE_TURBIDITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MEASURE_ID + " INTEGER NOT NULL,"
            + KEY_TUR + " INTEGER NOT NULL,"
            + KEY_TUR_DATE + " TEXT" + ")";

    //Chemicals table creation statement (ID,measureID,presence,type,name,concentration)
    private static final String CREATE_TABLE_CHEMICALS = "CREATE TABLE " + TABLE_CHEMICALS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MEASURE_ID + " INTEGER NOT NULL,"
            + KEY_PRES + "BIT NOT NULL,"
            + KEY_TYPE + " INTEGER NOT NULL,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_CON + " INTEGER" + ")"; //integers are more reliable than real numbers

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
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TURBIDITY);
        db.execSQL(CREATE_TABLE_CHEMICALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECTRO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECTROPOINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURBIDITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHEMICALS);
        onCreate(db);
    }

    /******************** Spectrophotometry Measurement Table Methods ***********************
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
                        sample.setDate(dateFormat.parse(c.getString(c.getColumnIndex(KEY_DATE))));
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
                        sample.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        try {
                            sample.setDate(dateFormat.parse(c.getString(c.getColumnIndex(KEY_DATE))));
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
        //delete all turbidity with this sample ID (should be only one but this code can help
        //  correct an error.
        ArrayList<turbidity> allTurbs = getAllTurbidityBySampleID(measureID);
        for (turbidity turb  : allTurbs) {
            deleteTur(turb.getID());
        }
        db.delete(TABLE_SPECTRO, KEY_ID + "=?", new String[] { String.valueOf(measureID)});
        db.close();
    }

    /**************************** Spectrophotometry Point Table Methods ************************
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

    public spectroPoint getSpectroPoint(long pointID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        spectroPoint point = new spectroPoint();
        try {
            c = db.query(TABLE_SPECTROPOINTS, null, KEY_ID + "=?",
                    new String[] { String.valueOf(pointID) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    point.setID(c.getLong(c.getColumnIndex(KEY_ID)));
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
                        point.setID(c.getLong(c.getColumnIndex(KEY_ID)));
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
                        point.setID(c.getLong(c.getColumnIndex(KEY_ID)));
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
    
    /***************************** USER TABLE METHODS *******************************
     * Methods to:
     *       create,
     *       get,
     *       getall
     *       delete
     * */

    public long createUser(user new_user){
        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, new_user.getName());
        values.put(KEY_PASS, new_user.getPass());
        values.put(KEY_CONFIG, new_user.getConfig());

        try {
            //return id
            id = db.insertOrThrow(TABLE_USER, null, values);
        }
        catch (SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to create this user.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }

    public user getUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        user user = new user();
        try {
            c = db.query(TABLE_USER, null, KEY_ID + "=?",
                    new String[] { String.valueOf(user_id) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    user.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                    user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                    user.setPass(c.getString(c.getColumnIndex(KEY_PASS)));
                    user.setConfig(c.getString(c.getColumnIndex(KEY_CONFIG)));
                    return user;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get user row.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            if (c != null)
                c.close();
        }
        return user;
    }

    public ArrayList<user> getAllUsers(){
        ArrayList<user> allUsers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_USER, null, null, null,
                    null,null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        user user = new user();
                        user.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                        user.setPass(c.getString(c.getColumnIndex(KEY_PASS)));
                        user.setConfig(c.getString(c.getColumnIndex(KEY_CONFIG)));
                        allUsers.add(user);
                    } while (c.moveToNext());
                }
                return allUsers;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all users.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allUsers;
    }

    public void deleteUser (long user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + "=?", new String[] { String.valueOf(user_id) });
        db.close();
    }

    /***************************** TURBIDITY TABLE METHODS *******************************
     * Methods to:
     *       create,
     *       get,
     *       getall by measurement ID,
     *       getall
     *       delete
     * */

    public long createTur(turbidity new_tur){
        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        ContentValues values = new ContentValues();
        values.put(KEY_TUR, new_tur.getTurb());
        values.put(KEY_MEASURE_ID, new_tur.getMeasurementID());
        values.put(KEY_TUR_DATE, dateFormat.format(new_tur.getDate()));

        try {
            //return id
            id = db.insertOrThrow(TABLE_TURBIDITY, null, values);
        }
        catch (SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to create this turbidity.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }

    public turbidity getTur(long tur_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        turbidity turbidity = new turbidity();
        try {
            c = db.query(TABLE_TURBIDITY, null, KEY_ID + "=?",
                    new String[] { String.valueOf(tur_id) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    turbidity.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                    turbidity.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                    turbidity.setTurb(c.getInt(c.getColumnIndex(KEY_TUR)));
                    try {
                        turbidity.setDate(dateFormat.parse(c.getString(c.getColumnIndex(KEY_TUR_DATE))));
                    }
                    catch(ParseException e){
                        Log.d(TAG, "Date Exception " + e);
                        Toast.makeText(context, "Failed to parse date \n Exception" + e, Toast.LENGTH_LONG).show();
                    }
                    return turbidity;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get turbidity.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            if (c != null)
                c.close();
        }
        return turbidity;
    }

    /*
    * Description: This methods returns an ArrayList of all turbidity measurements associated
    *       with a measurement ID. Should be only one entry: if more there is an error and the entry
    *       with a date corresponding to the measurement should be chosen.
    * */
    public ArrayList<turbidity> getAllTurbidityBySampleID(long measureID){
        ArrayList<turbidity> allTurbs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_TURBIDITY, null, KEY_MEASURE_ID + "=?",
                    new String[] { String.valueOf(measureID) }, null, null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        turbidity turbidity = new turbidity();
                        turbidity.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        turbidity.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        try {
                            turbidity.setDate(dateFormat.parse(c.getString(c.getColumnIndex(KEY_TUR_DATE))));
                        }
                        catch(ParseException e){
                            Log.d(TAG, "Date Exception " + e);
                            Toast.makeText(context, "Failed to parse date \n Exception" + e, Toast.LENGTH_LONG).show();
                        }
                        allTurbs.add(turbidity);
                    } while (c.moveToNext());
                }
                return allTurbs;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all turbidity point rows.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allTurbs;
    }

    public ArrayList<turbidity> getAllTurbidity(){
        ArrayList<turbidity> allTur = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_TURBIDITY, null, null, null,
                    null,null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        turbidity turbidity = new turbidity();
                        turbidity.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        turbidity.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        turbidity.setTurb(c.getInt(c.getColumnIndex(KEY_TUR)));
                        try {
                            turbidity.setDate(dateFormat.parse(c.getString(c.getColumnIndex(KEY_TUR_DATE))));
                        }
                        catch(ParseException e){
                            Log.d(TAG, "Date Exception " + e);
                            Toast.makeText(context, "Failed to parse date \n Exception" + e, Toast.LENGTH_LONG).show();
                        }

                        allTur.add(turbidity);
                    } while (c.moveToNext());
                }
                return allTur;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all turbidity.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allTur;
    }

    public void deleteTur (long tur_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TURBIDITY, KEY_ID + "=?", new String[] { String.valueOf(tur_id) });
        db.close();
    }

    /***************************** Chemicals Table Methods *******************************
     * Methods to:
     *       create,
     *       get,
     *       getall
     *       delete
     * */
    public long createChem(chemical new_chem){
        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;
        ContentValues values = new ContentValues();
        values.put(KEY_MEASURE_ID, new_chem.getMeasurementID());
        if (new_chem.getPresence() == false)
            values.put(KEY_PRES, 0);
        else
            values.put(KEY_PRES, 1);
        values.put(KEY_TYPE, new_chem.getType());
        values.put(KEY_NAME, new_chem.getName());
        values.put(KEY_CON, (int)(new_chem.getConcentration() * chemical.DECIMAL_SCALE));

        try {
            //return id
            id = db.insertOrThrow(TABLE_CHEMICALS, null, values);
        }
        catch (SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to create chemical.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }

    public chemical getChem(long chem_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        chemical chem = new chemical();
        try {
            c = db.query(TABLE_CHEMICALS, null, KEY_ID + "=?",
                    new String[] { String.valueOf(chem_id) }, null, null, null);

            if (c != null) {// calling a method when cursor = null causes crash
                if (c.moveToFirst()) {
                    chem.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                    chem.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                    if (c.getShort(c.getColumnIndex(KEY_PRES)) == 0)
                        chem.setPresence(false);
                    else
                        chem.setPresence(true);
                    chem.setType(c.getInt(c.getColumnIndex(KEY_TYPE)));
                    chem.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                    chem.setConcentration((float)c.getInt(c.getColumnIndex(KEY_CON))/chemical.DECIMAL_SCALE);
                    return chem;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get chemical.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
            if (c != null)
                c.close();
        }
        return chem;
    }

    public ArrayList<chemical> getAllChemicalBySampleID(long measureID){
        ArrayList<chemical> allChem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_CHEMICALS, null, KEY_MEASURE_ID + "=?",
                    new String[] { String.valueOf(measureID) }, null, null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        chemical chem = new chemical();
                        chem.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        chem.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        if (c.getShort(c.getColumnIndex(KEY_PRES)) == 0)
                            chem.setPresence(false);
                        else
                            chem.setPresence(true);
                        chem.setType(c.getInt(c.getColumnIndex(KEY_TYPE)));
                        chem.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                        chem.setConcentration((float)c.getInt(c.getColumnIndex(KEY_CON))/chemical.DECIMAL_SCALE);
                        allChem.add(chem);
                    } while (c.moveToNext());
                }
                return allChem;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all chemical rows.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allChem;
    }

    public ArrayList<chemical> getAllChemicals(){
        ArrayList<chemical> allChem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        try {
            //if not empty
            c = db.query(TABLE_CHEMICALS, null, null, null,
                    null,null, null);
            if (c != null) {//avoid null pointer crash, returns empty array
                if (c.moveToFirst()) {
                    do {
                        chemical chem = new chemical();
                        chem.setID(c.getLong(c.getColumnIndex(KEY_ID)));
                        chem.setMeasurementID(c.getLong(c.getColumnIndex(KEY_MEASURE_ID)));
                        if (c.getShort(c.getColumnIndex(KEY_PRES)) == 0)
                           chem.setPresence(false);
                        else
                           chem.setPresence(true);
                        chem.setType(c.getInt(c.getColumnIndex(KEY_TYPE)));
                        chem.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                        chem.setConcentration((float)c.getInt(c.getColumnIndex(KEY_CON))/chemical.DECIMAL_SCALE);
                        allChem.add(chem);
                    } while (c.moveToNext());
                }
                return allChem;
            }
        }
        catch(SQLException e){
            Log.d(TAG, "SQL Exception " + e);
            Toast.makeText(context, "Failed to get all chemicals.\n Exception " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
            if (c != null)
                c.close();
        }
        return allChem;
    }

    public void deleteChem (long chem_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHEMICALS, KEY_ID + "=?", new String[] { String.valueOf(chem_id) });
        db.close();
    }
}
