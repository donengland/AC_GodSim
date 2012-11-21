package org.ac.godsim;

import java.util.List;
import org.ac.godsim.persistentdata.GodSimDB;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eastland
 * @since 1-November-2012
 */

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private TextView tv;
	Spinner spinner;
	TextView newPlayer;
	final static int MAINT_ACTIVITY = 1; //this is to track calling/return from the player maintenanance activity
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        tv = (TextView)findViewById(R.id.msgBox);
        
        View continueButton = findViewById(R.id.PlayGameButton);
		continueButton.setOnClickListener(this);
        
        View maintenanceButton = findViewById(R.id.PlayerMaintenanceButton);
		maintenanceButton.setOnClickListener(this);

		View multiplayerButton = findViewById(R.id.MultiplayerButton);
		multiplayerButton.setOnClickListener(this);
		
		View quitButton = findViewById(R.id.QuitGameButton);
		quitButton.setOnClickListener(this);
		
		/* Delete any existing databases for testing purposes */
		/* TODO: delete this code later */
		GodSimDB.deleteDB(this);
		
		/* Create the database
		 * Note that this will only create a db if none exists
		 */
		new GodSimDB(this);
		
		/* populate the spinner with user names */
		spinner = (Spinner) findViewById(R.id.spinnerPlayer);
		spinner.setOnItemSelectedListener(this);
		attachPlayersToSpinner("Guest");
		
		/* Create a listener for new player entry */
		TextView.OnEditorActionListener newPlayerListener = new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ( event == null || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
					final EditText newPlayer = (EditText) findViewById(R.id.txtNewPlayerName);
					String player = newPlayer.getText().toString();
					if (!player.equals("")) {
						GodSimDB.addPlayer(player, "password");
						/* Update the spinner list */
						attachPlayersToSpinner(newPlayer.getText().toString());
						newPlayer.setText("");
						
						/* This will hide the soft keyboard after the user presses done */
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(newPlayer.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						
						return true;
					}
				}
				return false;
			}
		}; 
		
		/* Attach the listener to the new player text field */
		newPlayer = (TextView) findViewById(R.id.txtNewPlayerName);
		newPlayer.setOnEditorActionListener(newPlayerListener);
    }
    
    private void attachPlayersToSpinner(String defPlayer) {
        
        List<String> players = GodSimDB.queryAllRows("PLAYERS_TABLE");
        
        if (players.isEmpty()) {
        	System.err.println("PLAYERS table is empty; Adding guest player");
        	GodSimDB.addPlayer("Guest", "password");
        	/* Update list to include new result */
        	players = GodSimDB.queryAllRows("PLAYERS_TABLE");
        }
        
        /* Data adapter for spinner */ 
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_spinner_item, players); 
  
        /* Format the data adapter as a drop down list view */ 
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
  
        /* Attach the data adapter to spinner */
        spinner.setAdapter(dataAdapter);
        
        /* Set the spinner to the player just added */
        spinner.setSelection(dataAdapter.getPosition(defPlayer));
    }

    
    public void onClick(View view) {
    	switch (view.getId()) {
	    	case R.id.PlayGameButton:
	    		/* TODO:
	    		 * Load saved game state from db
	    		 */
	    		startActivity(new Intent(this, GodSim.class));
	    		break;
	    	case R.id.PlayerMaintenanceButton:
	    		final Bundle bundle = new Bundle();
	    		bundle.putString("defPlayer", (String) spinner.getSelectedItem());
	    		Intent i = new Intent(this, PlayerMaintenance.class);
	    		i.putExtras(bundle);
	    		startActivityForResult(i, MAINT_ACTIVITY);
	    		//startActivity(i);
	    		break;
	    	case R.id.MultiplayerButton:
	    		/* TODO:
	    		 * Wide open. Leave this button ineffective until much later
	    		 */
	    		feedback("to be implemented");
	    		break;
	    	case R.id.QuitGameButton:
	    		/* TODO:
	    		 * Save game state to db
	    		 * call onStop()???
	    		 */
	    		finish();
	    		moveTaskToBack(true);
	    		break;
	    	}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {
		    case MAINT_ACTIVITY:
		    	System.err.println("here");
		    	if (resultCode == RESULT_OK) {
		    		String defPlayer = data.getStringExtra("player");
		    		System.err.println(defPlayer);
		    		attachPlayersToSpinner(defPlayer);
		    		System.err.println("break");
		    		break;
		    	}
		    default:
		    	break;
	    }
    }   
    
    /*public void onPause() {
     * Not sure how to implement this. If I leave it active, hitting Esc from the map kicks all the way out
     * Leave inactive for now.
    	feedback("onPause()");
    }*/
    
    private void feedback(String msg) {
		String currentText = tv.getText().toString();
		currentText += "\n" + msg;
		tv.setText(currentText);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_god_sim, menu);
        return true;
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		System.err.println("onItemSelected");
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		System.err.println("onNothingSelected");
		
	}
	
//	@Override
//	public void onResume() {
//		//PlayerMaintenance.get
//		
//	}
}
