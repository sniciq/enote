package com.eddy.enote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.eddy.enote.note.ENote;
import com.eddy.enote.note.ENoteColumn;
import com.eddy.enote.note.ENoteService;

public class NoteListAct extends ListActivity {

	private static final int requestCode_editor = 1;

	private SimpleCursorAdapter dataAdapter;
	private ENoteService enoteService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		getListView().setOnCreateContextMenuListener(this);
		
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(ENoteColumn.Content_URI);
		}

		enoteService = new ENoteService(this);
		dataAdapter = new SimpleCursorAdapter(this, R.layout.enote_item, enoteService.getAllNoteCursor(), new String[] { ENote.column_name_title, ENote.column_name_create_date }, new int[] { R.id.title, R.id.createTime }, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
			@Override
			public void setViewText(TextView v, String text) {
				if(v.getId() == R.id.createTime) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					text = df.format(new Date(Long.parseLong(text)));
				}
				super.setViewText(v, text);
			}
		};
//		dataAdapter.setViewBinder(new View);
		
		
//		dataAdapter.setViewBinder(new ViewBinder() {
//			@Override
//			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//				if (columnIndex == 3) {
//	                long createDate = cursor.getLong(cursor.getColumnIndex(ENote.column_name_create_date));
//	                TextView textView = (TextView) view;
//	                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//	                textView.setText(df.format(new Date(createDate)));
//	                return true;
//	         }
//	         return false;
//			}
//		});
		
		setListAdapter(dataAdapter);
	}

	/**
	 * 长按右键菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.popmenu, menu);

		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		menu.setHeaderTitle(cursor.getString(cursor.getColumnIndex(ENote.column_name_title)));

		Intent intent = new Intent(null, Uri.withAppendedPath(getIntent().getData(), Integer.toString((int) info.id)));
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, new ComponentName(this, NoteListAct.class), null, intent, 0, null);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
		switch (item.getItemId()) {
		case R.id.open:
			Intent intent = new Intent(NoteListAct.this, NoteEditorAct.class);
			intent.setAction(Intent.ACTION_EDIT);
			intent.setData(noteUri);
			startActivityForResult(intent, requestCode_editor);
			return true;
		case R.id.delete:
			Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
			int id = cursor.getInt(cursor.getColumnIndex(ENote.column_name_id));
			enoteService.deleteById(id);
			dataAdapter.changeCursor(enoteService.getAllNoteCursor());
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	/**
	 * 右上方选择菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_option_menu, menu);
		Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NoteListAct.class), null, intent, 0, null);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			Intent intent = new Intent(NoteListAct.this, NoteEditorAct.class);
			intent.setAction(Intent.ACTION_INSERT);
			intent.setData(getIntent().getData());
			startActivityForResult(intent, requestCode_editor);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == requestCode_editor) {
			if(resultCode == RESULT_OK) {
				dataAdapter.changeCursor(enoteService.getAllNoteCursor());
			}
		}
		
	}

}
