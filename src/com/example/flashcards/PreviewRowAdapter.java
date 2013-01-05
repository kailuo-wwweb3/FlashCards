package com.example.flashcards;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PreviewRowAdapter extends BaseAdapter {
	private Context mContext;
	private List<FlashCard> mCards;

	public PreviewRowAdapter(Context context, List<FlashCard> cards) {
		mContext = context;
		mCards = cards;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCards.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PreviewListRow row = new PreviewListRow(mContext);
		FlashCard card = mCards.get(position);
		row.setLeftText(card.getStringFront());
		row.setRightText(card.getStringBack());
		return row;
	}
}
