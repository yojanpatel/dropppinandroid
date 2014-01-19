package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.PinListFragment;
import uk.co.whoisyojan.dropppin.fragments.PinMapFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PinListMapActivity extends Activity implements
							PinListFragment.Communicator {
			
	FragmentManager manager;
	PinMapFragment mapFrag;
	
	boolean dualPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pin_list_map_layout);
		
		manager = getFragmentManager();
		
		PinListFragment plf = (PinListFragment) manager.findFragmentById(R.id.pinListFragment);
		plf.setCommunicator(this);
		
		View detailsFrame = findViewById(R.id.mapFrame);
		dualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;	
		
		if(dualPane) {
			mapFrag = (PinMapFragment)manager.findFragmentById(R.id.mapFrame);
			if(mapFrag == null ) {
				mapFrag = new PinMapFragment();
				FragmentTransaction ft = manager.beginTransaction();
				ft.replace(R.id.mapFrame, mapFrag).commit();
			}	
		}
		
	}
	
	@Override
	public void respond(long id) {
		Log.d("Respond", "Called");
		if(dualPane) {
			if(mapFrag != null) {
				mapFrag.goToPoint(id);
			}
		} else {
			Intent intent = new Intent(this, PinMapActivity.class);
			intent.putExtra("index", id);
			startActivity(intent);
		}
	}

}
