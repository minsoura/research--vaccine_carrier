package xyz.minsoura.vaccinator.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by min on 2016-03-30.
 */
public class DataBaseAccessor2  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =1;

    private static final String DATABASE_NAME ="vaccineInfoManager";

    private static final String TABLE_NAME="vaccineInfo";

    //TODO: variables in the table
    private static final String KEY_ID ="id";
    private static final String KEY_ID_Number = "idNumber";

    private static final String KEY_bcgCheck="bcgCheck";
    private static final String KEY_bcgDate ="bcgDate";
    private static final String KEY_polioFirstCheck = "polioFirstCheck";
    private static final String KEY_polioFirstDate ="polioFirstDate";
    private static final String KEY_polioSecondCheck="polioSecondCheck";
    private static final String KEY_polioSecondDate="polioSecondDate";
    private static final String KEY_polioThirdCheck="polioThirdCheck";
    private static final String KEY_polioThirdDate="polioThirdDate";
    private static final String KEY_dptFirstCheck="dptFirstCheck";
    private static final String KEY_dptFirstDate="dptFirstDate";
    private static final String KEY_dptSecondCheck="dptSecondCheck";
    private static final String KEY_dptSecondDate="dptSecondDate";
    private static final String KEY_dptThirdCheck="dptThirdCheck";
    private static final String KEY_dptThirdDate="dptThirdDate";
    private static final String KEY_measleCheck="measleCheck";
    private static final String KEY_measleDate="measleDate";
    private static final String KEY_jeCheck="jeCheck";
    private static final String KEY_jeDate="jeDate";
    private static final String KEY_firstRegistration="firstRegistration";


    public DataBaseAccessor2(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_Number + " TEXT,"
                +KEY_bcgCheck+ " TEXT,"
                +KEY_bcgDate + " TEXT,"
                +KEY_polioFirstCheck + " TEXT,"
                +KEY_polioFirstDate + " TEXT,"
                +KEY_polioSecondCheck+ " TEXT,"
                +KEY_polioSecondDate+ " TEXT,"
                +KEY_polioThirdCheck+ " TEXT,"
                +KEY_polioThirdDate+ " TEXT,"
                +KEY_dptFirstCheck+ " TEXT,"
                +KEY_dptFirstDate+ " TEXT,"
                +KEY_dptSecondCheck+ " TEXT,"
                +KEY_dptSecondDate+ " TEXT,"
                +KEY_dptThirdCheck+ " TEXT,"
                +KEY_dptThirdDate+ " TEXT,"
                +KEY_measleCheck+ " TEXT,"
                +KEY_measleDate+ " TEXT,"
                +KEY_jeCheck+ " TEXT,"
                +KEY_jeDate +" TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_Number,contact.getIdNumber());
        values.put(KEY_bcgCheck,contact.getBcgCheck());
        values.put(KEY_bcgDate,contact.getBcgDate());
        values.put(KEY_polioFirstCheck,contact.getPolioFirstCheck());
        values.put(KEY_polioFirstDate,contact.getPolioFirstDate());
        values.put(KEY_polioSecondCheck,contact.getPolioSecondCheck());
        values.put(KEY_polioSecondDate,contact.getPolioSecondDate());
        values.put(KEY_polioThirdCheck,contact.getPolioThirdCheck());
        values.put(KEY_polioThirdDate,contact.getPolioThirdDate());
        values.put(KEY_dptFirstCheck,contact.getDptFirstCheck());
        values.put(KEY_dptFirstDate,contact.getDptFirstDate());
        values.put(KEY_dptSecondCheck,contact.getDptSecondCheck());
        values.put(KEY_dptSecondDate,contact.getDptSecondDate());
        values.put(KEY_dptThirdCheck,contact.getDptThirdCheck());
        values.put(KEY_dptThirdDate,contact.getDptThirdDate());
        values.put(KEY_measleCheck,contact.getMeasleCheck());
        values.put(KEY_measleDate,contact.getMeasleDate());
        values.put(KEY_jeCheck,contact.getJeCheck());
        values.put(KEY_jeDate,contact.getJeDate());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(String idNumber){
        if(!idNumber.equals("")) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID_Number,KEY_bcgCheck,KEY_bcgDate ,KEY_polioFirstCheck ,KEY_polioFirstDate, KEY_polioSecondCheck, KEY_polioSecondDate, KEY_polioThirdCheck, KEY_polioThirdDate, KEY_dptFirstCheck, KEY_dptFirstDate, KEY_dptSecondCheck, KEY_dptSecondDate, KEY_dptThirdCheck, KEY_dptThirdDate, KEY_measleCheck, KEY_measleDate, KEY_jeCheck,KEY_jeDate,}, KEY_ID_Number + "=?", new String[]{idNumber}, null, null, null, null);
            if (cursor.moveToFirst()) {

                Contact contact = new Contact(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18));
                return contact;

            }else{
                return null;
            }

        }else {
            return null;
        }
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
        values.put(KEY_bcgCheck,contact.getBcgCheck());
        values.put(KEY_bcgDate,contact.getBcgDate());
        values.put(KEY_polioFirstCheck,contact.getPolioFirstCheck());
        values.put(KEY_polioFirstDate,contact.getPolioFirstDate());
        values.put(KEY_polioSecondCheck,contact.getPolioSecondCheck());
        values.put(KEY_polioSecondDate,contact.getPolioSecondDate());
        values.put(KEY_polioThirdCheck,contact.getPolioThirdCheck());
        values.put(KEY_polioThirdDate,contact.getPolioThirdDate());
        values.put(KEY_dptFirstCheck,contact.getDptFirstCheck());
        values.put(KEY_dptFirstDate,contact.getDptFirstDate());
        values.put(KEY_dptSecondCheck,contact.getDptSecondCheck());
        values.put(KEY_dptSecondDate,contact.getDptSecondDate());
        values.put(KEY_dptThirdCheck,contact.getDptThirdCheck());
        values.put(KEY_dptThirdDate,contact.getDptThirdDate());
        values.put(KEY_measleCheck,contact.getMeasleCheck());
        values.put(KEY_measleDate,contact.getMeasleDate());
        values.put(KEY_jeCheck,contact.getJeCheck());
        values.put(KEY_jeDate,contact.getJeDate());
        db.insert(TABLE_NAME, null, values);

        return db.update(TABLE_NAME, values, KEY_ID_Number + "=?", new String[]{contact.getIdNumber()});
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID_Number +"=?",new String[]{String.valueOf(contact.getId())});
        db.close();
    }

}
