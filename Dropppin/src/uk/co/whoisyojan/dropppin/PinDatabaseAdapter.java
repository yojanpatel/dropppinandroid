package uk.co.whoisyojan.dropppin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.widget.Toast;


/*
 * Contains all methods to perform database operations like opening connection,
 * closing connection, insert, update, delete etc. 
 */
public class PinDatabaseAdapter {
	
	PinDatabaseHelper dbHelper;
	
	public PinDatabaseAdapter(Context context) {
		dbHelper = new PinDatabaseHelper(context);
	}
	
	// Returns the row id of the row data inserted in or -1 if operation unsuccessful
	public long insertPin(String title, String desc, Location location) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		// key-value pairs for the row to be added
		ContentValues cv = new ContentValues();
		cv.put(titleCol(), title);
		cv.put(descCol(), desc);
		cv.put(longitudeCol(), location.getLongitude());
		cv.put(latitudeCol(), location.getLatitude());
		
		long id = db.insert(PinDatabaseHelper.TABLE_NAME, null, cv);
		return id;
	}
	
	public Cursor getPin(long id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] columns = {idCol(), titleCol(), descCol(), longitudeCol(), latitudeCol()};
		String[] selectionArgs = {Long.toString(id)};
		
		Cursor cursor = db.query(PinDatabaseHelper.TABLE_NAME,
								columns,
								idCol() + " = ?",
								selectionArgs,
								null, null, null,null);
		
		cursor.moveToFirst();
		return cursor;
	}
	
	public long deletePin(long id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] whereArgs = {Long.toString(id)};
		return db.delete(PinDatabaseHelper.TABLE_NAME,
						idCol() + " = ?",
						whereArgs);
	}
	
	public Cursor getAllPins() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] columns = {idCol(), titleCol(), descCol(), longitudeCol(), latitudeCol()};
		Cursor cursor = db.query(PinDatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
		
		return cursor;
	}
	
	// Access methods for column names
	public static String idCol() { return PinDatabaseHelper.UID;}
	public static String titleCol() { return PinDatabaseHelper.TITLE;}
	public static String descCol() { return PinDatabaseHelper.DESC;}
	public static String longitudeCol() { return PinDatabaseHelper.LONGITUDE;}
	public static String latitudeCol() { return PinDatabaseHelper.LATITUDE;}
	
	static class PinDatabaseHelper extends SQLiteOpenHelper {
		private Context context;
		
		private static final String DATABASE_NAME = "pindatabase";
		private static final String TABLE_NAME = "pins";
		private static final int DATABASE_VERSION = 2;
		
		// column names
		private static final String UID = "_id";
		private static final String TITLE = "title";
		private static final String DESC = "desc";
		private static final String LONGITUDE = "longitude";
		private static final String LATITUDE = "latitude";

		
		private static final String CREATE = "CREATE TABLE " + TABLE_NAME + "("
											+ UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
											+ TITLE + " VARCHAR(255), "
											+ DESC + " TEXT, "
											+ LONGITUDE + " FLOAT, "
											+ LATITUDE + " FLOAT)";
		
		private static final String DROP = "DROP TABLE " + TABLE_NAME + " IF EXISTS";
		
		public PinDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Called on the first time the database is created
			try {
				db.execSQL(CREATE);
				Toast.makeText(context,"Table created", Toast.LENGTH_LONG).show();
			} catch (SQLException e) {
				Toast.makeText(context,"Table already exists", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				db.execSQL(DROP);
				onCreate(db);
			} catch(SQLException e) {
			}
		}
	}

}
