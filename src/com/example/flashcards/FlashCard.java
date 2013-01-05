package com.example.flashcards;

import java.util.ArrayList;

public class FlashCard {
	private String frontSideString;
	private String backSideString;

	public FlashCard() {
		this.frontSideString = "";
		this.backSideString = "";
	}

	public FlashCard(String frontSideString, String backSideString) {
		this.frontSideString = frontSideString;
		this.backSideString = backSideString;
	}

	public String getStringFront() {
		return this.frontSideString;
	}

	public String getStringBack() {
		return this.backSideString;
	}

	public boolean hasNext(ArrayList<FlashCard> cardGroup) {
		if (cardGroup.indexOf(this) < cardGroup.size() - 1) {
			return true;
		} else {
			return false;
		}
	}

	public FlashCard next(ArrayList<FlashCard> cardGroup) {
		if (this.hasNext(cardGroup)) {
			return cardGroup.get(cardGroup.indexOf(this) + 1);
		} else {
			return null;
		}
	}

	public void setStringFront(String frontSideString) {
		this.frontSideString = frontSideString;
	}

	public void setStringBack(String backSideString) {
		this.backSideString = backSideString;
	}

}
