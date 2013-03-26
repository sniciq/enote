package com.eddy.enote.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "enote.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + ENote.TableName + " ("
				+ ENote.column_name_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ ENote.column_name_title + " TEXT,"
				+ ENote.column_name_context + " TEXT,"
				+ ENote.column_name_create_date + " INTEGER ,"
				+ ENote.column_name_modified_date + " INTEGER"
				+ ");"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + ENote.TableName);
	}

}
