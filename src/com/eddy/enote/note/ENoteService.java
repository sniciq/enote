package com.eddy.enote.note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ENoteService {

	private DatabaseHelper dbHelper;
	
	public ENoteService(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void insertOrUpdate(ENoteEty ety) {
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
		if(ety.getId() == null) {
			StringBuffer sb = new StringBuffer();
			sb.append("insert into ");
			sb.append(ENote.TableName);
			sb.append("(");
			sb.append(ENote.column_name_title + ",");
			sb.append(ENote.column_name_context + ",");
			sb.append(ENote.column_name_create_date + ",");
			sb.append(ENote.column_name_modified_date);
			sb.append(")values(?,?,?,?)");
			db.execSQL(sb.toString(),new Object[]{ety.getTitle(), ety.getContext(), ety.getCreateDate().getTime(), ety.getModifiedDate().getTime()});  
		}
		else {
			StringBuffer sb = new StringBuffer();
			sb.append("update ");
			sb.append(ENote.TableName);
			sb.append(" set ");
			sb.append(ENote.column_name_title + "=?,");
			sb.append(ENote.column_name_context + "=?,");
			sb.append(ENote.column_name_create_date + "=?,");
			sb.append(ENote.column_name_modified_date + "=?");
			sb.append(" where " + ENote.column_name_id + "=?;");
			db.execSQL(sb.toString(),new Object[]{ety.getTitle(), ety.getContext(), ety.getCreateDate().getTime(), ety.getModifiedDate().getTime(), ety.getId()});  
		}
	}
	
	public ENoteEty getById(int id) {
		ENoteEty ety = null;
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + ENote.TableName +" where " + ENote.column_name_id + " =?;", new String[]{id+""});
			if(c.moveToNext()) {
				String title = c.getString(c.getColumnIndex(ENote.column_name_title));
				String context = c.getString(c.getColumnIndex(ENote.column_name_context));
				long createDate = c.getLong(c.getColumnIndex(ENote.column_name_create_date));
				long modifiedDate = c.getLong(c.getColumnIndex(ENote.column_name_modified_date));
				ety = new ENoteEty();
				ety.setId(id);
				ety.setTitle(title);
				ety.setContext(context);
				ety.setModifiedDate(new Date(createDate));
				ety.setCreateDate(new Date(modifiedDate));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ety;
	}
	
	public void deleteById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
		db.execSQL("delete from " + ENote.TableName + " where " + ENote.column_name_id + "=?;", new Object[]{id});
	}
	
	public Cursor getAllNoteCursor() {
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + ENote.TableName +" order by " + ENote.column_name_modified_date + " desc;", null);
			while(c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex(ENote.column_name_id));
				String title = c.getString(c.getColumnIndex(ENote.column_name_title));
				String context = c.getString(c.getColumnIndex(ENote.column_name_context));
				long createDate = c.getLong(c.getColumnIndex(ENote.column_name_create_date));
				long modifiedDate = c.getLong(c.getColumnIndex(ENote.column_name_modified_date));
				ENoteEty ety = new ENoteEty();
				ety.setId(id);
				ety.setTitle(title);
				ety.setContext(context);
				ety.setModifiedDate(new Date(createDate));
				ety.setCreateDate(new Date(modifiedDate));
			}
			return c;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ENoteEty> getAllNote() {
		List<ENoteEty> notes = new ArrayList<ENoteEty>();
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor c = db.rawQuery("select * from " + ENote.TableName, null);
			while(c.moveToNext()) {
				int id = c.getInt(c.getColumnIndex(ENote.column_name_id));
				String title = c.getString(c.getColumnIndex(ENote.column_name_title));
				String context = c.getString(c.getColumnIndex(ENote.column_name_context));
				long createDate = c.getLong(c.getColumnIndex(ENote.column_name_create_date));
				long modifiedDate = c.getLong(c.getColumnIndex(ENote.column_name_modified_date));
				ENoteEty ety = new ENoteEty();
				ety.setId(id);
				ety.setTitle(title);
				ety.setContext(context);
				ety.setModifiedDate(new Date(createDate));
				ety.setCreateDate(new Date(modifiedDate));
				notes.add(ety);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return notes;
	}

	public void saveTitle(int id, String string) {
		SQLiteDatabase db = dbHelper.getWritableDatabase(); 
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(ENote.TableName);
		sb.append(" set ");
		sb.append(ENote.column_name_title + "=? ");
		sb.append(" where " + ENote.column_name_id + "=?;");
		db.execSQL(sb.toString(), new Object[]{string, id});
	}
}
