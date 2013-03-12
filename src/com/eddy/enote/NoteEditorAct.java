package com.eddy.enote;

import com.eddy.enote.note.ENoteColumn;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteEditorAct extends Activity {
	
	private EditText mText;
	private int mState;
	private Uri myUri;
	
	private static final int state_insert = 0;
	private static final int state_edit = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.enote_edit);
		mText = (EditText) findViewById(R.id.enoteEditor);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_EDIT)) {
			mState = state_edit;
		}
		else if(action.equals(Intent.ACTION_INSERT)) {
			mState = state_insert;
			myUri = getContentResolver().insert(intent.getData(), null);
			System.out.println(myUri);
		}
	}
	
	@Override
    protected void onResume() {
		super.onResume();
		
		switch (mState) {
		case state_insert:
			setTitle(R.string.title_create);
			break;
		case state_edit:
			//FIXME ±à¼­ title
			setTitle("±à¼­");
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_edtor_option_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			String text = mText.getText().toString();
			doSave(null, text);
			finish();
			break;
		case R.id.menu_delete:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void doSave(String title, String text) {
		ContentValues contentValues = new ContentValues();
		if(mState == state_insert) {
			contentValues.put(ENoteColumn.column_name_title, text.substring(0, 4));
			contentValues.put(ENoteColumn.column_name_create_date, System.currentTimeMillis());
			contentValues.put(ENoteColumn.column_name_modified_date, System.currentTimeMillis());
			contentValues.put(ENoteColumn.column_name_context, text);
//			getContentResolver().update(myUri, contentValues, null, null);
		}
		
		
		
	}

}
