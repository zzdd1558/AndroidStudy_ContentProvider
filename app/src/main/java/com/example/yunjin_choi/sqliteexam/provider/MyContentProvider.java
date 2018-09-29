package com.example.yunjin_choi.sqliteexam.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.yunjin_choi.sqliteexam.handler.MyHandler;

public class MyContentProvider extends ContentProvider {

    private MyHandler myDB;

    // provider를 만들었을때 입력한 authority , manifests에서 확인 가능.
    private static final String AUTHORITY =
            "com.yunjin_choi.provider.MyContentProvider";

    // 데이터베이스 테이블 이름
    private static final String PRODUCT_TABLE = "PRODUCT_TB";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PRODUCT_TABLE);

    public static final int PRODUCTS = 1;
    public static final int PRODUCTS_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);


    // 테이블만 참고할 떄는 상수값 1을 반환하고
    // 테이블과 행을 같이 참고할 때 상수값 2를 반환한다.
    static {
        sURIMatcher.addURI(AUTHORITY, PRODUCT_TABLE, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, PRODUCT_TABLE + "/#", PRODUCTS_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case PRODUCTS :
                rowsDeleted = db.delete(MyHandler.TABLE_NAME , selection , selectionArgs );
                break;

            case PRODUCTS_ID :

                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(MyHandler.TABLE_NAME , MyHandler.COLUMN_ID + " = " + id , null);
                }else{
                    rowsDeleted = db.delete(MyHandler.TABLE_NAME , MyHandler.COLUMN_ID + " = " + id + " and " + selection, selectionArgs);
                }

                break;

            default :
                throw new IllegalArgumentException("알수없는 URI ");
        }

        getContext().getContentResolver().notifyChange(uri , null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //URI와 ContentValues 객체가 전달된다 . URI 객체는 추가될 곳을 지정하며 , ContentValues에는 추가될 데이터가 포함된다.
        // URI 타입을 확인하기 위해 sUriMatcher객체를 사용한다
        // URI 가 부적합하다면 예외를 발생시켜야한다.
        // SQLite 데이터베이스의 쓰기 가능한 인스턴스 참조를 얻는다.
        // SQL insert 명령을 수행하여 데이터를 데이터베이스 테이블에 추가한다.
        // 데이터베이스 데이터가 변경되었다는 것을 콘텐트 리졸버에게 알림한다.
        // 새로 추가된 테이블 행의 URI를 반환한다.

        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase db = myDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case PRODUCTS:
                id = db.insert(MyHandler.TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("알수 없는 URI 입니다. : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PRODUCT_TABLE + "/" + id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        myDB = new MyHandler(getContext(), null, null, 1);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // 조회 요청된 데이터를 반환하기 위해 콘텐트 제공자가 호출되면 콘텐트 제공자 클래스의 query()를 메소드가 호출된다.

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(myDB.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {

            case PRODUCTS_ID:
                queryBuilder.appendWhere(MyHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case PRODUCTS:
                break;

            default:
                throw new IllegalArgumentException("알수없는 URI입니다.");

        }

        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase() , projection , selection , selectionArgs,null , null , sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver() , uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {

            case PRODUCTS:

                rowsUpdated = db.update(MyHandler.TABLE_NAME , values , selection , selectionArgs);
                break;

            case PRODUCTS_ID:

                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(MyHandler.TABLE_NAME , values , MyHandler.COLUMN_ID + " = " + id , null);
                }else{
                    rowsUpdated = db.update(MyHandler.TABLE_NAME , values , MyHandler.COLUMN_ID + " = " + id + " and " + selection , selectionArgs);
                }

                break;

            default:
                throw new IllegalArgumentException("알수없는 URI입니다.");
        }

        getContext().getContentResolver().notifyChange(uri , null);

        return rowsUpdated;
    }
}
