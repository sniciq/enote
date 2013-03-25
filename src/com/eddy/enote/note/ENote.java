package com.eddy.enote.note;


public class ENote {
	public static final String AUTHORITY = "com.eddy.enote.note.ENote";
	public static final String TableName = "eddy_note";
	public static final String column_name_id = "_id";
	public static final String column_name_title = "title";
	public static final String column_name_context = "context";
	public static final String column_name_create_date = "createDate";
	public static final String column_name_modified_date = "modifiedDate";
	public static final String DEFAULT_SORT_ORDER = "modifiedDate desc";
	
	private ENote() {
	}
}
