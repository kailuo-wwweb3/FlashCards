package com.example.flashcards;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Name
	public static final String DATABASE_NAME = "CardSetsManager";
	public static final String TABLE_CARD_SETS = "TABLE_CARD_SETS";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CARDSETS_TABLE = "CREATE TABLE " + TABLE_CARD_SETS + "("
				+ "card_name" + " text)";
		db.execSQL(CREATE_CARDSETS_TABLE);
		// db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_SETS);
		onCreate(db);
	}

	public void resetTable(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + tableName);
	}

	public void addCardSet(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		// ContentValues values = new ContentValues();
		// values.put("card_name", cardGroup.getName());
		Log.d("actually add!!", name);
		db.execSQL("INSERT INTO " + TABLE_CARD_SETS + " VALUES ('" + name
				+ "')");
		// db.close();
	}

	public void deleteCardSet(String cardName) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DELETE FROM " + TABLE_CARD_SETS + " WHERE card_name = '"
				+ cardName + "'");

		db.execSQL("DROP TABLE IF EXISTS '" + cardName + "'");

		// db.delete(TABLE_CARD_SETS, "card_name = " + cardName, null);

		// db.close();
	}

	public boolean isTableExisting(SQLiteDatabase db, String tableName) {

		if (tableName == null || db == null || !db.isOpen()) {
			return false;
		}
		Cursor cursor = db.rawQuery(
				"SELECT * FROM sqlite_master WHERE name = '" + tableName + "'",
				null);

		if (cursor != null) {
			return cursor.getCount() > 0;
		}
		return false;
	}

	public boolean isCardExisting(SQLiteDatabase db, FlashCard card,
			String cardSetName) {
		String frontString = card.getStringFront();
		String backString = card.getStringBack();
		Cursor cursor = db.rawQuery(
				"SELECT COUNT(*) FROM ? WHERE front = ? AND back = ?",
				new String[] { cardSetName, frontString, backString });
		if (!cursor.moveToFirst()) {
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}

	public void addCardToSet(FlashCard card, String cardSetName) {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean isTableExisting = isTableExisting(db, cardSetName);
		String frontString = card.getStringFront();
		String backString = card.getStringBack();
		ContentValues row = new ContentValues();
		row.put("front", frontString);
		row.put("back", backString);
		if (!isTableExisting) {
			// Create a new table and add the card into it.
			db.execSQL("CREATE TABLE '" + cardSetName
					+ "' (front text, back text)");
			// Insert into card sets table.
			ContentValues newCardSetRow = new ContentValues();
			newCardSetRow.put("card_name", cardSetName);
			db.insert(TABLE_CARD_SETS, null, newCardSetRow);
		}
		// db.insert(cardSetName, null, row);
		db.execSQL("INSERT INTO \"" + cardSetName + "\" VALUES (\""
				+ frontString + "\" , \"" + backString + "\")");
	}

	public void deleteCardFromSet(FlashCard card, String cardSetName) {
		SQLiteDatabase db = this.getWritableDatabase();
		String frontString = card.getStringFront();
		String backString = card.getStringBack();
		db.execSQL("DELETE FROM \"" + cardSetName + "\" WHERE front = \""
				+ frontString + "\" AND " + "back = \"" + backString + "\"");
	}

	public ArrayList<FlashCard> getAllCardFromSet(String cardSetName) {
		ArrayList<FlashCard> cards = new ArrayList<FlashCard>();
		SQLiteDatabase db = this.getWritableDatabase();
		if (!isTableExisting(db, cardSetName)) {
			return cards;
		}
		// String countQuery = "SELECT COUNT(*) FROM '" + cardSetName + "'";
		// Cursor count_cursor = db.rawQuery(countQuery, null);
		// count_cursor.moveToFirst();
		// if (count_cursor.getInt(0) == 0) {
		// return cards;
		// }

		String selectQuery = "SELECT * FROM '" + cardSetName + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			return cards;
		}
		cursor.moveToFirst();

		while (true) {
			String front = cursor.getString(0);
			String back = cursor.getString(1);
			FlashCard card = new FlashCard(front, back);
			cards.add(card);
			if (!cursor.moveToNext()) {
				break;
			}
		}
		return cards;
	}

	public boolean isTableExist(String cardSetName) {

		return isTableExisting(this.getWritableDatabase(), cardSetName);

	}

	public ArrayList<String> getAllCardSetNames() {
		ArrayList<String> cardNames = new ArrayList<String>();
		// Select All Query
		String selectQuery = "SELECT card_name FROM " + TABLE_CARD_SETS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			return cardNames;
		}

		cursor.moveToFirst();
		while (true) {
			cardNames.add(cursor.getString(0));
			if (!cursor.moveToNext()) {
				break;
			}
			// Log.d("count", cursor.getString(0));
		}

		// return contact list
		return cardNames;
	}
}
