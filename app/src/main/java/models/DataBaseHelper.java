//package models;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by vinnik on 06.12.2016.
// */
//
//public class DataBaseHelper extends SQLiteOpenHelper {
//
//    // Logcat tag
//    private static final String LOG = "DatabaseHelper";
//
//    // Database Version
//    private static final int DATABASE_VERSION = 1;
//
//    // Database Name
//    private static final String DATABASE_NAME = "feceList";
//
//    // Table Names
//    private static final String TABLE_PERSON = "persons";
//    private static final String TABLE_IMAGE = "images";
//    private static final String TABLE_CATEGORIE = "categories";
//
//    // Common column names
//    private static final String KEY_ID = "id";
//
//    // PERSONS Table - column nmaes
//    private static final String KEY_FIRST_NAME = "firstName";
//    private static final String KEY_LAST_NAME = "lastName";
//    private static final String KEY_SECOND_NAME = "secondName";
//    private static final String KEY_CATEGORY = "category";
//
//    // IMAGES Table - column names
//    private static final String KEY_IMAGE = "image";
//    private static final String KEY_IS_MAIN = "isMain";
//    private static final String KEY_OWNER = "owner";
//
//    // NOTE_CATEGORIES Table - column names
//    // Table Create Statements
//
//    private static final String CREATE_TABLE_PERSON = "CREATE TABLE "
//            + TABLE_PERSON + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME
//            + " TEXT," + KEY_LAST_NAME + " TEXT," + KEY_SECOND_NAME
//            + " TEXT," + KEY_CATEGORY + " TEXT " + ")";
//
//    // Tag table create statement
//    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + TABLE_IMAGE
//            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGE + " TEXT,"
//            + KEY_IS_MAIN + " TEXT," + KEY_OWNER + " TEXT," + ")";
//
//    // todo_tag table create statement
//    private static final String CREATE_TABLE_CATEGORIE = "CREATE TABLE "
//            + TABLE_CATEGORIE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
//            + KEY_CATEGORY + " TEXT," + ")";
//
//    public DataBaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        // creating required tables
//        sqLiteDatabase.execSQL(CREATE_TABLE_PERSON);
//        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE);
//        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORIE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//// on upgrade drop older tables
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PERSON);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_IMAGE);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CATEGORIE);
//
//        // create new tables
//        onCreate(sqLiteDatabase);
//    }
//
//    public long createPerson(Person person, long[] tag_ids) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_FIRST_NAME, person.getFirstName());
//        values.put(KEY_LAST_NAME, person.getLastName());
//        values.put(KEY_SECOND_NAME, person.getSecondName());
//        values.put(KEY_CATEGORY, person.getCategory());
//
//        // insert row
//        long person_id = db.insert(TABLE_PERSON, null, values);
//
//        // assigning tags to todo ??
//
//        return person_id;
//    }
//
//    public Person getPerson(long id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_PERSON + " WHERE "
//                + KEY_ID + " = " + id;
//
//        Log.e(LOG, selectQuery);
//
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c != null)
//            c.moveToFirst();
//
//        Person person = new Person();
//        person.setId(c.getLong(c.getColumnIndex(KEY_ID)));
//        person.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
//        person.setFirstName(c.getString(c.getColumnIndex(KEY_FIRST_NAME)));
//        person.setLastName(c.getString(c.getColumnIndex(KEY_LAST_NAME)));
//        person.setSecondName(c.getString(c.getColumnIndex(KEY_SECOND_NAME)));
//
//        return person;
//    }
//    public List<Person> getAllPersons() {
//        List<Person> persons = new ArrayList<Person>();
//        String selectQuery = "SELECT  * FROM " + TABLE_PERSON;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Person person = new Person();
//                person.setId(c.getLong(c.getColumnIndex(KEY_ID)));
//                person.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
//                person.setFirstName(c.getString(c.getColumnIndex(KEY_FIRST_NAME)));
//                person.setLastName(c.getString(c.getColumnIndex(KEY_LAST_NAME)));
//                person.setSecondName(c.getString(c.getColumnIndex(KEY_SECOND_NAME)));
//
//                // adding to todo list
//                persons.add(person);
//            } while (c.moveToNext());
//        }
//
//        return persons;
//    }
//    public List<Person> getAllPersonsByCategory(String category_name) {
//        List<Person> persons = new ArrayList<Person>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_PERSON +" WHERE "
//                + KEY_CATEGORY + " = " + category_name;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Person person = new Person();
//                person.setId(c.getLong(c.getColumnIndex(KEY_ID)));
//                person.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
//                person.setFirstName(c.getString(c.getColumnIndex(KEY_FIRST_NAME)));
//                person.setLastName(c.getString(c.getColumnIndex(KEY_LAST_NAME)));
//                person.setSecondName(c.getString(c.getColumnIndex(KEY_SECOND_NAME)));
//
//                persons.add(person);
//            } while (c.moveToNext());
//        }
//        return persons;
//    }
//    public int updatePerson(Person person) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_FIRST_NAME, person.getFirstName());
//        values.put(KEY_LAST_NAME, person.getLastName());
//        values.put(KEY_SECOND_NAME, person.getSecondName());
//        values.put(KEY_CATEGORY, person.getCategory());
//
//        // updating row
//        return db.update(TABLE_PERSON, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(person.getId()) });
//    }
//    public void deletePerson(long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_PERSON, KEY_ID + " = ?",
//                new String[] { String.valueOf(id) });
//    }
//}
