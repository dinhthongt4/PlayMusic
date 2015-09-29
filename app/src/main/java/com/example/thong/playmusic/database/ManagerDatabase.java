package com.example.thong.playmusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thong.playmusic.model.Album;
import com.example.thong.playmusic.model.Tracks;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thong on 04/09/2015.
 */
public class ManagerDatabase {

    private static final String TAG = "ManagerDatabase";
    private static final String DATABASE_NAME = "media_player";
    public static final int DATABASE_VERSION = 3;
    private static final String TABLE_MEDIA_INFO = "tbl_media_info";
    private static final String TABLE_MEDIA_TYPE = "tbl_media_type";
    private static final String TABLE_MEDIA_GROUP = "tbl_media_group";
    private static final String TABLE_MEDIA_GROUP_NAME = "tbl_media_group_name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID_TYPE = "id_type";
    private static final String COLUMN_ARTIST = "artist";
    private static final String COLUMN_SINGER = "singer";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ALBUM = "album";
    private static final String COLUMN_COPY_RIGHT = "copy_right";
    private static final String COLUMN_TYPE_NAME = "type_name";
    private static final String COLUMN_ID_GROUP = "id_group";
    private static final String COLUMN_GROUP_NAME = "group_name";

    private static Context mContext;
    static SQLiteDatabase mDb;
    private OpenHelper mOpenHelper;


    public ManagerDatabase(Context context) {
        this.mContext = context;
    }

    public ManagerDatabase open() throws SQLException {
        mOpenHelper = new OpenHelper(mContext);
        mDb = mOpenHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mOpenHelper.close();
    }

    public long insertMediaInfo(String path, String name, int idType, String artist
            , String singer, String duration
            , String imgPath, String description, String album, String copyRight) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PATH, path);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_ID_TYPE, idType);
        cv.put(COLUMN_ARTIST, artist);
        cv.put(COLUMN_SINGER, singer);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_IMAGE_PATH, imgPath);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_ALBUM, album);
        cv.put(COLUMN_COPY_RIGHT, copyRight);

        return mDb.insert(TABLE_MEDIA_INFO, null, cv);
    }

    public long insertMediaType(int idType, String type) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID_TYPE, idType);
        cv.put(COLUMN_TYPE_NAME, type);
        return mDb.insert(TABLE_MEDIA_TYPE, null, cv);
    }

    public long insertMediaGroup(int id, int idGroup) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ID_GROUP, idGroup);

        return mDb.insert(TABLE_MEDIA_GROUP, null, cv);
    }

    public long insertMediaGroupName(String groupName) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROUP_NAME, groupName);
        return mDb.insert(TABLE_MEDIA_GROUP_NAME, null, cv);
    }

    public ArrayList<Album> getNameAlbum() {

        ArrayList<Album> albumArrayList = new ArrayList<>();

        String[] columns = new String[]{COLUMN_ID_GROUP, COLUMN_GROUP_NAME};
        Cursor c = mDb.query(TABLE_MEDIA_GROUP_NAME, columns, null, null, null, null, null);
        if(c == null) {
            Log.v(TAG,"C = null");
        }

        int iIdAlbum = c.getColumnIndex(COLUMN_ID_GROUP);
        int iNameAlbum = c.getColumnIndex(COLUMN_GROUP_NAME);

        while (c.moveToNext()) {
            Album album = new Album();
            album.setIdAlbum( c.getInt(iIdAlbum));
            album.setAlbumName(c.getString(iNameAlbum));
            albumArrayList.add(album);
        }
        c.close();
        return albumArrayList;
    }

    public ArrayList<Tracks> getDataMediaInfo() {

        ArrayList<Tracks> trackses = new ArrayList<>();
        String[] columns = new String[]{COLUMN_ID, COLUMN_PATH, COLUMN_NAME, COLUMN_ID_TYPE, COLUMN_ARTIST
                , COLUMN_SINGER, COLUMN_DURATION, COLUMN_IMAGE_PATH, COLUMN_DESCRIPTION, COLUMN_ALBUM, COLUMN_COPY_RIGHT};
        Cursor c = mDb.query(TABLE_MEDIA_INFO, columns, null, null, null, null, null);
        if (c == null) {
            Log.v("Cursor", "C is NULL");
        }
        String result = "";

        int iID = c.getColumnIndex(COLUMN_ID);
        int iPath = c.getColumnIndex(COLUMN_PATH);
        int iName = c.getColumnIndex(COLUMN_NAME);
        int iIDType = c.getColumnIndex(COLUMN_ID_TYPE);
        int iArtist = c.getColumnIndex(COLUMN_ARTIST);
        int iSinger = c.getColumnIndex(COLUMN_SINGER);
        int iDuration = c.getColumnIndex(COLUMN_DURATION);
        int iImagePath = c.getColumnIndex(COLUMN_IMAGE_PATH);
        int iDescription = c.getColumnIndex(COLUMN_DESCRIPTION);
        int iAlbum = c.getColumnIndex(COLUMN_ALBUM);
        int iCopyRight = c.getColumnIndex(COLUMN_COPY_RIGHT);

        while (c.moveToNext()) {
            Tracks tracks = new Tracks();
            tracks.setId(c.getInt(iID));
            tracks.setStream_url(c.getString(iPath));
            tracks.setArtist(c.getString(iArtist));
            tracks.setDuration(Long.parseLong(c.getString(iDuration)));
            tracks.setTitle(c.getString(iName));
            tracks.setArtwork_url(c.getString(iImagePath));
            trackses.add(tracks);
        }
        c.close();
        return trackses;
    }

    public Tracks getDataMediaInfo(int id) {

        Tracks tracks = null;
        Cursor c = mDb.rawQuery("SELECT * FROM " + TABLE_MEDIA_INFO + " WHERE " + COLUMN_ID + "='" + id + "'", null);
        if (c == null) {
            Log.v("Cursor", "C is NULL");
        }

        int iID = c.getColumnIndex(COLUMN_ID);
        int iPath = c.getColumnIndex(COLUMN_PATH);
        int iName = c.getColumnIndex(COLUMN_NAME);
        int iIDType = c.getColumnIndex(COLUMN_ID_TYPE);
        int iArtist = c.getColumnIndex(COLUMN_ARTIST);
        int iSinger = c.getColumnIndex(COLUMN_SINGER);
        int iDuration = c.getColumnIndex(COLUMN_DURATION);
        int iImagePath = c.getColumnIndex(COLUMN_IMAGE_PATH);
        int iDescription = c.getColumnIndex(COLUMN_DESCRIPTION);
        int iAlbum = c.getColumnIndex(COLUMN_ALBUM);
        int iCopyRight = c.getColumnIndex(COLUMN_COPY_RIGHT);

        while (c.moveToNext()) {
            tracks = new Tracks();
            tracks.setId(iID);
            tracks.setStream_url(c.getString(iPath));
            tracks.setArtist(c.getString(iArtist));
            tracks.setDuration(Long.parseLong(c.getString(iDuration)));
            tracks.setTitle(c.getString(iName));
            tracks.setArtwork_url(c.getString(iImagePath));
        }
        c.close();
        return tracks;
    }

    public int getIDAlbum(String groupName) {
        int idAlbum = 0;

        Cursor c = mDb.rawQuery("SELECT " + COLUMN_ID_GROUP + " FROM " + TABLE_MEDIA_GROUP_NAME + " WHERE " + COLUMN_GROUP_NAME + "='" + groupName + "'", null);
        if (c == null) {
            Log.v("Cursor", "C is NULL");
        }

        int iID = c.getColumnIndex(COLUMN_ID_GROUP);

        while (c.moveToNext()) {
            idAlbum = c.getInt(iID);
        }
        c.close();
        return idAlbum;
    }

    public ArrayList<Integer> getListIdMusics(int id) {
        ArrayList<Integer> idMusics = new ArrayList<>();
        Cursor c = mDb.rawQuery("SELECT "+COLUMN_ID+" FROM "+TABLE_MEDIA_GROUP+" WHERE "+COLUMN_ID_GROUP+"='"+id+"'",null);
        int iId = c.getColumnIndex(COLUMN_ID);
        while (c.moveToNext()) {
            idMusics.add(c.getInt(iId));
        }
        c.close();
        return idMusics;
    }

    public int getIdMusic(String path) {
        int idMusics = 0;

        Cursor c = mDb.rawQuery("SELECT "+COLUMN_ID+" FROM "+TABLE_MEDIA_INFO+" WHERE "+COLUMN_PATH+"='"+path+"'",null);
        if (c == null) {
            Log.v("Cursor", "C is NULL");
        }

        int iID = c.getColumnIndex(COLUMN_ID);

        while (c.moveToNext()) {
            idMusics = c.getInt(iID);
        }
        c.close();
        return idMusics;
    }

    //---------------- class OpenHelper ------------------
    private static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {

            arg0.execSQL("CREATE TABLE " + TABLE_MEDIA_INFO + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_PATH + " NVARCHAR(50) NOT NULL, "
                    + COLUMN_NAME + " NVARCHAR(50), "
                    + COLUMN_ID_TYPE + " INTEGER, "
                    + COLUMN_ARTIST + " NVARCHAR(50), "
                    + COLUMN_SINGER + " NVARCHAR(50), "
                    + COLUMN_DURATION + " VARCHAR(50), "
                    + COLUMN_IMAGE_PATH + " NVARCHAR(50), "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_ALBUM + " NVARCHAR(50), "
                    + COLUMN_COPY_RIGHT + " TEXT);");

            arg0.execSQL("CREATE TABLE " + TABLE_MEDIA_TYPE + " ("
                    + COLUMN_ID_TYPE + " INTEGER PRIMARY KEY, "
                    + COLUMN_TYPE_NAME + " VARCHAR(50));");

            arg0.execSQL("CREATE TABLE " + TABLE_MEDIA_GROUP_NAME + " ("
                    + COLUMN_ID_GROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_GROUP_NAME + " NVARCHAR(50));");

            arg0.execSQL("CREATE TABLE " + TABLE_MEDIA_GROUP + " ("
                    + COLUMN_ID + " INTEGER, "
                    + COLUMN_ID_GROUP + " INTEGER" +
                    "value," +
                    "  PRIMARY KEY ( "+ COLUMN_ID +", "+ COLUMN_ID_GROUP +"));");

            arg0.execSQL("CREATE TRIGGER check_insert BEFORE INSERT ON " + TABLE_MEDIA_INFO + "  \n" +
                    "BEGIN  \n" +
                    "SELECT CASE   \n" +
                    "WHEN (((SELECT " + COLUMN_PATH + " FROM tbl_media_info WHERE " + COLUMN_PATH + " = NEW.path ) IS NOT NULL) AND ((SELECT " + COLUMN_NAME + " FROM tbl_media_info WHERE " + COLUMN_NAME + " = NEW.name ) IS NOT NULL))\n" +
                    "THEN RAISE(ABORT, 'This is an User Define Error Message - This employee_id does not exist.')   \n" +
                    "END;   \n" +
                    "END");
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_GROUP);
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_GROUP_NAME);
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_TYPE);
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_INFO);
            onCreate(arg0);
        }
    }
}