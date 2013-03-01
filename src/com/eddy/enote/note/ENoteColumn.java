package com.eddy.enote.note;

import android.net.Uri;
import android.provider.BaseColumns;

public class ENoteColumn implements BaseColumns {

	private static final String Table_Name = "eddy_note";
	
	private static final String Scheme = "content://";
	
	private static final String Path_Note = "/enote";
	
	private static final String Path_Note_ID = "/enote/";
	
	public static final Uri Content_URI = Uri.parse(Scheme + ENote.AUTHORITY + Path_Note);
	
}
