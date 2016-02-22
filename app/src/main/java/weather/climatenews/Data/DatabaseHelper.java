package weather.climatenews.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author Ashish 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Weather.db";
    public static final String TABLE_NAME = "weather_table";
    public static final String _ID = "ID";
    public static final String COL_WEATHER_ICON = "ICON";
    public static final String COL_MAX_TEMP = "MAX";
    public static final String COL_MIN_TEMP = "MIN";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_WIND_SPEED = "WIND";
    public static final String COL_PRESSURE = "PRESSURE";
    public static final String COL_HUMIDITY = "HUMIDITY";
    public static final String COL_CLOUD = "CLOUD";
    public static final String COL_DAY = "DAY";

    public static final String CREATE_QUERY = "create table "+ TABLE_NAME + "("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_WEATHER_ICON+" TEXT, "
            +COL_MAX_TEMP+" INTEGER,"
            +COL_MIN_TEMP+" INTEGER, "
            +COL_DESCRIPTION+" TEXT, "
            +COL_WIND_SPEED+" TEXT, "
            +COL_PRESSURE+" TEXT, "
            +COL_HUMIDITY+" TEXT, "
            +COL_CLOUD+" TEXT, "
            +COL_DAY+" TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    //INSERT DATA INTO TABLE
    public boolean insertData(int i,String COL_WEATHER_ICON, String COL_MAX_TEMP, String COL_MIN_TEMP, String COL_DESCRIPTION, String COL_WIND_SPEED, String COL_PRESSURE, String COL_HUMIDITY, String COL_CLOUD, String COL_DAY ){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        //When empty database
        if(res.getCount() < 7) {
            ContentValues values = new ContentValues();
            {
                values.put(this.COL_WEATHER_ICON, COL_WEATHER_ICON);//[i]);
                values.put(this.COL_MAX_TEMP, COL_MAX_TEMP);//[i]);
                values.put(this.COL_MIN_TEMP, COL_MIN_TEMP);//[i]);
                values.put(this.COL_DESCRIPTION, COL_DESCRIPTION);//[i]);
                values.put(this.COL_WIND_SPEED, COL_WIND_SPEED);//[i]);
                values.put(this.COL_PRESSURE, COL_PRESSURE);//[i]);
                values.put(this.COL_HUMIDITY, COL_HUMIDITY);//[i]);
                values.put(this.COL_CLOUD, COL_CLOUD);//[i]);
                values.put(this.COL_DAY, COL_DAY);

                long result = db.insert(TABLE_NAME, null, values);
                if(result== -1){
                    return false;
                }
            }
            return true;
        }

        //When not empty just update
        ContentValues values = new ContentValues();
        {
            values.put(this.COL_WEATHER_ICON, COL_WEATHER_ICON);//[i]);
            values.put(this.COL_MAX_TEMP, COL_MAX_TEMP);//[i]);
            values.put(this.COL_MIN_TEMP, COL_MIN_TEMP);//[i]);
            values.put(this.COL_DESCRIPTION, COL_DESCRIPTION);//[i]);
            values.put(this.COL_WIND_SPEED, COL_WIND_SPEED);//[i]);
            values.put(this.COL_PRESSURE, COL_PRESSURE);//[i]);
            values.put(this.COL_HUMIDITY, COL_HUMIDITY);//[i]);
            values.put(this.COL_CLOUD, COL_CLOUD);//[i]);
            values.put(this.COL_DAY, COL_DAY);
            db.update(TABLE_NAME, values, "ID = ?", new String[]{"" + (i + 1)});
        }
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_NAME, null);
        return result;
    }
}
