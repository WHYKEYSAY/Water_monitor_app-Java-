package com.example.groupel.elecoen390_watermonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class waterMonitordbHelper extends SQLiteOpenHelper {
    //TODO: SQLite table definitions
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
    //TODO: Create, update, delete, get, getall for each table.
}
