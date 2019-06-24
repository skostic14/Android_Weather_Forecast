package com.example.projekat1;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WxProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.projekat1";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, WxHelper.TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.projekat1.weather";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.projekat1.weather";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, WxHelper.TABLE_NAME, 1);
        sUriMatcher.addURI(AUTHORITY, WxHelper.TABLE_NAME + "/#",  2);
    }

    public WxProvider(){}

    private WxHelper mHelper = null;

    private int delete(String selection, String[] selectionArgs) {
        int deleted = 0;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        deleted = db.delete(WxHelper.TABLE_NAME, selection, selectionArgs);
        mHelper.close();

        return deleted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted = 0;

        switch (sUriMatcher.match(uri)) {
            case 1:
                deleted = delete(selection, selectionArgs);
                break;
            case 2:
                deleted = delete(WxHelper.COLUMN_CITY_NAME + "=?", new String[] {uri.getLastPathSegment()});
                break;
            case 3:
                deleted = delete(WxHelper.COLUMN_DATE_TIME + "<?", new String[] {uri.getLastPathSegment()});
                break;
            default:
        }

        if (deleted > 0 ) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        String type = null;

        switch (sUriMatcher.match(uri)) {
            case 1:
                type = CONTENT_TYPE;    // We expect Cursor to have 0 to infinity items
                break;
            case 2:
                type = CONTENT_ITEM_TYPE;   // We expect Cursor to have 1 item
                break;
            default:
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(WxHelper.TABLE_NAME, null, values);
        mHelper.close();

        if (id != -1) {
            ContentResolver resolver = getContext().getContentResolver();
            if (resolver == null)
                return null;

            resolver.notifyChange(uri, null);
        }

        return Uri.withAppendedPath(uri, Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        // Initialization of content provider on startup.
        mHelper = new WxHelper(getContext());
        return true;
    }

    private Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.query(WxHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Handling query requests from clients.
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case 1:
                cursor = query(projection, selection, selectionArgs, sortOrder);
                break;
            case 2:
                cursor = query(projection, WxHelper.COLUMN_CITY_NAME + "=?",
                        new String[]{uri.getLastPathSegment()}, null);
                break;
            default:
        }

        return cursor;
    }

    private int update(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updated = db.update(WxHelper.TABLE_NAME, values, selection, selectionArgs);
        mHelper.close();
        return updated;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Handling requests to update one or more rows.
        int updated = 0;

        switch (sUriMatcher.match(uri)) {
            case 1:
                updated = update(values, selection, selectionArgs);
                break;
            case 2:
                updated = update(values, "_id=?", new String[]{uri.getLastPathSegment()});
                break;
            default:
        }

        if (updated > 0) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return updated;
    }
}
