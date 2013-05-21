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

        cv.put(colID, 6);
        cv.put(colType, "entertainment");
        cv.put(colCity, "Москва");
        cv.put(colName, "16 тонн");
        cv.put(colLat, 55.764288);
        cv.put(colLng, 37.564466);
        cv.put(colAddress, "Пресненский Вал, 6, стр. 1 ");
        cv.put(colPhone, "(499) 253 53 00");
        cv.put(colDesc, "Один из лучших музыкальных клубов");
        cv.put(colUnder, "Улица 1905 года ");
        cv.put(colWork, "пн-вс 11.00–6.00");
        db.insert(table, colID, cv);

        cv.put(colID, 7);
        cv.put(colType, "entertainment");
        cv.put(colCity, "Москва");
        cv.put(colName, "Б2");
        cv.put(colLat, 55.767003);
        cv.put(colLng, 37.592983);
        cv.put(colAddress, "Б.Садовая, 8/1");
        cv.put(colPhone, "(495) 650 99 09");
        cv.put(colDesc, "");
        cv.put(colUnder, "Маяковская");
        cv.put(colWork, "пн-вс 12.00–6.00 ");
        db.insert(table, colID, cv);

        cv.put(colID, 8);
        cv.put(colType, "entertainment");
        cv.put(colCity, "Москва");
        cv.put(colName, "Stadium Live");
        cv.put(colLat, 55.807807);
        cv.put(colLng, 37.511786);
        cv.put(colAddress, "Ленинградский просп., 80, корп. 17 ");
        cv.put(colPhone, "(495) 540 55 40");
        cv.put(colDesc, "");
        cv.put(colUnder, "Сокол ");
        cv.put(colWork, "пн-вс 10.00–22.00");
        db.insert(table, colID, cv);

        cv.put(colID, 9);
        cv.put(colType, "entertainment");
        cv.put(colCity, "Москва");
        cv.put(colName, "Crocus City Hall");
        cv.put(colLat, 55.819158);
        cv.put(colLng, 37.387545);
        cv.put(colAddress, "66-й км МКАД");
        cv.put(colPhone, "(499) 550 00 55");
        cv.put(colDesc, "Один из самых востребованных залов города");
        cv.put(colUnder, "Мякинино");
        cv.put(colWork, "");
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
//private DBcontroller dbHelper =  new DBcontroller(this);
/*
            Cursor cursor = dbHelper.getAllPlaces();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String type = cursor.getString(cursor.getColumnIndex("PlacesType"));
                String name = cursor.getString(cursor.getColumnIndex("PlaceName"));
                String desc = cursor.getString(cursor.getColumnIndex("PlaceDesc"));
                String under = cursor.getString(cursor.getColumnIndex("PlaceUnder"));
                String work = cursor.getString(cursor.getColumnIndex("PlaceWork"));
                String decription = desc + "\n" + "Метро: " + under + "\n" + "Время работы: " + work;
                Double lat = cursor.getDouble(cursor.getColumnIndex("PlaceLat"));
                Double lng = cursor.getDouble(cursor.getColumnIndex("PlaceLng"));

                if (type.equals("attraction")) {
                    attractions.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, decription));
                } else if (type.equals("entertainment")) {
                    entertainment.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, decription));
                }

                cursor.moveToNext();
            }
            cursor.close();
*/