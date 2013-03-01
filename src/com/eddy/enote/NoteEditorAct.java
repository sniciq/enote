package com.eddy.enote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class NoteEditorAct extends Activity {
	
	private EditText mText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.enote_edit);
		mText = (EditText) findViewById(R.id.enoteEditor);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_EDIT)) {
			
		}
		else if(action.equals(Intent.ACTION_INSERT)) {
			
		}
	}
	
	@Override
    protected void onResume() {
		super.onResume();
		setTitle("AAAAAAAAAA");
	}

}
