package com.example.yunjin_choi.sqliteexam.handler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yunjin_choi.sqliteexam.dto.ProductDTO;
import com.example.yunjin_choi.sqliteexam.provider.MyContentProvider;

public class MyHandler extends SQLiteOpenHelper {

    private static final String TAG = "MyHandler.java";

    // DB Version
    private static final int DATABASE_VERSION = 1;

    // DB NAME
    private static final String DATABASE_NAME = "productDB.db";

    // TABLE NAME
    public static final String TABLE_NAME = "PRODUCT_TB";

    // TABLE COLUMN
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT_NAME = "productName";
    public static final String COLUMN_PRODUCT_QUANTITY = "productQuantity";

    private ContentResolver contentResolver;

    public MyHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        contentResolver = context.getContentResolver();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY , " +
                COLUMN_PRODUCT_NAME + " TEXT , " +
                COLUMN_PRODUCT_QUANTITY + " INTEGER " + " ) ";

        Log.i(TAG + " onCreate : ", CREATE_PRODUCTS_TABLE);

        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    // 이전 버전보다 더 높은 데이터베이스 버전을 갖고 핸들러가 실행된다면 onUpgrade가 실행된다.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // 테이블이 존재한다면 삭제한다.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // 삭제된 테이블을 다시 생성하기 위한 onCreate() 호출
        onCreate(sqLiteDatabase);
    }

    // Product Insert Method
    public void insertProduct(ProductDTO product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_QUANTITY, product.getProductQuantity());


        contentResolver.insert(MyContentProvider.CONTENT_URI, values);
    }

    // Product SELECT By Product Name
    public ProductDTO findProductByName(String productName) {

        // Select될 데이터의 열 순서.
        String[] projection = {COLUMN_ID, COLUMN_PRODUCT_NAME, COLUMN_PRODUCT_QUANTITY};

        // selection 조건절을 나타낸다.
        String selection = COLUMN_PRODUCT_NAME + " = \"" + productName + "\"";

        Cursor cursor = contentResolver.query(MyContentProvider.CONTENT_URI, projection, selection, null, null);

        ProductDTO product = null;

        if (cursor.moveToFirst()) {
            product = new ProductDTO();
            cursor.moveToFirst();

            product.setProductID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setProductQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }

        return product;
    }


    // Delete Product By productName
    public int deleteProductByName(String productName) {

        String selection = COLUMN_PRODUCT_NAME + " = \"" + productName + "\"";

        int rowsDeleted = contentResolver.delete(MyContentProvider.CONTENT_URI , selection , null);

        return rowsDeleted;

    }
}
