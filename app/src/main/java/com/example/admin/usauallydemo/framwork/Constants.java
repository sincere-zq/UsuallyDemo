package com.example.admin.usauallydemo.framwork;


import com.example.admin.usauallydemo.framwork.db.model.UserModel;

/**
 * Created by Lizhangfeng on 2016/7/13 0013.
 * Description: 常量类
 */
public class Constants {

    public static final String SQLITE_NAME = "tuanche_db";//数据库名称

    public static final int SQLITE_VERSION = 1;//数据库版本

    //数据库所有的表
//    public static String[] TABLES = new String[]{UserModel.class.getName(), SearchHistoryModel.class.getName()};
    public static String[] TABLES = new String[]{UserModel.class.getName()};
}
