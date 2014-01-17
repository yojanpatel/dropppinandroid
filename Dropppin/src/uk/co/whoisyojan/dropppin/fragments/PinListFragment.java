package uk.co.whoisyojan.dropppin.fragments;

import uk.co.whoisyojan.dropppin.PinCursorAdapter;
import uk.co.whoisyojan.dropppin.PinDatabaseAdapter;
import uk.co.whoisyojan.dropppin.R;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PinListFragment extends Fragment implements OnItemClickListener {
	
	Context context;
	PinDatabaseAdapter dbAdapter;
	ListView listView;
	Communicator comm;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		return inflater.inflate(R.layout.pin_listview_layout, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dbAdapter = new PinDatabaseAdapter(getActivity());
		listView = (ListView)getView().findViewById(R.id.pinListView);
		Cursor c = dbAdapter.getAllPins();
		PinCursorAdapter pca = new PinCursorAdapter(context, c, 0);
		listView.setAdapter(pca);
		
		listView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		comm.respond(id);
	}
	
	
	public interface Communicator {
		public void respond(long id);
	}
	
	public void setCommunicator(Communicator comm) {
		this.comm = comm;
	}

}
