package com.gx303.fastandroid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.gx303.fastandroid.utils.FastLogUtils.e;

/**
 * ormlite的DatabaseHelper
 * 用法
 *
 *
 public class dbhelp1 extends FastDatabaseHelper{
     static  String DataBaseName="db1.db";
     static int DATABASE_VERSION=2;
     public dbhelp1(Context context)
     {
         super(context,DataBaseName,DATABASE_VERSION);
     }

     @Override
     public void setBeans() {
         super.beans.add(User.class);
     }
 }
 */
public abstract class FastDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Context mContext;
    public List<Class> beans=new ArrayList<Class>();
    public FastDatabaseHelper(Context context,String DATABASE_NAME,int DATABASE_VERSION)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
        setBeans();
    }
    /**
     * 设置所有的bean类
     */
    public abstract void setBeans();

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            for(int i=0;i<beans.size();i++)
            {
                TableUtils.createTable(connectionSource,beans.get(i));
            }
        }
        catch (SQLException e)
        {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try
        {
            for(int i=0;i<beans.size();i++)
            {
                TableUtils.dropTable(connectionSource, beans.get(i), true);
            }
            onCreate(database, connectionSource);
        }
        catch (SQLException e)
        {

        }
    }
    public SQLiteDatabase openDatabaseFromAsset(String dbname,String fileNameFromAsset)
    {
        SQLiteDatabase database=null;
        try
        {
            File f_database=mContext.getDatabasePath(dbname);
            e(f_database.getPath());
            if(!f_database.exists())
            {
                InputStream is = mContext.getAssets().open(fileNameFromAsset);
                FileOutputStream fos = new FileOutputStream(f_database);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                FileOutputStream out = new FileOutputStream(f_database);
                out.write(buffer);
                out.close();
            }
            database=SQLiteDatabase.openOrCreateDatabase(f_database,null);
        }
        catch (Exception e)
        {
            e("aa"+e.toString());
        }
        finally
        {
            return database;
        }
    }
}
