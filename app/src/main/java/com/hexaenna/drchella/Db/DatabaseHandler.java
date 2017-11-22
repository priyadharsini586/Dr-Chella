package com.hexaenna.drchella.Db;

/**
 * Created by Priya Mohan on 04-11-2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CHELLADB";

    // Contacts table name
    private static final String TABLE_LANGUAGE = "language";
    private static final String TABLE_USER_DETAILS= "user_details";

    // Contacts Table Columns names
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_ID = "id";

    private static final String USER_NAME = "user_name";
    private static final String USER_MBL = "mbl_number";
    private static final String USER_ID = "user_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LANGUAGE + "("
                + KEY_ID + " TEXT ,"
                + KEY_LANGUAGE + " TEXT " + ")";
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_DETAILS + "("
                + USER_ID + " TEXT ,"
                + USER_NAME + " TEXT ,"
                + USER_MBL + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */


   public void addLanguage(String lang,String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LANGUAGE, lang);
        values.put(KEY_ID,id);// Contact Name

        // Inserting Row
        db.insert(TABLE_LANGUAGE, null, values);
        db.close(); // Closing database connection
    }

    public void addUser(String userNmae,String mbl,String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, userNmae);
        values.put(USER_ID,id);
        values.put(USER_MBL,mbl);

        // Inserting Row
        db.insert(TABLE_USER_DETAILS, null, values);
        db.close(); // Closing database connection
    }


   public  String getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LANGUAGE, new String[] { KEY_ID,
                        KEY_LANGUAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        String language = cursor.getString(1);
        // return contact
        return language;
    }

    public String[] getUserName(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_DETAILS, new String[] { USER_NAME,
                        USER_MBL }, USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        String[] userDetails = {cursor.getString(0),cursor.getString(1)};

        // return contact
        return userDetails;
    }


    // Getting All Contacts
    /*public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }*/

    // Updating single contact
    public int updateContact(String id,String language) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_LANGUAGE, language);

        // updating row
        return db.update(TABLE_LANGUAGE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // Deleting single contact
    public boolean checkForTables(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +TABLE_USER_DETAILS, null);

        if(cursor != null){

            cursor.moveToFirst();

            int count = cursor.getInt(0);

            if(count > 0){
                return true;
            }

            cursor.close();
        }

        return false;
    }
}
