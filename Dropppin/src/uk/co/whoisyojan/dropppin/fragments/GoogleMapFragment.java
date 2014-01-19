package uk.co.whoisyojan.dropppin.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;


public class GoogleMapFragment extends MapFragment{
	
	private Fragment parent;
	private static final String MAP_BUNDLE_KEY = "MapOptions";
	private OnMapReadyListener callback;
	
	public static GoogleMapFragment newInstance(Fragment parent) {
		GoogleMapFragment fragment = new GoogleMapFragment();
		fragment.setParentFragment(parent);
		return fragment;
	}
	
	public static GoogleMapFragment newInstance(Fragment parent, GoogleMapOptions options) {
		Bundle args = new Bundle();
		args.putParcelable(MAP_BUNDLE_KEY, options);
		GoogleMapFragment fragment = new GoogleMapFragment();
		fragment.setParentFragment(parent);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
		View v = super.onCreateView(inflater, group, bundle);
		callback = (OnMapReadyListener)parent;
		if (callback != null && !callback.isMapReady()) {
			callback.onMapReady(getMap());
		}
		return v;
	}
	
	public void setParentFragment(Fragment f) {
		parent = f;
	}
	
	public static interface OnMapReadyListener {
		void onMapReady(GoogleMap map);
		boolean isMapReady();
	}
	
	
}
