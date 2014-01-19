package uk.co.whoisyojan.dropppin.fragments;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import uk.co.whoisyojan.dropppin.LocationUtil;
import uk.co.whoisyojan.dropppin.PinDatabaseAdapter;
import uk.co.whoisyojan.dropppin.R;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PinFormFragment extends Fragment implements OnClickListener, LocationUtil.Communicator {
	
	PinDatabaseAdapter dbAdapter;
	
	Context context;
	
	EditText title;
	EditText desc;
	EditText address;
	EditText search;
	
	ProgressBar addressIndicator;
	ProgressBar searchIndicator;
	
	Button submitBtn;
	Button searchBtn;
	Button locationBtn;
	
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
		
		dbAdapter = new PinDatabaseAdapter(context);
		
		title = (EditText)getView().findViewById(R.id.pinTitle);
		desc = (EditText)getView().findViewById(R.id.pinDescription);
		address = (EditText)getView().findViewById(R.id.pinLocation);
		search = (EditText)getView().findViewById(R.id.pinLocationSearch);
		
		addressIndicator = (ProgressBar)getView().findViewById(R.id.address_progress);
		searchIndicator = (ProgressBar)getView().findViewById(R.id.search_progress);
		
		submitBtn = (Button)getView().findViewById(R.id.pinSubmit);
		submitBtn.setOnClickListener(this);
		locationBtn = (Button)getView().findViewById(R.id.pinCurrentLocation);
		locationBtn.setOnClickListener(this);
		searchBtn = (Button)getView().findViewById(R.id.pinSearchBtn);
		searchBtn.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch( v.getId()) {
		
		case R.id.pinSubmit:
			String message;
			if(location != null) {
				
				long id = dbAdapter.insertPin(title.getText().toString(),
											  desc.getText().toString(),
											  location);
				
				if (id < 0)
					message = "Unsuccessful: Pin not added";
				else
					message = "Pin successfully added";
			} else {
				message = "Pin location is unrecognized";
			}
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			break;
			
		case R.id.pinCurrentLocation:
			locationUtil = new LocationUtil(this);	
			break;
			
		case R.id.pinSearchBtn:
			String searchQuery = search.getText().toString();
			if(searchQuery.trim().length() > 0) { //check query is not just whitespaces
				searchIndicator.setVisibility(View.VISIBLE);
				searchBtn.setVisibility(View.GONE);
				(new GetAddressFromSearchTask(context)).execute(searchQuery);
			} else {
				Toast.makeText(context, "Oops, nothing entered.", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	public void setLocation(Location l) {
		location = l;
		locationUtil.disconnect();
		
		// if geocoder services are available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
           && Geocoder.isPresent()) {
			locationBtn.setVisibility(View.GONE);
			addressIndicator.setVisibility(View.VISIBLE);
			(new GetAddressFromLocationTask(context)).execute(location);
		} else {
			locationBtn.setVisibility(View.VISIBLE);
		}
	}
	
	
	/* to call getFromLocation() in a background thread
	 * Location - current location
	 * Void - progress units are not used
	 * String - An address passed to onPostExecute() */
	private class GetAddressFromLocationTask extends
			AsyncTask<Location, Void, String> {
		
		Context context;

		public GetAddressFromLocationTask(Context c) {
			super();
			context = c;
		}

		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder =
					new Geocoder(context, Locale.getDefault());
			Location loc = params[0];
			List<Address> addresses = null;
			
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),
							loc.getLongitude(), 1);
			} catch(IOException e1) {
				return ("IOException trying to get address");
			} catch(IllegalArgumentException e2) {
				return ("Illegal arguments: " + loc.getLatitude() + " , " + loc.getLongitude() + " passed.");
			}
			
			if(addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				String addressText = String.format("%s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
						address.getLocality(),
						address.getCountryName());
				
				return addressText;
			} else {
				return "No address found";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			addressIndicator.setVisibility(View.GONE);
			locationBtn.setVisibility(View.VISIBLE);
			address.setText(result);
		}		
	}
	
	/* to call g in a background thread
	 * Location - current location
	 * Void - progress units are not used
	 * String - An address passed to onPostExecute() */
	private class GetAddressFromSearchTask extends
			AsyncTask<String, Void, Address> {
		
		Context context;
		String addressText;
		
		public GetAddressFromSearchTask(Context c) {
			super();
			context = c;
		}

		@Override
		protected Address doInBackground(String... params) {
			Geocoder geocoder =
					new Geocoder(context, Locale.getDefault());
			String search = params[0];
			List<Address> addresses = null;
			
			try {
				addresses = geocoder.getFromLocationName(search, 4);
			} catch(IOException e1) {
				Log.e("Geocoder", "IOException could not getFromLocationName()");
			}
			
			if(addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				addressText = addressToString(address);
				return address;
			} else {
				addressText =  "No address found";
				return null;
			}
			
		}
		
		private String addressToString(Address address) {
			String addressText = String.format("%s, %s, %s",
					address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
					address.getLocality(),
					address.getCountryName());
			
			return addressText;
		}

		@Override
		protected void onPostExecute(Address result) {
			
			searchIndicator.setVisibility(View.GONE);
			searchBtn.setVisibility(View.VISIBLE);
			address.setText(addressText);
			if(result != null) {
				location = new Location("reverseGeocoded");
				location.setLatitude(result.getLatitude());
				location.setLongitude(result.getLongitude());
			}
		}		
	}
}
