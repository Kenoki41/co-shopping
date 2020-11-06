package comp5216.sydney.edu.au.a5216login.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import comp5216.sydney.edu.au.a5216login.R;


public class AddListActivity extends Activity
{
	public int position = 0;
	EditText etItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.dialog_create_new_list);
		
		//Get the data from the main screen
		String editItem = getIntent().getStringExtra("item");
		position = getIntent().getIntExtra("position",-1);
		
		// show original content in the text field
		etItem = (EditText)findViewById(R.id.createListName);
		etItem.setText(editItem);
	}

	public void onSubmit(View v) {
		etItem = (EditText) findViewById(R.id.createListName);

		// Prepare data intent for sending it back
		Intent data = new Intent();

		// Pass relevant data back as a result
		data.putExtra("item", etItem.getText().toString());
		data.putExtra("position", position);

		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}

	public void onCancel(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(AddListActivity.this);
		builder.setTitle(R.string.dialog_cancel_edit_list_title)
				.setMessage(R.string.dialog_cancel_msg)
				.setPositiveButton(R.string.yes, new
						DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogInterface, int i) {
								etItem = (EditText) findViewById(R.id.etEditListName);

								//Prepare data intent for sending it back
								Intent data = new Intent();
								setResult(RESULT_CANCELED, data); // set result code and bundle data
								// for response
								finish(); // closes the activity, pass data to parent
							}
						})
				.setNegativeButton(R.string.no, new
						DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogInterface, int i) {

							}
						});
		builder.create().show();
	}
}
