package com.eddy.enote;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.eddy.enote.note.ENote;
import com.eddy.enote.note.ENoteEty;
import com.eddy.enote.note.ENoteService;

public class NoteEditorAct extends Activity {
	
	private EditText mText;
	private int mState;
	private ENoteService enoteService;
	
	private static final int state_insert = 0;
	private static final int state_edit = 1;
	private Integer id = null;
	private ENoteEty editEty = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.enote_edit);
		mText = (EditText) findViewById(R.id.enoteEditor);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_EDIT)) {
			id = intent.getExtras().getInt(ENote.column_name_id);
			mState = state_edit;
		}
		else if(action.equals(Intent.ACTION_INSERT)) {
			mState = state_insert;
			id = null;
			editEty = null;
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
			editEty = enoteService.getById(id);
			mText.setText(editEty.getContext());
			setTitle(editEty.getTitle());
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
			setResult(RESULT_OK, null);
			finish();
			break;
		case R.id.menu_delete:
			if(mState == state_insert) {
				finish();
			}
			else if(mState == state_edit) {
				AlertDialog.Builder build = new Builder(this);
				build.setTitle(getResources().getString(R.string.delete_confirm_title));
				build.setMessage(getResources().getString(R.string.delete_confirm_msg) + " " + editEty.getTitle() + " ? ");
				build.setPositiveButton(getResources().getString(R.string.confirm_ok), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						enoteService.deleteById(id);
						setResult(RESULT_OK, null);
						finish();
					}
				});
				build.setNegativeButton(getResources().getString(R.string.confirm_cancel), new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				build.create().show();
			}			
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void doSave(String title, String text) {
		if(text.trim().equals(""))
			return;
		
		ENoteEty ety = new ENoteEty();
		if(title == null || title.trim().equals("")) {
			if(text.length() > 4) {
				title = text.substring(0, 4);
			}
			else {
				title = text;
			}
		}
		
		ety.setContext(text);
		Date d = new Date();
		ety.setModifiedDate(d);
		
		if(mState == state_insert) {
			ety.setTitle(title);
			ety.setCreateDate(d);
			enoteService.insertOrUpdate(ety);
		}
		else if(mState == state_edit) {
			ety.setId(id);
			ety.setTitle(editEty.getTitle());
			ety.setCreateDate(editEty.getCreateDate());
			enoteService.insertOrUpdate(ety);
		}
	}

}
