package com.example.admin.usauallydemo.framwork.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.admin.usauallydemo.framwork.db.DBHelper;

import java.util.Map;


/**
 * Created by zengqiang on 2016/8/8.
 * Description:MySqlLiteDemo
 */
public abstract class BaseModel implements BaseColumns {

    private DBHelper dbHelper;

    public BaseModel() {
        dbHelper = DBHelper.getInstance();
    }

    /**
     * 创建表格sql语句
     * CREATE TABLE COMPANY(
     * ID INTEGER PRIMARY KEY AUTOINCREMENT,
     * NAME           TEXT    NOT NULL,
     * AGE            INT     NOT NULL,
     * ADDRESS        CHAR(50),
     * SALARY         REAL)
     * )
     */
    public String getCreateTable(String tableName, Map<String, String> map) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create table ").append(tableName).append(" (");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue());
            stringBuilder.append(",");
        }
        String sql = stringBuilder.substring(0, stringBuilder.length() - 1);
        sql = sql + ")";
        return sql;
    }

    public String getCreateTableSql() {
        return getCreateTable(getTableName(), getParamsMap());
    }

    /**
     * 获得表名称
     */

    protected abstract String getTableName();

    /**
     * 获得表格行列族名集合
     */

    protected abstract Map<String, String> getParamsMap();

    /**
     * 插入一行内容
     */

    public void insert( ContentValues values) {
        dbHelper.getWritableDatabase().insert(getTableName(), null, values);
    }

    /**
     * 修改一条内容
     */

    public void update( ContentValues values, String whereClause, String[] whereArgs) {
        dbHelper.getWritableDatabase().update(getTableName(),values, whereClause, whereArgs);
    }

    /**
     * 删除一条数据
     */

    public void delete(String whereClause, String[] whereArgs) {
        dbHelper.getWritableDatabase().delete(getTableName(), whereClause, whereArgs);
    }

    /**
     * 查看数据
     */

    public Cursor query(String sql) {
        return dbHelper.getReadableDatabase().rawQuery(sql, null);
    }

    /**
     * 查看所有数据
     */
    public Cursor queryAll(){
        return dbHelper.getReadableDatabase().query(getTableName(),null,null,null,null,null,null);
    }
    /**
     * 清空表
     */

    public void clear(){
        dbHelper.getWritableDatabase().execSQL("delete from "+getTableName());
    }

    /**
     * 删除整个表
     */

    public void deleteTable(){
        dbHelper.getWritableDatabase().execSQL("drop table "+getTableName());
    }
}
