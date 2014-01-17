package uk.co.whoisyojan.dropppin;

import android.app.Activity;
import android.app.Fragment;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class LocationUtil implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {
	
	private final static int
			CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	LocationClient locationClient;
	
	Activity containing;
	Communicator comm;
	
	public LocationUtil(Fragment f) {		
		containing = f.getActivity();
		comm = (Communicator) f;
		connect();
	}
	
	public void connect() {
		if(locationClient == null) {
			locationClient = new LocationClient(
					containing.getApplicationContext(),
					this,this);
		}
		locationClient.connect();
	}
	
	public void disconnect() {
		locationClient.disconnect();
		Toast.makeText(containing.getApplicationContext(),
				"Disconnected",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		Toast.makeText(containing.getApplicationContext(),
					"Connected",
					Toast.LENGTH_SHORT).show();

		Location currentLocation = locationClient.getLastLocation();
		comm.setLocation(currentLocation);
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(containing.getApplicationContext(),
				"Disconnected. Please re-connect",
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        containing,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
	}
	
	
	void showErrorDialog(int code) {
		GooglePlayServicesUtil.getErrorDialog(code, containing, 
				CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
	}
	
	public interface Communicator {
		public void setLocation(Location l);
	}

}
