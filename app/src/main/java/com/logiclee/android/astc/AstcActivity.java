package com.logiclee.android.astc;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.logiclee.android.astc.ServiceInvoice.ServiceQualityEnum;

/**
 * ASTC is a simple tip calculator and the first Android app I have ever written.
 * ASTC implements an Android activity that is the main screen of a tip calculator application.
 * @author Nigel C. Lee
 * Version History:
 *   1.0.0: 3/25/2013 First published to the Google Play Store.
 *   1.0.1: 3/14/2015 No functional changes.  Source code imported from Eclipse to Android Studio
 *   and published to GitHub.
 */
public class AstcActivity extends Activity {
	ServiceInvoice bill;
	private static long ZERO = (long) 0.0;
	EditText etView;
	TextView tView;
	RadioGroup rgView;
	RatingBar rbView;
	float amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_tip_calc);
		//Log.i(getString(R.string.debug_message_tag), "Start of method SimpleTipCalcActivity.onCreate");
		bill = new ServiceInvoice();
		addListenerOnBillAmount();
		addListenerOnRatingBar();
		updateScreenFields();	
		if (android.os.Build.VERSION.SDK_INT < 14) { 				//SDK v14 is Android 4.0 (ICS)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Lock portrait mode
		}
	}
	
	/**
	 * Listener for bill amount
	 */
	public void addListenerOnBillAmount() {
		etView = (EditText) findViewById(R.id.billAmount);
		etView.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				//Log.i(getString(R.string.debug_message_tag), "Start of method afterTextChanged");
				try { amount = Float.parseFloat(etView.getText().toString()); }
				catch (Exception e) { amount = 0.0F; }
				bill.setBillAmount(amount); 
				updateScreenFields();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
		});			
	}
	
	/**
	 * Listener for rating bar
	 */
	public void addListenerOnRatingBar() {
		//Log.i(getString(R.string.debug_message_tag), "Start of method addListenerOnRatingBar");
		rbView = (RatingBar) findViewById(R.id.ratingServiceQuality);
		rbView.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar bar, float sq, boolean userChanged) {
				// TODO Auto-generated method stub
				//Log.i(getString(R.string.debug_message_tag), "Start of method addListenerOnRatingBar.onRatingChanged");

				//RatingBar rBar = (RatingBar) findViewById(view.getId());
				//Log.i(getString(R.string.debug_message_tag), Float.toString(sq));

				if (sq == 1.0F) {
					bill.setServiceQuality(ServiceQualityEnum.ONESTAR);
				} else if (sq == 2.0F) {
					bill.setServiceQuality(ServiceQualityEnum.TWOSTAR);
				} else if (sq == 4.0F) {
					bill.setServiceQuality(ServiceQualityEnum.FOURSTAR);
				} else if (sq == 5.0F) { 
					bill.setServiceQuality(ServiceQualityEnum.FIVESTAR);
				} else {
					bill.setServiceQuality(ServiceQualityEnum.THREESTAR);
				}
				updateScreenFields();				
			}
		});
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.simple_tip_calc, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_exit:
	        	finish();
	        	System.exit(0);
	            return true;
	        case R.id.action_about:
	        	try { 
	        		PackageInfo pkg = getPackageManager().getPackageInfo(getPackageName(), 0);
	        		displayToast (getString(R.string.app_name) + " " 
	        				+ pkg.versionName + " by " 
	        				+ getString(R.string.app_author));
	        	} catch (Exception e) { 
	        		displayToast(getString(R.string.app_name));
	        		}  
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Clear the screen fields
	 */
	public void clearScreenFields() {
		//Log.i(getString(R.string.debug_message_tag), "Start of method reset");
		bill.setBillAmount(ZERO);
		setTextViewAmount(R.id.billAmount, ZERO);
		setTextViewAmount(R.id.tipAmount, ZERO);
		setTextViewAmount(R.id.totalAmount, ZERO);
	}

	public void setTextViewAmount(int id, float value) {
		TextView view = (TextView) findViewById(id);
		view.setText(String.format("$%.2f", value));
	}	
	
	public void displayToast(CharSequence text) {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}
	
	/**
	 * Update screen fields from bill
	 */
	public void updateScreenFields() {
		TextView view = (TextView) findViewById(R.id.tipPercentLabel);
		float perc = bill.getTipPercentage()*100;
		if (perc <= 0.0F) { 
			view.setText("");
		} else {
			String percString = "[ " + String.format("%2.0f", perc) + "% ]";
			view.setText(percString);
		}
		
		setTextViewAmount(R.id.tipAmount, bill.getTipAmount());
		setTextViewAmount(R.id.totalAmount, bill.getBillTotal());
	}
	
}
