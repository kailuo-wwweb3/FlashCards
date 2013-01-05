package com.example.flashcards;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class Activity_flash_card_list extends Activity implements
		OnClickListener {
	public static final String key_cardSetsNames = "key_cardSetsNames";
	private TableLayout tableLayout;
	private Button addButton;
	public static final String key_NewCardGroupName = "key_NewCardGroupName";
	public static final String key_EditCardGroupName = "key_EditCardGroupName";
	public static final String key_clickedCardSet = "key_clickedCardSet";
	private DatabaseHandler db;
	private ListView mListView;
	private EditText newCardSetEditText;
	private ArrayList<String> names;
	private ArrayAdapter<String> namesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash_card_list);
		addButton = (Button) findViewById(R.id.add_new_card_set_button);
		addButton.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.card_set_list_view);
		newCardSetEditText = (EditText) findViewById(R.id.add_new_card_set_editText);

		db = new DatabaseHandler(this.getApplicationContext());
		names = db.getAllCardSetNames();
		namesAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names);
		mListView.setAdapter(namesAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent cardIntent = new Intent(Activity_flash_card_list.this,
						Activity_card_view.class);
				@SuppressWarnings("unchecked")
				ArrayAdapter<String> namesAdapter = (ArrayAdapter<String>) parent
						.getAdapter();
				cardIntent.putExtra(key_clickedCardSet,
						namesAdapter.getItem(position));
				startActivity(cardIntent);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				deleteName(position);
				return false;
			}

		});

	}

	protected void deleteName(final int position) {
		// Delete from database

		AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
		confirmBuilder.setCancelable(true);
		confirmBuilder.setMessage("Do you want to delete "
				+ names.get(position));
		confirmBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deleteCardSet(names.get(position));
						names.remove(position);
						namesAdapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				});

		confirmBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		confirmBuilder.create().show();

	}

	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		CharSequence[] charSequenceArray = {};
		for (int i = 0; i < this.tableLayout.getChildCount(); i++) {
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			TextView text = (TextView) row.getChildAt(0);
			String name = text.getText().toString();
			charSequenceArray[i] = name.subSequence(0, name.length());
		}
		startSearch("eee", false, bundle, false);
		return super.onSearchRequested();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_new_card_set_button:
			// Add new card set into the list view.
			if (newCardSetEditText.getText().toString() != "") {
				names.add(newCardSetEditText.getText().toString());
				db.addCardSet(newCardSetEditText.getText().toString());
				namesAdapter.notifyDataSetChanged();
			}
			break;
		}
	}

}