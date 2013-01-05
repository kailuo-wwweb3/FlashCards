package com.example.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Activity_search extends Activity implements OnClickListener {
	private TableLayout tableLayout;
	private DatabaseHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		tableLayout = (TableLayout) findViewById(R.id.search_results);
		db = new DatabaseHandler(this.getApplicationContext());

		// Get the intent, verify the action and get the query.
		Intent intent = this.getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	private void doMySearch(String query) {
		// TODO Auto-generated method stub

		for (String name : db.getAllCardSetNames()) {
			if (name.contains(query)) {
				TextView textView = new TextView(this);
				TableRow tableRow = new TableRow(this);
				tableRow.addView(textView);
				tableLayout.addView(tableRow);
				TableRow newCardGroup = (TableRow) getLayoutInflater().inflate(
						R.layout.tablerow, null);
				TextView child = new TextView(this);
				child.setText(name);
				newCardGroup.addView(child);
				TableRow.LayoutParams rel_btn = new TableRow.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				rel_btn.topMargin = 40;
				newCardGroup.setLayoutParams(rel_btn);
				tableLayout.addView(newCardGroup);
				newCardGroup.setOnClickListener(this);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_search, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		Intent cardIntent = new Intent(this, Activity_card_view.class);
		startActivity(cardIntent);
	}
}
