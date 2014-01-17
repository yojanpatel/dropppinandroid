package uk.co.whoisyojan.dropppin.fragments;

import uk.co.whoisyojan.dropppin.LocationUtil;
import uk.co.whoisyojan.dropppin.PinDatabaseAdapter;
import uk.co.whoisyojan.dropppin.R;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PinFormFragment extends Fragment implements OnClickListener, LocationUtil.Communicator {
	
	PinDatabaseAdapter dbAdapter;
	
	Context context;
	
	EditText title;
	EditText desc;
	EditText address;
	
	Location location;
	LocationUtil locationUtil;
	
	public static final int LOCATION_REQUEST = 9800;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		return inflater.inflate(R.layout.pin_form_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		dbAdapter = new PinDatabaseAdapter(getActivity());
		
		title = (EditText)getView().findViewById(R.id.pinTitle);
		desc = (EditText)getView().findViewById(R.id.pinDescription);
		address = (EditText)getView().findViewById(R.id.pinLocation);
		
		Button submitBtn = (Button)getView().findViewById(R.id.pinSubmit);
		submitBtn.setOnClickListener(this);
		Button locationBtn = (Button)getView().findViewById(R.id.pinCurrentLocation);
		locationBtn.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch( v.getId()) {
		
		case R.id.pinSubmit:
			if(location == null) {
				location = new Location("");
				location.setLatitude(22.204589);
				location.setLongitude(10.105668);
			}
			long id = dbAdapter.insertPin(title.getText().toString(),
										  desc.getText().toString(),
										  location);
			
			if (id < 0)
				Toast.makeText(context, "Unsuccessful: Pin not added", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "Pin successfully added", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.pinCurrentLocation:
			locationUtil = new LocationUtil(this);		
			break;
		}
	}
	
	public void setLocation(Location l) {
		location = l;
		address.setText("Current Location");
		address.setTextColor(Color.LTGRAY);
		locationUtil.disconnect();
	}
}
