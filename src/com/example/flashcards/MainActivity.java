package com.example.flashcards;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
	private Button flashCardsButton;
	private Button quizLetButton;
	private Button aboutUsButton;
	private Button exitButton;
	private ImageView facebook;
	private ImageView google;
	private View email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		flashCardsButton = (Button) findViewById(R.id.flashcards_button);
		quizLetButton = (Button) findViewById(R.id.quizlet_button);
		aboutUsButton = (Button) findViewById(R.id.about_us_button);
		exitButton = (Button) findViewById(R.id.about_us_button);
		facebook = (ImageView) findViewById(R.id.facebook);
		google = (ImageView) findViewById(R.id.google);
		email = (View) findViewById(R.id.view1);

		flashCardsButton.setOnClickListener(this);
		quizLetButton.setOnClickListener(this);
		aboutUsButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		facebook.setOnClickListener(this);
		google.setOnClickListener(this);
		email.setOnClickListener(this);

		// getApplication().deleteDatabase(DatabaseHandler.DATABASE_NAME);
		// DatabaseHandler db = new
		// DatabaseHandler(this.getApplicationContext());
		// FlashCardGroup english = new FlashCardGroup("english", new
		// ArrayList<FlashCard>());
		// FlashCardGroup spanish = new FlashCardGroup("spanish", new
		// ArrayList<FlashCard>());
		// FlashCardGroup chinese = new FlashCardGroup("chinese", new
		// ArrayList<FlashCard>());
		// FlashCardGroup japanese = new FlashCardGroup("japanese", new
		// ArrayList<FlashCard>());
		// db.addCardSet(english);
		// db.addCardSet(spanish);
		// db.addCardSet(chinese);
		// db.addCardSet(japanese);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.flashcards_button:
			Intent flashCardIntent = new Intent(this,
					Activity_flash_card_list.class);
			startActivity(flashCardIntent);
			break;
		case R.id.quizlet_button:
			Intent quizletIntent = new Intent(this, Activity_quizlet.class);
			startActivity(quizletIntent);
			break;
		case R.id.about_us_button:
			// Implement about us view.
			Intent aboutUsIntent = new Intent(MainActivity.this,
					Activity_aboutus.class);
			startActivity(aboutUsIntent);

			break;
		case R.id.facebook:
			Intent facebookIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://m.facebook.com/sharer.php?u=http://www.google.com"));
			startActivity(facebookIntent);
			break;
		case R.id.google:
			// share this app on google+
			// Launch the Google+ share dialog with attribution to your app.
			Intent googlePlusIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://plus.google.com/share?url=www.google.com"));
			startActivity(googlePlusIntent);

			break;
		case R.id.view1:
			// open an email application.
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);

			/* Fill it with Data */
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { "to@email.com" });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Subject");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
			/* Send it off to the Activity-Chooser */
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));

		default:
			break;
		}

	}
}
