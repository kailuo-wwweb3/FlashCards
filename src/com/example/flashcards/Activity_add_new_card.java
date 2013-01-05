package com.example.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Activity_add_new_card extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_card);
		findViewById(R.id.ok_addcard).setOnClickListener(this);
		findViewById(R.id.cancel_addcard).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_new_card, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent result = new Intent();
		switch (v.getId()) {
		case R.id.ok_addcard:
			result.putExtra(Activity_card_view.key_frontName,
					(((EditText) findViewById(R.id.front_editText)).getText()
							.toString()));
			result.putExtra(Activity_card_view.key_backName,
					(((EditText) findViewById(R.id.back_editText)).getText()
							.toString()));
			setResult(Activity.RESULT_OK, result);
			finish();
			break;
		case R.id.cancel_addcard:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;
		default:
			break;
		}

	}
}
