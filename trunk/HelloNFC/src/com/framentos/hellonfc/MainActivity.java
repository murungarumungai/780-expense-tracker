package com.framentos.hellonfc;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the tutorial code downloaded from
 * http://www.framentos.com/en/android-tutorial/2012/07/31/write-hello-world-into-a-nfc-tag-with-a/
 * A few changes were made to this code: 
 * 1. use 3 EditText widgets to take user's input for type, category and price
 * 2. write "application/tracker" mime type to NFC tag
 * 3. write 3 NdefRecords into the NFC tag at once
 */
@SuppressLint({ "ParserError", "ParserError" })
public class MainActivity extends Activity{

	NfcAdapter adapter;
	PendingIntent pendingIntent;
	IntentFilter writeTagFilters[];
	boolean writeMode;
	Tag mytag;
	Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ctx=this;
		Button btnWrite = (Button) findViewById(R.id.button);
		final TextView type = (TextView)findViewById(R.id.edit_type);
		final TextView description = (TextView)findViewById(R.id.edit_description);
		final TextView price = (TextView)findViewById(R.id.edit_price);

		btnWrite.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					if(mytag==null){
						Toast.makeText(ctx, ctx.getString(R.string.error_detected), Toast.LENGTH_LONG ).show();
					}else{
						write(type.getText().toString(),description.getText().toString(),price.getText().toString(),mytag);
						Toast.makeText(ctx, ctx.getString(R.string.ok_writing), Toast.LENGTH_LONG ).show();
					}
				} catch (IOException e) {
					Toast.makeText(ctx, ctx.getString(R.string.error_writing), Toast.LENGTH_LONG ).show();
					e.printStackTrace();
				} catch (FormatException e) {
					Toast.makeText(ctx, ctx.getString(R.string.error_writing) , Toast.LENGTH_LONG ).show();
					e.printStackTrace();
				}
			}
		});

		adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		writeTagFilters = new IntentFilter[] { tagDetected };
		
	}

	

	private void write(String text1, String text2,String text3,Tag tag) throws IOException, FormatException {

		NdefRecord[] records = { createRecord(text1),createRecord(text2),createRecord(text3) };
		NdefMessage  message = new NdefMessage(records);
		// Get an instance of Ndef for the tag.
		Ndef ndef = Ndef.get(tag);
		// Enable I/O
		ndef.connect();
		// Write the message
		ndef.writeNdefMessage(message);
		// Close the connection
		ndef.close();
	}



	private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
		String lang       = "en";
		
		byte[] langBytes  = lang.getBytes("US-ASCII");
		int    langLength = langBytes.length;
		byte[] textBytes  = text.getBytes();
		int    textLength = textBytes.length;
		byte[] payload    = new byte[textLength];

		// set status byte (see NDEF spec for actual bits)
		payload[0] = (byte) langLength;

		// copy langbytes and textbytes into payload
		System.arraycopy(textBytes, 0, payload, 0, textLength);
		String mimeType = "application/tracker";
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
		NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
		return recordNFC;
	}


	@Override
	protected void onNewIntent(Intent intent){
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);    
			Toast.makeText(this, this.getString(R.string.ok_detection) + mytag.toString(), Toast.LENGTH_LONG ).show();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		WriteModeOff();
	}

	@Override
	public void onResume(){
		super.onResume();
		WriteModeOn();
	}

	private void WriteModeOn(){
		writeMode = true;
		adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
	}

	private void WriteModeOff(){
		writeMode = false;
		adapter.disableForegroundDispatch(this);
	}


}