package com.mcindoe.workoutwhiz.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDBSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "workoutwhiz.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WORKOUT = "workout";
    public static final String WORKOUT_ID = "_id";
    public static final String WORKOUT_NAME = "workout_name";
    public static final String WORKOUT_DATE = "date";
    public static final String WORKOUT_FAVORITE = "favorite";

    public static final String TABLE_EXERCISE = "exercise";
    public static final String EXERCISE_ID = "_id";
    public static final String EXERCISE_WORKOUT_ID = "workout_id";
    public static final String EXERCISE_NAME = "exercise_name";
    public static final String EXERCISE_INTENSITY = "intensity";

    public static final String TABLE_REP = "rep";
    public static final String REP_ID = "_id";
    public static final String REP_EXERCISE_ID = "exercise_id";
    public static final String REP_COUNT = "count";

    public static final String WORKOUT_TABLE_CREATE = "create table "
        + TABLE_WORKOUT + "("
        + WORKOUT_ID + " integer primary key autoincrement, "
        + WORKOUT_NAME + " text not null, "
        + WORKOUT_FAVORITE + " integer not null, "
        + WORKOUT_DATE + " text not null);";

    public static final String EXERCISE_TABLE_CREATE = "create table "
        + TABLE_EXERCISE + "("
        + EXERCISE_ID + " integer primary key autoincrement, "
        + EXERCISE_WORKOUT_ID + " integer not null, "
        + EXERCISE_NAME + " text not null, "
        + EXERCISE_INTENSITY + " integer not null, "
        + " FOREIGN KEY (" + EXERCISE_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUT + " (" + WORKOUT_ID + "));";

    public static final String REP_TABLE_CREATE = "create table "
        + TABLE_REP + "("
        + REP_ID + " integer primary key autoincrement, "
        + REP_EXERCISE_ID + " integer not null, "
        + REP_COUNT + " integer not null, "
        + " FOREIGN KEY (" + REP_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + " (" + EXERCISE_ID + "));";

    public WorkoutDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORKOUT_TABLE_CREATE);
        db.execSQL(EXERCISE_TABLE_CREATE);
        db.execSQL(REP_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REP);
        onCreate(db);
    }

}
