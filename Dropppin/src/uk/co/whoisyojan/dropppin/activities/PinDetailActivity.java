package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.PinDetailFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PinDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_detail);
		long index  = getIntent().getLongExtra("index", 0);
		// Link activity to the form fragment
		PinDetailFragment pdf = PinDetailFragment.newInstance(index);
		//pdf.changeData(index);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.pindetail_activity_layout, pdf, "sx");
		transaction.commit();
	}
	
	

}
