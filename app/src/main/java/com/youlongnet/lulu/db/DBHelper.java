package com.youlongnet.lulu.db;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class DBHelper {

    private static DBHelper instance;
    private int dbVersion = 1;
    private DbUtils dbUtils;

    private DBHelper(Context context) {
        onCreate(context);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return "lulu.db";
    }

    private void onCreate(Context context) {
        dbUtils = DbUtils.create(context, getUserDatabaseName(), dbVersion,
                new DbUpgrade());
    }

    public void save(Object o) {
        try {
            getDbUtils().save(o);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<?> list) {
        try {
            getDbUtils().saveAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public class DbUpgrade implements DbUpgradeListener {

        @Override
        public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {

        }
    }
}
