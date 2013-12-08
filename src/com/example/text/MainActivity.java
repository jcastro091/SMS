package com.example.text;


import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener
{
	private ViewFlipper viewFlipper;
	private float lastX;
	private final int REQUEST = 23;
	private ArrayList<String> list;
	private ArrayList<GroupObject> group_list;
	private ListView mListView;
	private ArrayAdapter<String> mAdapter;

	

    @SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);      
        this.findViewById( R.id.UpdateList ).setOnClickListener( this );
		list = new ArrayList<String>();
		group_list = new ArrayList<GroupObject>();
		mListView = (ListView)findViewById(R.id.listView1);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		mListView.setEmptyView(findViewById(R.id.empty));
		mListView.setAdapter(mAdapter);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		
        
        
        
        
        ImageButton b2 = (ImageButton)findViewById(R.id.logo);
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent newIntent = new Intent(MainActivity.this, Groups.class);
 			startActivityForResult(newIntent, REQUEST);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST) {
				GroupObject contact = (GroupObject) data.getSerializableExtra("group");
				System.out.println("Category Added " + contact);
				list.add(contact.getName());
				group_list.add(contact);
				mAdapter.notifyDataSetChanged();
				
				SharedPreferences mPrefs = getSharedPreferences("MajestykTextApp", Context.MODE_WORLD_READABLE);
				for(ContactObject ind_contact : contact.getContacts()) {
					Log.i("Testing", ind_contact.getPhoneNo()
							.replace(" ", "").replace("-",  ""));
					mPrefs.edit().putString(ind_contact.getPhoneNo()
							.replace(" ", "").replace("-",  ""),
							contact.getMessage()).commit();
				}
			}
		}
	}

	public void onClick1(View v) {
		Intent result = new Intent(MainActivity.this, Groups.class);
		startActivity(result);
		result.putExtra("contact", list);
		this.setResult(RESULT_OK, result);
		finish();
}
    

    ArrayList<String> smsList = new ArrayList<String>();
    
	public void onItemClick( AdapterView<?> parent, View view, int pos, long id ) 
	{
		try 
		{
		    	String[] splitted = smsList.get( pos ).split("\n"); 
			String sender = splitted[0];
			String encryptedData = "";
			for ( int i = 1; i < splitted.length; ++i )
			{
			    encryptedData += splitted[i];
			}
			String data = sender + "\n" + StringCryptor.decrypt( new String(SmsReceiver.PASSWORD), encryptedData );
			Toast.makeText( this, data, Toast.LENGTH_SHORT ).show();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onClick( View v ) 
	{
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);

		int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
		int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );
		
		if ( indexBody < 0 || !cursor.moveToFirst() ) return;
		
		smsList.clear();
		
		do
		{
			String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
			smsList.add( str );
		}
		while( cursor.moveToNext() );

		
		ListView smsListView = (ListView) findViewById( R.id.SMSList );
		smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
		smsListView.setOnItemClickListener( this );
	}

	/////////////////FLIP VIEW////////////////////////////////////////
	// Method to handle touch event like left to right swap and right to left swap
	@Override
	public boolean onTouchEvent(MotionEvent touchevent) 
	{
		switch (touchevent.getAction())
		{
		// when user first touches the screen to swap
		case MotionEvent.ACTION_DOWN: 
		{
			lastX = touchevent.getX();
			break;
		}
		case MotionEvent.ACTION_UP: 
		{
			float currentX = touchevent.getX();

			// if left to right swipe on screen
			if (lastX < currentX) 
			{
				// If no more View/Child to flip
				if (viewFlipper.getDisplayedChild() == 0)
					break;

				// set the required Animation type to ViewFlipper
				// The Next screen will come in form Left and current Screen will go OUT from Right 
				viewFlipper.setInAnimation(this, R.anim.in_from_left);
				viewFlipper.setOutAnimation(this, R.anim.out_to_right);
				// Show the next Screen
				viewFlipper.showNext();
			}

			// if right to left swipe on screen
			if (lastX > currentX)
			{
				if (viewFlipper.getDisplayedChild() == 1)
					break;
				// set the required Animation type to ViewFlipper
				// The Next screen will come in form Right and current Screen will go OUT from Left 
				viewFlipper.setInAnimation(this, R.anim.in_from_right);
				viewFlipper.setOutAnimation(this, R.anim.out_to_left);
				// Show The Previous Screen
				viewFlipper.showPrevious();
			}
			break;
		}
		}
		return false;
	}}
