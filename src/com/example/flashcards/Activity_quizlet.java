package com.example.flashcards;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Activity_quizlet extends Activity implements OnClickListener {
	public static final String CLIENT_ID = "HGW7b9Zc9q";
	private static final String RECORDS_PER_LOAD = "50";
	public static final String Key_CardSetTitle = "Key_CardSetTitle";
	public static final String Key_CardSetID = "Key_CardSetID";
	private static final String NO_MORE_RECORDS = "No more records";
	private Button submitButton;
	private EditText search_box;
	private ListView search_result;
	private List<CardRecord> cardRecords;
	private ProgressDialog spinnerDialog;
	private int totalResult;
	private String current_searchURL;

	@Override
	/**
	 * Where the activity starts.
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quizlet);

		submitButton = (Button) findViewById(R.id.search_for_set_button);
		search_box = (EditText) findViewById(R.id.search_for_set);
		search_box.setFocusable(true);

		search_result = (ListView) findViewById(R.id.search_result);
		submitButton.setOnClickListener(this);

		cardRecords = new ArrayList<CardRecord>();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_quizlet, menu);
		return true;
	}

	/**
	 * Handle onClick listener.
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_for_set_button:
			// Reset card records when starting a new search.
			cardRecords = new ArrayList<CardRecord>();

			String queryMessage = search_box.getText().toString();
			String search_url = "https://api.quizlet.com/2.0/search/sets?client_id="
					+ CLIENT_ID
					+ "&q="
					+ queryMessage
					+ "&per_page="
					+ RECORDS_PER_LOAD;
			current_searchURL = search_url;

			startSpinnerDialog();
			new SearchQuizletTask().execute(search_url);
			break;
		default:
			break;
		}
	}

	/**
	 * Helper method to start a spinner dialog with message "Loading...".
	 * Usually can be called before extracting card set data from Quizlet.
	 */
	private void startSpinnerDialog() {
		spinnerDialog = new ProgressDialog(this);
		spinnerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		spinnerDialog.setMessage("Loading...");
		spinnerDialog.show();
	}

	/**
	 * Update the ListView by the card records extracted from Quizlet. Also, add
	 * action listener for each list row.
	 * 
	 * @param cards
	 */
	public void updateListView(List<CardRecord> cards) {
		// Dismiss the sinnper dialog.
		spinnerDialog.dismiss();

		// Create an ArrayAdapter to adapter the list view.
		ArrayAdapter<CardRecord> adapter = new ArrayAdapter<CardRecord>(this,
				android.R.layout.simple_list_item_1, cards);
		search_result.setAdapter(adapter);

		// add click listener for each item on the list.
		search_result
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// Click the bottom to view more card records.
						if (position == cardRecords.size() - 1) {
							CardRecord report = cardRecords.get(position);
							String reportMessage = report.getTitle();
							// If no more records, make it unavailable.
							if (reportMessage.equals(NO_MORE_RECORDS)) {
								// Do nothing.
							} else {
								// click the report to load more card set
								// records.
								int currentProgress = cardRecords.size();
								int loadedPageNum = currentProgress
										/ Integer.valueOf(RECORDS_PER_LOAD);
								String searchMore_url = current_searchURL
										+ "&page=" + (loadedPageNum + 1);
								startSpinnerDialog();
								new SearchQuizletTask().execute(searchMore_url);
							}
						} else {
							// Click to preview the card set.
							@SuppressWarnings("unchecked")
							ArrayAdapter<CardRecord> cardRecordsAdapter = (ArrayAdapter<CardRecord>) parent
									.getAdapter();
							CardRecord cardRecord = cardRecordsAdapter
									.getItem(position);
							String cardSetTitle = cardRecord.getTitle();
							String cardSetID = cardRecord.getId();

							Intent previewCardSetIntent = new Intent(
									Activity_quizlet.this,
									Activity_previewCardSet.class);

							previewCardSetIntent.putExtra(Key_CardSetTitle,
									cardSetTitle);
							previewCardSetIntent.putExtra(Key_CardSetID,
									cardSetID);

							startActivity(previewCardSetIntent);
						}
					}
				});
	}

	private class SearchQuizletTask extends
			AsyncTask<String, Integer, List<CardRecord>> {

		@Override
		protected void onPreExecute() {
			// invoked on the UI thread immediately after the task is executed

			super.onPreExecute();
		}

		@Override
		protected List<CardRecord> doInBackground(String... params) {
			// Parse json here.
			JsonNetworkAdapter jsonNetworkAdapter = new JsonNetworkAdapter();
			String url = params[0];
			List<CardRecord> backup;

			JSONObject response = jsonNetworkAdapter.getJsonData(url);

			try {
				JSONArray sets = response.getJSONArray("sets");
				totalResult = response.getInt("total_results");
				List<CardRecord> records = extractCardRecords(sets);

				return records;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// Log.e("error",
				// "Response from server didn't have a messages key");
				// e.printStackTrace();
				AlertDialog.Builder key_alert_Builder = new AlertDialog.Builder(
						Activity_quizlet.this);
				key_alert_Builder.setMessage("No match result..");
				key_alert_Builder.setCancelable(false);
				key_alert_Builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});

				Dialog dialog = key_alert_Builder.create();
				dialog.show();
				backup = new ArrayList<CardRecord>();

			}
			return backup;
		}

		private List<CardRecord> extractCardRecords(JSONArray searchResult) {

			// if the current search result is null, just return cardRecords.
			if (searchResult == null) {
				return cardRecords;
			}

			// if cardRecords is not empty, then replace the report message by
			// the new search result.
			// Otherwise, just simply add search result onto the cardRecords.
			if (cardRecords.size() != 0) {
				cardRecords.remove(cardRecords.size() - 1);
			}

			for (int i = 0; i < searchResult.length(); i++) {
				try {
					JSONObject cardSetInfo = searchResult.getJSONObject(i);
					CardRecord cardRecord = new CardRecord();
					cardRecord.setId(String.valueOf(cardSetInfo.getInt("id")));
					cardRecord.setTitle(cardSetInfo.getString("title"));
					cardRecords.add(cardRecord);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// Mark to load more in the end of card records;
			CardRecord report = new CardRecord();
			String reportMessage = "";

			// Decrement by 1 because of the report at the very bottom.
			if (cardRecords.size() - 1 < totalResult) {
				reportMessage = cardRecords.size() + " of " + totalResult;
			} else {
				reportMessage = NO_MORE_RECORDS;
			}
			report.setTitle(reportMessage);
			cardRecords.add(report);
			return cardRecords;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<CardRecord> result) {
			super.onPostExecute(result);
			updateListView(result);
		}
	}
}