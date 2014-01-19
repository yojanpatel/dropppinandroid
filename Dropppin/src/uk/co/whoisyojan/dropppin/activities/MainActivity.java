package uk.co.whoisyojan.dropppin.activities;

import uk.co.whoisyojan.dropppin.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void createPinForm(View v) {
		Intent i = new Intent(this, PinFormActivity.class);
		startActivity(i);
	}
	
	public void createListView(View v) {
		Intent i = new Intent(this, PinListMapActivity.class);
		//Intent i = new Intent(this, PinDetailActivity.class);
		
		startActivity(i);
	}
	
	


}
