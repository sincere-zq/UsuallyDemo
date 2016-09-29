package com.example.admin.usauallydemo.framwork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.usauallydemo.MyApplication;
import com.example.admin.usauallydemo.framwork.Constants;
import com.example.admin.usauallydemo.framwork.base.BaseModel;
import com.example.admin.usauallydemo.framwork.utils.LogUtils;


/**
 * Created by zengqiang on 2016/8/8.
 * Description:MySqlLiteDemo
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    private DBHelper(Context context) {
        super(context, Constants.SQLITE_NAME, null, Constants.SQLITE_VERSION);
    }

    public static DBHelper getInstance(){
        if (dbHelper==null)
            dbHelper=new DBHelper(MyApplication.getApplication().getApplicationContext());
        return dbHelper;
    }



    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //第一次安装时运行创建
        try {
            for (int i = 0; i< Constants.TABLES.length; i++){
                Class<BaseModel> baseModleClass= (Class<BaseModel>) Class.forName(Constants.TABLES[i]);
                BaseModel baseModle=baseModleClass.newInstance();
                sqLiteDatabase.execSQL(baseModle.getCreateTableSql());
                LogUtils.e(baseModle.getCreateTableSql());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
