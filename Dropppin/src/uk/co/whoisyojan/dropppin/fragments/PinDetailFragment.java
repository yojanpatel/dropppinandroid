package uk.co.whoisyojan.dropppin.fragments;


import uk.co.whoisyojan.dropppin.PinDatabaseAdapter;
import uk.co.whoisyojan.dropppin.R;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PinDetailFragment extends Fragment {
	

	public static final String ID = "_id";
	long id;
	
	TextView title;
	TextView desc;
	TextView location;
	
	PinDatabaseAdapter dbAdapter;
	Context context;
	Cursor cursor;
	
	int titleCol, descCol, lonCol, latCol;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.pin_detail_layout, container, false);
		
		id = getArguments().getLong(ID);
		Log.d("ID",""+id);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		context = getActivity();
		
		dbAdapter = new PinDatabaseAdapter(getActivity());
		cursor = dbAdapter.getPin(id);
		titleCol = cursor.getColumnIndex(dbAdapter.titleCol());
		descCol = cursor.getColumnIndex(dbAdapter.descCol());
		lonCol = cursor.getColumnIndex(dbAdapter.longitudeCol());
		latCol = cursor.getColumnIndex(dbAdapter.latitudeCol());
		
		title = (TextView)getView().findViewById(R.id.detailTitle);
		desc = (TextView)getView().findViewById(R.id.detailDesc);
		location = (TextView)getView().findViewById(R.id.detailLocation);
		//Toast.makeText(context, cursor.getString(titleCol), Toast.LENGTH_LONG).show();
		changeData(id);
		
	}



	// Factory method to instantiate fragment with id
	public static PinDetailFragment newInstance(long id) {
		PinDetailFragment p = new PinDetailFragment();
		Bundle args = new Bundle();
		args.putLong(ID, id);
		p.setArguments(args);
		return p;
	}
	
	public long getShownIndex() {
		return getArguments().getLong(ID, 0);
	}
	
	public void changeData(long id) {
		String ti = cursor.getString(titleCol);
		String de = cursor.getString(descCol);
		double lon = cursor.getDouble(lonCol);
		double lat = cursor.getDouble(latCol);
		
		title.setText(ti);
		desc.setText(de);
		location.setText("Longitude: " + lon + " | Latitude: " + lat );
	}
	
	
}
