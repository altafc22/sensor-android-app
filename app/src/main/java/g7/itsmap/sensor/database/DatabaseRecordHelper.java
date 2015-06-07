package g7.itsmap.sensor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import g7.itsmap.sensor.model.Record;

/**
 * Created by benjaminclanet on 05/05/2015.
 */

//http://www.tutorialspoint.com/android/android_sqlite_database.htm

public class DatabaseRecordHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecordsDB.db";
    private static final String TABLE_NAME = "record";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEMPERATURE = "temperature";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseRecordHelper(Context context) {

        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table " + TABLE_NAME + " ";
        sql += "(";
        sql += this.COLUMN_ID + " id integer primary key,";
        sql += this.COLUMN_TEMPERATURE + " temperature real,";
        sql += this.COLUMN_HUMIDITY + " humidity real,";
        sql += this.COLUMN_TIMESTAMP + " timestamp text";
        sql += ")";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public Record insertRecord(double temperature, double humidity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TEMPERATURE, temperature);
        contentValues.put(COLUMN_HUMIDITY, humidity);

        java.util.Date date= new java.util.Date();
        String dateString = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
        contentValues.put(COLUMN_TIMESTAMP, dateString);

        db.insert(TABLE_NAME, null, contentValues);

        return this.getLastRecord();
    }

    private Record getLastRecord() {

        String sql = "select * from " + TABLE_NAME + " ";
        sql += "order by " + COLUMN_ID + " desc ";
        sql += "limit 1";

        ArrayList<Record> records = this.select(sql);
        if(records.size() == 1) {

            return records.get(0);
        }

        return null;
    }

    public ArrayList<Record> getAllRecords() {

        String sql = "select * from " + TABLE_NAME + " ";
        sql += "order by " + COLUMN_ID + " desc";

        return this.select(sql);
    }

    private ArrayList<Record> select(String sql) {

        ArrayList records = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery(sql, null );
        res.moveToFirst();

        while(res.isAfterLast() == false) {

            double temperature = res.getDouble(res.getColumnIndex(COLUMN_TEMPERATURE));
            double humidity = res.getDouble(res.getColumnIndex(COLUMN_HUMIDITY));
            String dateString = res.getString(res.getColumnIndex(COLUMN_TIMESTAMP));

            try {

                Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(dateString);
                Record record = new Record(temperature, humidity, date);
                records.add(record);

            } catch (ParseException e) {

                e.printStackTrace();
            }

            res.moveToNext();
        }

        return records;
    }
}
