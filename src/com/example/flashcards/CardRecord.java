package com.example.flashcards;

public class CardRecord {
	private String id;
	private String title;

	public CardRecord() {
		// Construct a card record without doing anything.
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setTitle(String title) {
		// TODO Auto-generated method stub
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.title;
	}


}
