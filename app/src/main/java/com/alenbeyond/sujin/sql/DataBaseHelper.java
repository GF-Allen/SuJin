package com.alenbeyond.sujin.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alenbeyond.sujin.bean.SuJinHome;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by AlenBeyond on 2016/9/4.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "Sujin.db";

    private static DataBaseHelper instance;
    private Dao<SuJinHome, Integer> homeDao;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 5);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DataBaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DataBaseHelper.class) {
                if (instance == null)
                    instance = new DataBaseHelper(context);
            }
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.clearTable(connectionSource, SuJinHome.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, SuJinHome.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<SuJinHome, Integer> getHomeDao() throws SQLException {
        if (homeDao == null) {
            homeDao = getDao(SuJinHome.class);
        }
        return homeDao;
    }

    @Override
    public void close() {
        super.close();
        homeDao = null;
    }
}
