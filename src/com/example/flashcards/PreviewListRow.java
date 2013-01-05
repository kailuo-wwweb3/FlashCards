package com.example.flashcards;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreviewListRow extends LinearLayout {
	private TextView mLeftTextView;
	private TextView mRightTextView;

	public PreviewListRow(Context context) {
		super(context);

		this.setOrientation(HORIZONTAL);
		mLeftTextView = new TextView(context);
		mRightTextView = new TextView(context);

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		inflater.inflate(R.layout.preview_row_view, this);
		mLeftTextView = (TextView) findViewById(R.id.left_text_view);
		mRightTextView = (TextView) findViewById(R.id.right_text_view);

		// addView(mLeftTextView);
		// addView(mRightTextView);
	}

	public void setLeftText(String text) {
		mLeftTextView.setText(text);
	}

	public void setRightText(String text) {
		mRightTextView.setText(text);
	}

}
