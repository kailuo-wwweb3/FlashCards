package com.example.flashcards;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class Activity_card_view extends Activity implements OnClickListener {

	// the current FlashCard which is showed.
	private FlashCard currentCard;
	// the current word showed on the current card.
	private String currentWord;
	
	// Keys for adding new card.
	public static final String key_frontName = "key_frontName";
	public static final String key_backName = "key_backName";
	// request code for adding card.
	public static final int REQUEST_ADDINGCARD = 0;
	// an array list of FlashCard to store all the cards in the current card
	// set.
	private ArrayList<FlashCard> flashCards;
	// SQLite database management.
	private DatabaseHandler db;
	// the current card set name.
	private String cardSetName;
	// the progress view on the top shows the current progress of studying.
	private TextView progressView;
	// check if the current view is the front image or not.
	private boolean isFirstImage;
	// front and back images for 3D animation flipping.
	private Button frontImage;
	private Button backImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_view);
		isFirstImage = true;

		frontImage = (Button) findViewById(R.id.ImageView01);
		backImage = (Button) findViewById(R.id.ImageView02);

		backImage.setVisibility(View.GONE);

		frontImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isFirstImage) {
					applyRotation(0, 90);
					isFirstImage = !isFirstImage;

				} else {
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				}
			}
		});

		backImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFirstImage) {
					applyRotation(0, 90);
					isFirstImage = !isFirstImage;
				} else {
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				}
			}
		});
		findViewById(R.id.add_new_card).setOnClickListener(this);
		findViewById(R.id.go_next).setOnClickListener(this);
		findViewById(R.id.go_prev).setOnClickListener(this);
		findViewById(R.id.close_button).setOnClickListener(this);
		db = new DatabaseHandler(this.getApplicationContext());
		cardSetName = this.getIntent().getStringExtra(
				Activity_flash_card_list.key_clickedCardSet);
		progressView = (TextView) findViewById(R.id.progress_text_view);

		// Grab all flashcards from database;

		this.flashCards = db.getAllCardFromSet(cardSetName);

		if (this.flashCards.size() != 0) {
			currentCard = this.flashCards.get(0);
			currentWord = currentCard.getStringFront();

			frontImage.setText(currentWord);
			backImage.setText(currentCard.getStringBack());
			// currentCardButton.setText(currentWord);
		} else {
			currentWord = "";
			frontImage.setText("");
			backImage.setText("");
		}
		updateProgress();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_activity_card_view, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int currentIndex = this.flashCards.indexOf(currentCard);
		Log.d("Index", String.valueOf(currentIndex));
		switch (v.getId()) {
		case R.id.add_new_card:
			String frontName = null;
			String backName = null;
			Intent addNewCardIntent = new Intent(this,
					Activity_add_new_card.class);
			addNewCardIntent.putExtra(key_frontName, frontName);
			addNewCardIntent.putExtra(key_backName, backName);
			startActivityForResult(addNewCardIntent, REQUEST_ADDINGCARD);
			break;

		case R.id.go_next:
			Log.d("aaa", "go next");
			// If the current card is not the last card, then set the current
			// card to be the next card
			// on the deck.
			if (this.flashCards.size() == 0) {
				break;
			}

			if (currentIndex < this.flashCards.size() - 1) {
				currentCard = this.flashCards.get(currentIndex + 1);
				currentWord = currentCard.getStringFront();

			} else {
				// do nothing
			}
			// set both front and back image.
			frontImage.setText(currentWord);
			backImage.setText(currentCard.getStringBack());
			// update progress text.
			updateProgress();
			break;

		case R.id.go_prev:
			Log.d("aaa", "go prev");
			// If the current card is not the first card, then set the current
			// card to be the previous card
			// on the deck.

			if (this.flashCards.size() == 0) {
				break;
			}
			if (currentIndex > 0) {
				currentCard = this.flashCards.get(currentIndex - 1);
				currentWord = currentCard.getStringFront();
			} else {
				// do nothing
			}
			frontImage.setText(currentWord);
			backImage.setText(currentCard.getStringBack());
			// update progress text.
			updateProgress();
			break;

		case R.id.close_button:
			// If there is no card any more in this.flashCards, exit the
			// activity.
			if (this.flashCards.size() == 0) {
				finish();
				break;
			}
			// if there is only one card in this.flashCard, remove it from
			// this.flashCard and the sqlite database and exit the
			// activity.
			if (this.flashCards.size() == 1) {
				this.flashCards.remove(0);
				db.deleteCardFromSet(currentCard, cardSetName);
				finish();
				break;
				// if there are more than one card in the deck...
			} else {

				// Remove current card from database.
				db.deleteCardFromSet(currentCard, cardSetName);
				if (currentIndex == 0) {
					this.flashCards.remove(0);
					currentCard = this.flashCards.get(0);
					// if deleting the last card, then set the current card to
					// be the (length - 2)th card.
				} else if (currentIndex == this.flashCards.size() - 1) {
					this.flashCards.remove(this.flashCards.size() - 1);
					currentCard = this.flashCards
							.get(this.flashCards.size() - 1);
					// otherwise, set the current card to be the next card on
					// the deck.
				} else {
					this.flashCards.remove(currentCard);
					currentCard = this.flashCards.get(currentIndex);
				}
				// set current word.
				currentWord = currentCard.getStringFront();
				// set both front and back images.
				frontImage.setText(currentWord);
				backImage.setText(currentCard.getStringBack());
				// Update the progress text.
				updateProgress();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Update the progress text on the top.
	 */
	private void updateProgress() {
		progressView.setText((flashCards.indexOf(currentCard) + 1) + "/"
				+ (flashCards.size()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ADDINGCARD) {
			if (resultCode == Activity.RESULT_OK) {
				String frontSide = data.getStringExtra(key_frontName);
				String backSide = data.getStringExtra(key_backName);
				// add new card to the flashCards
				FlashCard newCard = new FlashCard(frontSide, backSide);
				this.flashCards.add(newCard);
				currentCard = newCard;
				currentWord = currentCard.getStringFront();
				frontImage.setText(currentWord);
				backImage.setText(currentCard.getStringBack());
				// add new card to the database
				db.addCardToSet(newCard, cardSetName);
				// Update the progress text.
				updateProgress();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void applyRotation(float start, float end) {
		// Find the center of image
		final float centerX = frontImage.getWidth() / 2.0f;
		final float centerY = frontImage.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3DAnimation rotation = new Flip3DAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage,
				frontImage, backImage));
		if (isFirstImage) {
			frontImage.startAnimation(rotation);
		} else {
			backImage.startAnimation(rotation);
		}
	}
}