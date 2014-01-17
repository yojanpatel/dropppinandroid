package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.PinFormFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PinFormActivity extends Activity {

	private static final String PIN_FORM_TAG = "PIN_FORM_FRAGMENT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_form);
		
		// Link activity to the form fragment
		PinFormFragment pff = new PinFormFragment();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.pinform_activity_layout, pff, PIN_FORM_TAG);
		transaction.commit();

	}
	


	

}
