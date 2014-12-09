package edu.purdue.jwlehman;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.*;



public class CustomOnItemSelectedListener implements OnItemSelectedListener{
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Toast.makeText(parent.getContext(),parent.getItemAtPosition(pos).toString(),Toast.LENGTH_LONG).show();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
