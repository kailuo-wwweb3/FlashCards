package com.example.flashcards;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity_editCardSet extends Activity implements OnClickListener {
	private Button ok_button;
	private Button cancel_button;
	private EditText editGroupName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_card_set);
		ok_button = (Button) findViewById(R.id.ok1);
		cancel_button = (Button) findViewById(R.id.cancel1);
		ok_button.setOnClickListener(this);
		cancel_button.setOnClickListener(this);
		editGroupName = (EditText) findViewById(R.id.editGroupName);
		Intent intent = this.getIntent();
		String cardGroupName = intent
				.getStringExtra(Activity_flash_card_list.key_EditCardGroupName);
		editGroupName.setText(cardGroupName);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_card_set, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent result = new Intent();
		switch (v.getId()) {
		case R.id.ok1:
			result.putExtra(Activity_flash_card_list.key_EditCardGroupName,
					editGroupName.getText().toString());
			setResult(Activity.RESULT_OK, result);
			finish();
			break;
		case R.id.cancel1:
			setResult(Activity.RESULT_CANCELED, result);
			finish();
			break;
		default:
			break;
		}

	}
}
