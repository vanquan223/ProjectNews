package vn.vnpt.vanquan223.projectnews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.vnpt.vanquan223.projectnews.model.ListNewsDBModel;
import vn.vnpt.vanquan223.projectnews.model.ListNewsModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "vnpt.db";
    public static int DB_VERSION = 1;
    public static String TABLE_NEWS = "LISTNEWS";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table " + TABLE_NEWS + "("
                + BOOKMARK.id + " integer primary key, "
                + BOOKMARK.date + " text, "
                + BOOKMARK.title + " text, "
                + BOOKMARK.excerpt + " text, "
                + BOOKMARK.image + " text, "
                + BOOKMARK.content + " text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDB(ListNewsModel list) {
//        for (ListNewsModel list : lists) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKMARK.id, list.getId());
        values.put(BOOKMARK.date, list.getDate());
        if (list.getTitle() != null)
            values.put(BOOKMARK.title, list.getTitle().getRendered());
        if (list.getExcerpt() != null)
            values.put(BOOKMARK.excerpt, list.getExcerpt().getRendered());
        if (list.getBetter_featured_image() != null)
            values.put(BOOKMARK.image, list.getBetter_featured_image().getSource_url());
        if (list.getContent() != null)
            values.put(BOOKMARK.content, (list.getContent().getRendered()));
        db.insert(TABLE_NEWS, null, values);
//        }
    }

    public List<ListNewsDBModel> selectAllNews() {
        List<ListNewsDBModel> lists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from " + TABLE_NEWS;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ListNewsDBModel list = new ListNewsDBModel(
                        /*cursor.getString(cursor.getColumnIndex(BOOKMARK.date)),
                        cursor.getString(cursor.getColumnIndex(BOOKMARK.title)),
                        cursor.getString(cursor.getColumnIndex(BOOKMARK.excerpt)),
                        cursor.getString(cursor.getColumnIndex(BOOKMARK.image)),
                        cursor.getString(cursor.getColumnIndex(BOOKMARK.content))*/
                );
                list.setDate(cursor.getString(cursor.getColumnIndex(BOOKMARK.date)));
                list.setTitle(cursor.getString(cursor.getColumnIndex(BOOKMARK.title)));
                list.setExcerpt(cursor.getString(cursor.getColumnIndex(BOOKMARK.excerpt)));
                list.setImage(cursor.getString(cursor.getColumnIndex(BOOKMARK.image)));
                list.setContent(cursor.getString(cursor.getColumnIndex(BOOKMARK.content)));
                lists.add(list);
            } while (cursor.moveToNext());
        }
        return lists;
    }


    public boolean checkBookMark(ListNewsModel item) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from " + TABLE_NEWS + " WHERE "+BOOKMARK.id + " = " +item.getId();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public void deleteBookmark(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, BOOKMARK.id + " = ?", new String[]{id+""});
    }
}
