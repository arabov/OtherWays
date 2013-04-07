package com.OtherWays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBcontroller extends SQLiteOpenHelper {

    static final String dbName = "Otherways";
    static final String table = "Places";
    static final String colID = "PlacesID";
    static final String colType = "PlacesType";
    static final String colCity = "PlacesCity";
    static final String colName = "PlaceName";
    static final String colLat = "PlaceLat";
    static final String colLng = "PlaceLng";
    static final String colAddress = "PlaceAddress";
    static final String colPhone = "PlacePhone";
    static final String colDesc = "PlaceDesc";
    static final String colUnder = "PlaceUnder";
    static final String colWork = "PlaceWork";

    public DBcontroller(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + table + " (" +
                colID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                colType + " TEXT, " +
                colCity + " TEXT, " +
                colName + " TEXT, " +
                colLat + " REAL, " +
                colLng + " REAL, " +
                colAddress + " TEXT, " +
                colPhone + " TEXT, " +
                colDesc + " TEXT, " +
                colUnder + " TEXT, " +
                colWork + " TEXT" + ");"
        );

        ContentValues cv = new ContentValues();

        cv.put(colID, 1);
        cv.put(colType, "attraction");
        cv.put(colCity, "Москва");
        cv.put(colName, "Кремль");
        cv.put(colLat, 55.751778);
        cv.put(colLng, 37.617026);
        cv.put(colAddress, "");
        cv.put(colPhone, "");
        cv.put(colDesc, "Главное место страны");
        cv.put(colUnder, "");
        cv.put(colWork, "");
        db.insert(table, colID, cv);

        cv.put(colID, 2);
        cv.put(colType, "attraction");
        cv.put(colCity, "Москва");
        cv.put(colName, "Большой театр");
        cv.put(colLat, 55.759846);
        cv.put(colLng, 37.618640);
        cv.put(colAddress, "Театральная пл., 1");
        cv.put(colPhone, "(495) 455 55 55");
        cv.put(colDesc, "Театр номер один");
        cv.put(colUnder, "Охотный Ряд, Театральная");
        cv.put(colWork, "Режим работы кассы: пн-вс 11.00–15.00, 16.00–20.00 (в здании дирекции), 11.00–14.00, 15.00–19.00 (в здании Новой сцены), 12.00–16.00, 18.00–20.00 (в здании Основной сцены)");
        db.insert(table, colID, cv);

        cv.put(colID, 3);
        cv.put(colType, "attraction");
        cv.put(colCity, "Москва");
        cv.put(colName, "Пушкинская площадь");
        cv.put(colLat, 55.765445);
        cv.put(colLng, 37.606005);
        cv.put(colAddress, "");
        cv.put(colPhone, "");
        cv.put(colDesc, "");
        cv.put(colUnder, "Тверская, Пушкинская");
        cv.put(colWork, "");
        db.insert(table, colID, cv);

        cv.put(colID, 4);
        cv.put(colType, "attraction");
        cv.put(colCity, "Москва");
        cv.put(colName, "Царицыно");
        cv.put(colLat, 55.612572);
        cv.put(colLng, 37.684018);
        cv.put(colAddress, "Дольская, 1");
        cv.put(colPhone, "(495) 321 63 66, 321 80 39, 321 07 43, (499) 725 72 87");
        cv.put(colDesc, "Парк");
        cv.put(colUnder, "Царицыно, Орехово");
        cv.put(colWork, "4 апреля – 31 октября: вт–пт 11.00–18.00, сб 11.00–20.00, вс 11.00–19.00, 1 ноября – 3 апреля: ср–пт 11.00–18.00, сб 11.00–20.00, вс 11.00–19.00, касса закрывается на час раньше; парк: пн-вс 6.00–0.00 ");
        db.insert(table, colID, cv);

        cv.put(colID, 5);
        cv.put(colType, "attraction");
        cv.put(colCity, "Москва");
        cv.put(colName, "Цирк на проспекте Вернадского");
        cv.put(colLat, 55.694496);
        cv.put(colLng, 37.540280);
        cv.put(colAddress, "просп. Вернадского, 7");
        cv.put(colPhone, "(495) 930 28 15 (автоответчик), 930 03 00, 939 45 47 (заказ и доставка билетов)");
        cv.put(colDesc, "Самый вместительный Московский цирк");
        cv.put(colUnder, "Университет");
        cv.put(colWork, "пн-вс 10.30-19.00");
        db.insert(table, colID, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Cursor getAllPlaces() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table, new String[]{});
        return cursor;
    }
}