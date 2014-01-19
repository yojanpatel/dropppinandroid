package uk.co.whoisyojan.dropppin.fragments;

import java.util.HashMap;

import uk.co.whoisyojan.dropppin.PinDatabaseAdapter;
import uk.co.whoisyojan.dropppin.R;
import uk.co.whoisyojan.dropppin.fragments.GoogleMapFragment.OnMapReadyListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PinMapFragment extends Fragment implements
	OnMapClickListener, OnMarkerClickListener, OnMapReadyListener {

	Context context;
	public static final String ID = "_id";
	long id;
	
	PinDatabaseAdapter dbAdapter;
	Cursor allPins;
	
	GoogleMapFragment mapFrag;
	GoogleMap googleMap;
	boolean mapPopulated;
	
	HashMap<Long, Marker> markerMap;
	
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState) {
		
		context = getActivity();
		setRetainInstance(true);
		
		dbAdapter = new PinDatabaseAdapter(getActivity());	
		if(allPins == null)
			allPins = dbAdapter.getAllPins();
		
		if(markerMap == null)
			markerMap = new HashMap<Long, Marker>();
		
		return inf.inflate(R.layout.pin_map_layout, vg, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initializeFrag();
	}
	
	// Factory method to instantiate fragment with id
	public static PinMapFragment newInstance(long id) {
		PinMapFragment p = new PinMapFragment();
		Bundle args = new Bundle();
		args.putLong(ID, id);
		p.setArguments(args);
		return p;
	}
	
	private void initializeFrag() {
		mapFrag = (GoogleMapFragment) getFragmentManager().findFragmentById(R.id.pinMap);
		
		if (mapFrag == null) {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			mapFrag = GoogleMapFragment.newInstance(this, getMapOptions());
			mapFrag.setRetainInstance(true);
			transaction.replace(R.id.pinMap, mapFrag).commit();
		}
	}
	
	// Add map markers for every pin in the database
	public void populateMap() {
		if(!mapPopulated){
			allPins.moveToFirst();
			while(!allPins.isAfterLast()) {
				
				Marker marker = googleMap.addMarker(new MarkerOptions()
					.position(getPointFromCursor(allPins))
					.title(allPins.getString(allPins.getColumnIndex(PinDatabaseAdapter.titleCol())))
					.snippet(allPins.getString(allPins.getColumnIndex(PinDatabaseAdapter.descCol())))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				
				markerMap.put(
					allPins.getLong(allPins.getColumnIndex(PinDatabaseAdapter.idCol())),
					marker);
				
				allPins.moveToNext();
			}
			mapPopulated = true;
		}
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		googleMap = map;	
		if(mapFrag != null){
			if (googleMap != null) {
				googleMap.setOnMapClickListener(this);
				googleMap.setOnMarkerClickListener(this);
				googleMap.setBuildingsEnabled(true);
				populateMap();
			}		
		}
	}
	
	
	public void goToPoint(LatLng point) {
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point), 1000, null);
	}
	
	// Move map camera to centre the row with ID = id
	public void goToPoint(long id) {
		Cursor c = dbAdapter.getPin(id);
		LatLng point = getPointFromCursor(c);
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(point), 1500, null);
		markerMap.get(id).showInfoWindow();
	}
	
	// callback method when map is clicked
	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		// redirect to add pin page with location filled in with point
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		//redirect to pin detail view
		return false;
	}
	
	
	private GoogleMapOptions getMapOptions() {
		// Centre the marker clicked or default to first
		try {
			id = getArguments().getLong(ID);
		} catch(NullPointerException e) {
			allPins.moveToFirst();
			id = allPins.getLong(allPins.getColumnIndex(ID));
		}
		
		Cursor pin = dbAdapter.getPin(id);
		LatLng point = getPointFromCursor(pin);
		
		CameraPosition cp = new CameraPosition(point, 12, 0, 0);
		
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
		.rotateGesturesEnabled(false)
		.compassEnabled(false)
		.zoomControlsEnabled(false)
		.zoomGesturesEnabled(true)
		.rotateGesturesEnabled(false)
		.camera(cp);
		
		return options;
	}
	

	private static LatLng getPointFromCursor(Cursor c) {
		 return new LatLng(
					c.getDouble(c.getColumnIndex(PinDatabaseAdapter.latitudeCol())),
					c.getDouble(c.getColumnIndex(PinDatabaseAdapter.longitudeCol())));
	}

	@Override
	public boolean isMapReady() {
		return googleMap != null;
	}
}
