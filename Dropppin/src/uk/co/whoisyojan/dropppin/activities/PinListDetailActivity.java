package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.PinDetailFragment;
import uk.co.whoisyojan.dropppin.fragments.PinListFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PinListDetailActivity extends Activity implements
							PinListFragment.Communicator {
		
	FragmentManager manager;
	boolean dualPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_list_detail_layout);
		
		manager = getFragmentManager();
		View detailsFrame = findViewById(R.id.detailFrame);
		dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
		
		PinListFragment plf = (PinListFragment) manager.findFragmentById(R.id.pinListFragment);
		plf.setCommunicator(this);
	}

	@Override
	public void respond(long id) {
		
		if(dualPane) {
			
			PinDetailFragment details = (PinDetailFragment)manager.findFragmentById(R.id.detailFrame);
			if(details == null || details.getShownIndex() != id) {
				details = PinDetailFragment.newInstance(id);
				FragmentTransaction ft = manager.beginTransaction();
				ft.replace(R.id.detailFrame, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		} else {
			Intent intent = new Intent(this, PinDetailActivity.class);
			intent.putExtra("index", id);
			startActivity(intent);
		}
		
		
		
	}


}
