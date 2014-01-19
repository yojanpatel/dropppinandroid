package uk.co.whoisyojan.dropppin;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PinCursorAdapter extends CursorAdapter {

	Context context;
	LayoutInflater inflater;
	
	public PinCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		this.context = context;
		inflater = LayoutInflater.from(context);
	
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		//ImageView img = (ImageView)view.findViewById(R.id.rowImage);
		TextView title = (TextView)view.findViewById(R.id.rowTitle);
		TextView desc = (TextView)view.findViewById(R.id.rowDesc);
		
		title.setText(cursor.getString(1));
		desc.setText(cursor.getDouble(3) + " " + cursor.getDouble(4));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(R.layout.listview_row, parent, false);
		return view;
	}
	
	@Override
	public long getItemId(int pos) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(pos);
		return cursor.getLong(cursor.getColumnIndex(PinDatabaseAdapter.idCol()));

	}

}
