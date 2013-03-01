package com.eddy.enote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
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
import android.widget.SimpleAdapter;

import com.eddy.enote.note.ENoteColumn;

public class NoteListAct extends ListActivity {

	private static final String TAG = "NoteListAct";
	private static final int requestCode_editor = 1;

	ArrayList<Map<String, String>> data;
	SimpleAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		getListView().setOnCreateContextMenuListener(this);

		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(ENoteColumn.Content_URI);
		}

		data = new ArrayList<Map<String, String>>();
		Map<String, String> itemdata = new HashMap<String, String>();
		itemdata.put("title", "ttt11111");
		itemdata.put("createTime", "2013-01-01");
		data.add(itemdata);

		itemdata = new HashMap<String, String>();
		itemdata.put("title", "ttt222");
		itemdata.put("createTime", "2013-02-01");
		data.add(itemdata);

		dataAdapter = new SimpleAdapter(this, data, R.layout.enote_item, new String[] { "title", "createTime" }, new int[] { R.id.title, R.id.createTime });
		setListAdapter(dataAdapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.popmenu, menu);

		String title = data.get((int) info.id).get("title");
		menu.setHeaderTitle(title);

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
			data.remove((int) info.id);
			dataAdapter.notifyDataSetChanged();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == requestCode_editor) {
			
		}
		
	}

}
