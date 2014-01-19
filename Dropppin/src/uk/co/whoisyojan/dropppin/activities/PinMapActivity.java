package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.PinMapFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PinMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_map);
		long index  = getIntent().getLongExtra("index", 0);

		PinMapFragment pmf = PinMapFragment.newInstance(index);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.pinmap_activity_layout, pmf, "sx");
		transaction.commit();
	}

}
