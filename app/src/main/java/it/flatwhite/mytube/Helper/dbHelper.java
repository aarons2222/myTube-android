package it.flatwhite.mytube.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.flatwhite.mytube.Model.dbModel;

/**
 * Created by Aaron on 01/12/2017.
 * flatwhite.it
 *
 *
 *
 * DATABASE HELPER CLASS FOR ALL THINGS DB RELATED
 */

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="mytube";
    private static final int DATABASE_VERSION = 1;

    // TABLE NAMES
    private static final String TUBE_TABLE = "tubeLines";
    private static final String NEWS_TABLE = "newsTable";

    // SQL COMMANDS FOR TABLE CREATION
    private static final String CREATE_TUBE_TABLE = "create table "+TUBE_TABLE +"(lineName TEXT primary key, lineStatus TEXT )";
    private static final String CREATE_NEWS_TABLE = "create table "+NEWS_TABLE +"(title TEXT, desc TEXT, url TEXT)";

    Context context;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute SQL statemnt for table creation
        db.execSQL(CREATE_TUBE_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete table if exist then create new
        db.execSQL("DROP TABLE IF EXISTS " + TUBE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE);

        // Create tables again
        onCreate(db);
    }

    // TUBE TABLE METHODS
    /* Insert into database*/
    public void insertIntoLineTable(String lineName,String lineStatus){
        //Log.i("insert", "before insert");

        //Gets reference to writable db
        SQLiteDatabase db = this.getWritableDatabase();

        //Create the Content Values to add key column
        ContentValues values = new ContentValues();
        values.put("lineName", lineName);
        values.put("lineStatus", lineStatus);

        //insert the data
        db.insert(TUBE_TABLE, null, values);

        //Close db connection.
        db.close();
        Toast.makeText(context, "insert value", Toast.LENGTH_LONG);
    //    Log.i("insert into DB", "After insert");
    }

    ///    NEWS TABLE METHODS
    public void insertIntoNewsTable(String title,String desc, String url){
        //Log.i("insert", "before insert");

        //Gets reference to writable db
        SQLiteDatabase db = this.getWritableDatabase();

        //Create the Content Values to add key column
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("desc", desc);
        values.put("desc", url);

        //insert the data
        db.insert(NEWS_TABLE, null, values);

        //Close db connection.
        db.close();
        Toast.makeText(context, "insert value", Toast.LENGTH_LONG);
    //    Log.i("insert into DB", "After insert");
    }

    /* Retrive  data from database */
    public List<dbModel> getDataFromLineTable(){
        List<dbModel> modelList = new ArrayList<dbModel>();
        String query = "select * from "+TUBE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                dbModel model = new dbModel();
                model.setLineName(cursor.getString(0));
                model.setLineStatus(cursor.getString(1));

                modelList.add(model);
            }while (cursor.moveToNext());
        }

        //Log.i("status data", modelList.toString());
        return modelList;
    }

    /* Retrive  data from database */

    public List<dbModel> getDataFromNewsTable(){
        List<dbModel> modelList = new ArrayList<dbModel>();
        String query = "select * from "+NEWS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                dbModel model = new dbModel();
                model.setTitle(cursor.getString(0));
                model.setDesc(cursor.getString(0));
                model.setUrl(cursor.getString(1));

                modelList.add(model);
            }while (cursor.moveToNext());
        }


       // Log.d("status data", modelList.toString());
        return modelList;
    }


    /* CLEAR TABLE ROWS BEFORE INSERTING INORDER TO PREVENT DUPLICATE DATA*/
    public void clearNewsTable(){
   //     Log.i("insert data", "");

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NEWS_TABLE,null,null);
    }

    public void clearTubeTable(){
//        Log.i("insert data", "");
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TUBE_TABLE,null,null);
    }
}
