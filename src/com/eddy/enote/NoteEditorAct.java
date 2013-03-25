package com.eddy.enote;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.eddy.enote.note.ENoteEty;
import com.eddy.enote.note.ENoteService;

public class NoteEditorAct extends Activity {
	
	private EditText mText;
	private int mState;
	private ENoteService enoteService;
	
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
		}
		enoteService = new ENoteService(this);
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
			Intent output = new Intent();
			setResult(RESULT_OK, output);
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
		if(text.trim().equals(""))
			return;
		
		if(mState == state_insert) {
			ENoteEty ety = new ENoteEty();
			if(text.length() > 4) {
				ety.setTitle(text.substring(0, 4));
			}
			else {
				ety.setTitle(text);
			}
			ety.setContext(text);
			Date d = new Date();
			ety.setCreateDate(d);
			ety.setModifiedDate(d);
			enoteService.insertOrUpdate(ety);
		}
	}

}
