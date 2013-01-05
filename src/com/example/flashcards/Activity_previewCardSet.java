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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_previewCardSet extends Activity {
	// Preview list that be showed on UI.
	private ListView previewList;
	// the name of the current card set.
	private TextView cardset_title;
	// sqlite database.
	private DatabaseHandler db;
	// a list of FlashCard to store the result from parsing json.
	private List<FlashCard> mCards;

	// dialogs..
	private static final int ALERT_DIALOG = 1;
	private ProgressDialog spinnerDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set up variables.
		db = new DatabaseHandler(this.getApplicationContext());
		setContentView(R.layout.activity_preview_card_set);
		previewList = (ListView) findViewById(R.id.preview_list);
		cardset_title = (TextView) findViewById(R.id.cardset_title);

		Intent intent = this.getIntent();
		String title = intent.getStringExtra(Activity_quizlet.Key_CardSetTitle);
		String id = intent.getStringExtra(Activity_quizlet.Key_CardSetID);
		cardset_title.setText(title);
		String card_set_info_url = "https://api.quizlet.com/2.0/sets/" + id
				+ "&client_id=" + Activity_quizlet.CLIENT_ID;

		spinnerDialog = new ProgressDialog(this);
		spinnerDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		spinnerDialog.setMessage("Loading...");
		spinnerDialog.show();
		// Start pulling all card info.
		new PreviewCardSetTask().execute(card_set_info_url);
	}

	/**
	 * update the preview list view on UI thread.
	 * 
	 * @param cards
	 */
	public void updateResultListView(List<FlashCard> cards) {
		spinnerDialog.dismiss();
		PreviewRowAdapter adapter = new PreviewRowAdapter(this, cards);
		previewList.setAdapter(adapter);
		mCards = cards;
	}

	public void notifyResult(boolean success) {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		if (success) {
			alertBuilder.setMessage("Download completed!");
		} else {
			alertBuilder.setMessage("Download failed!");
		}
		alertBuilder.setIcon(R.drawable.complete);
		alertBuilder.setCancelable(false);
		alertBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		Dialog dialog = alertBuilder.create();
		dialog.show();
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		switch (id) {
		case ALERT_DIALOG:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setMessage("Download completed!");
			alertBuilder.setIcon(R.drawable.complete);
			alertBuilder.setCancelable(false);
			alertBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			dialog = alertBuilder.create();
			break;

		default:
			return super.onCreateDialog(id);
		}
		return dialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_preview_card_set, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_download:

			String cardSetName = cardset_title.getText().toString();

			// add flash card into local sqlite database.
			for (FlashCard card : mCards) {
				db.addCardToSet(card, cardSetName);
			}
			// notify if the card set is already stored into the database.
			notifyResult(db.isTableExist(cardSetName));
			break;
		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private class PreviewCardSetTask extends
			AsyncTask<String, Integer, List<FlashCard>> {

		@Override
		protected List<FlashCard> doInBackground(String... params) {
			// get url parameter.
			String url = params[0];
			// set up the json adapter.
			JsonNetworkAdapter jsonAdapter = new JsonNetworkAdapter();
			JSONObject response = jsonAdapter.getJsonData(url);

			try {
				// grab all josn array with key = terms.
				JSONArray cardsInfo = response.getJSONArray("terms");

				return populateCards(cardsInfo);
			} catch (JSONException e) {
				Log.e("error", "No such key");
				e.printStackTrace();
			}

			return null;
		}

		// Parse json array into a list of flash cards.
		private List<FlashCard> populateCards(JSONArray cardsInfo) {
			List<FlashCard> search_results = new ArrayList<FlashCard>();
			for (int i = 0; i < cardsInfo.length(); i++) {
				FlashCard card = new FlashCard();
				JSONObject item;
				try {
					item = cardsInfo.getJSONObject(i);
					card.setStringFront(item.getString("term"));
					card.setStringBack(item.getString("definition"));
					search_results.add(card);

				} catch (JSONException e) {
					 Log.e("error", "No such key");
					 e.printStackTrace();
				}
			}
			return search_results;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(List<FlashCard> result) {

			// update the list view on UI thread.
			updateResultListView(result);
		}
	}

}
