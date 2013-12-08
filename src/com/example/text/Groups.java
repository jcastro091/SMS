package com.example.text;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressWarnings("deprecation")
public class Groups extends Activity implements OnClickListener, OnItemClickListener {

	private final int REQUEST = 56;
	private ArrayList<String> list;
	private ArrayList<ContactObject> contact_list;
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;
	private EditText editText;
	Cursor cursor;
	Button btnSendSMS;
//	SlidingDrawer slidingDrawer;
//	ToggleButton toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groups);

	//	slidingDrawer = (SlidingDrawer)findViewById(R.id.SlidingDrawer);
		editText = (EditText)findViewById(R.id.message);
//		toggle = (ToggleButton)findViewById(R.id.toggle);
//		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener(){
//			@Override
//			public void onDrawerOpened(){
//				toggle.setChecked(true);
//			}
//		});
//		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener(){
//			@Override
//			public void onDrawerClosed(){
//				toggle.setChecked(false);
//			}
//		});

		list = new ArrayList<String>();
		mListView = (ListView)findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		mListView.setAdapter(mAdapter);

		contact_list = new ArrayList<ContactObject>();
		
		ImageButton b1 = (ImageButton)findViewById(R.id.users);
		b1.setOnClickListener(this);

		btnSendSMS = (Button) findViewById(R.id.save);
		btnSendSMS.setOnClickListener(this);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			switch(requestCode) {
			case REQUEST:
				ContactObject contact = (ContactObject) data.getSerializableExtra("contact");
				System.out.println("Added " + contact);
				list.add(contact.getName());
				contact_list.add(contact);
				mAdapter.notifyDataSetChanged();
				break;
			}
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cursor != null)
			cursor.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.users:
			//	list.add("16462032852");
			//	mAdapter.notifyDataSetChanged();
			Intent contactIntent = new Intent(Groups.this, ContactListActivity.class);
			startActivityForResult(contactIntent, REQUEST);
			break;
		case R.id.save:
			for(int i = 0; i < contact_list.size(); i++){
				String phoneNo = contact_list.get(i).getPhoneNo();
				String message = editText.getText().toString();               
				if (phoneNo.length()>0 && message.length()>0) {                
					//	sendSMS(phoneNo, message);
					GroupObject group = new GroupObject(
							((EditText)findViewById(R.id.name_group)).getText().toString(),
							((EditText)findViewById(R.id.message)).getText().toString(),
							contact_list);
					Intent newIntent = new Intent();
					newIntent.putExtra("group", group);
					setResult(RESULT_OK, newIntent);
					finish();
				} else
					Toast.makeText(getBaseContext(), 
							"Please enter both phone number and message.", 
							Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
}