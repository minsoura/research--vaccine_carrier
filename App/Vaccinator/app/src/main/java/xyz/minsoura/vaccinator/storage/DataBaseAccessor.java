package xyz.minsoura.vaccinator.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by min on 2016-03-29.
 */
public class DataBaseAccessor extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;

    private static final String DATABASE_NAME ="infoManager";

    private static final String TABLE_NAME="personInfo";

    //TODO: variables in the table
    private static final String KEY_ID ="id";
    private static final String KEY_ID_Number = "idNumber";


    private static final String KEY_posLat="posLat";
    private static final String KEY_posLong="posLong";

    private static final String KEY_firstNAME="firstName";
    private static final String KEY_middleNAME="middleName";
    private static final String KEY_lastName ="lastName";

    private static final String KEY_country ="country";
    private static final String KEY_district ="district";
    private static final String KEY_address1 ="address1";
    private static final String KEY_address2="address2";
    private static final String KEY_postalCode="postalCode";

    private static final String KEY_weight="weight";
    private static final String KEY_height="height";

    public DataBaseAccessor(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_Number + " TEXT,"
                + KEY_posLat + " TEXT,"
                + KEY_posLong + " TEXT,"
                + KEY_firstNAME + " TEXT,"
                + KEY_middleNAME +" TEXT,"
                + KEY_lastName +" TEXT,"
                + KEY_country +" TEXT,"
                +KEY_district +" TEXT,"
                +KEY_address1 +" TEXT,"
                +KEY_address2 +" TEXT,"
                +KEY_postalCode +" TEXT,"
                +KEY_weight +" TEXT,"
                +KEY_height +" TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_Number,contact.getIdNumber());

        values.put(KEY_posLat,contact.getPosLat());
        values.put(KEY_posLong,contact.getPosLong());
        values.put(KEY_firstNAME, contact.getFirstNameM());
        values.put(KEY_middleNAME,contact.getMiddleNameM());
        values.put(KEY_lastName,contact.getLastNameM());
        values.put(KEY_address1,contact.getAddress1M());
        values.put(KEY_address2,contact.getAddress2M());
        values.put(KEY_country,contact.getDialogCountryM());
        values.put(KEY_district,contact.getDialogDistrictM());
        values.put(KEY_postalCode,contact.getPostalCodeM());
        values.put(KEY_weight,contact.getWeightM());
        values.put(KEY_height,contact.getHeightM());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(String idNumber){
        if(!idNumber.equals("")) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID_Number, KEY_posLat,KEY_posLong, KEY_firstNAME, KEY_middleNAME, KEY_lastName, KEY_country, KEY_district, KEY_address1, KEY_address2, KEY_postalCode, KEY_weight, KEY_height}, KEY_ID_Number + "=?", new String[]{idNumber}, null, null, null, null);
            if (cursor.moveToFirst()) {

                    Contact contact = new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),cursor.getString(11),cursor.getString(12));
                    return contact;

            }else{
                return null;
            }

        }else {
            return null;
        }
    }
    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<Contact>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{

                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setIdNumber(cursor.getString(1));
                contact.setPosLat(cursor.getString(2));
                contact.setPosLong(cursor.getString(3));
                contact.setFirstNameM(cursor.getString(4));
                contact.setMiddleNameM(cursor.getString(5));
                contact.setLastNameM(cursor.getString(6));
                contact.setDialogCountryM(cursor.getString(7));
                contact.setDialogDistrictM(cursor.getString(8));
                contact.setAddress1M(cursor.getString(9));
                contact.setAddress2M(cursor.getString(10));
                contact.setPostalCodeM(cursor.getString(11));
                contact.setWeightM(cursor.getString(12));
                contact.setHeightM(cursor.getString(13));

                contactList.add(contact);

            }while(cursor.moveToNext());
        }

        return contactList;
    }

    public int getContactsCount(){
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        return cursor.getCount();

    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_Number,contact.getIdNumber());
        values.put(KEY_posLat,contact.getPosLat());
        values.put(KEY_posLong,contact.getPosLong());
        values.put(KEY_firstNAME, contact.getFirstNameM());
        values.put(KEY_middleNAME,contact.getMiddleNameM());
        values.put(KEY_lastName,contact.getLastNameM());
        values.put(KEY_address1,contact.getAddress1M());
        values.put(KEY_address2,contact.getAddress2M());
        values.put(KEY_country,contact.getDialogCountryM());
        values.put(KEY_district,contact.getDialogDistrictM());
        values.put(KEY_postalCode,contact.getPostalCodeM());
        values.put(KEY_weight,contact.getWeightM());
        values.put(KEY_height,contact.getHeightM());
        //db.insert(TABLE_NAME, null, values);

        return db.update(TABLE_NAME, values, KEY_ID_Number + "=?", new String[]{contact.getIdNumber()});
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID_Number +"=?",new String[]{String.valueOf(contact.getId())});
        db.close();
    }

}