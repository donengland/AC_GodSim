package org.ac.godsim;

import java.util.List;

import org.ac.godsim.persistentdata.GodSimDB;
import org.ac.godsim.sharedprefs.SharedPrefs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	String thisPlayer;
	int thisGame;
	//SharedPreferences prefs;
	//SharedPreferences.Editor prefsEditor;
	final String lastPlayerPref = "LAST_PLAYER";
	final static int PLAY_GAME = 1; //this is to track calling / returning from a a game
	final static int MAINT_ACTIVITY = 2; //this is to track calling/return from the player maintenance activity 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        tv = (TextView)findViewById(R.id.msgBox);
        
        View continueButton = findViewById(R.id.PlayGameButton);
		continueButton.setOnClickListener(this);
        
        View maintenanceButton = findViewById(R.id.PlayerMaintenanceButton);
		maintenanceButton.setOnClickListener(this);

//		View multiplayerButton = findViewById(R.id.MultiplayerButton);
//		multiplayerButton.setOnClickListener(this);
		
		View quitButton = findViewById(R.id.QuitGameButton);
		quitButton.setOnClickListener(this);
		
		/* Delete any existing databases for testing purposes */
		/* TODO: delete this code later */
		GodSimDB.deleteDB(this);
		
		/* Load the shared preferences */
//		prefs = this.getSharedPreferences("GodSimPrefs", MODE_PRIVATE);
//		String lastPlayer = prefs.getString(lastPlayerPref, "test");
//		prefsEditor = prefs.edit().putString(lastPlayerPref, "test");
//		prefsEditor.commit();
		
		/* Create the database
		 * Note that this will only create a db if none exists
		 */
		new GodSimDB(this);
			
		/* Load the default settings */
		List<String> settings = GodSimDB.getSettings();
		
		/* populate the spinner with user names */
		spinner = (Spinner) findViewById(R.id.spinnerPlayer);
		spinner.setOnItemSelectedListener(this);
		
		String lastPlayer;
		if (settings.isEmpty()) {
			/* this means that we have no stored settings (probably due to an empty database) */
			/* populate the spinner with just the "Guest" account */
			System.err.println("Empty DB discovered. Adding Guest player to spinner");
			attachPlayersToSpinner("Guest");
			thisGame = 1;
		} else {
			/* Since we have saved values, use them to initiate state */
			lastPlayer = settings.get(0);
			System.err.printf("the last player to play was: %s\n", lastPlayer);
			attachPlayersToSpinner(lastPlayer);
			System.err.printf("retrieving game number for player: %s\n", lastPlayer);
			thisGame = GodSimDB.getGame(lastPlayer);
			System.err.printf("Game number for player %s is: %d\n", lastPlayer, thisGame);
			System.err.println("before here");
		}
		
		/* Take this out if not running tests */
		Tests.runTests();
		
		/* Create a listener for new player entry */
		TextView.OnEditorActionListener newPlayerListener = new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ( event == null || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ) {
					final EditText newPlayer = (EditText) findViewById(R.id.txtNewPlayerName);
					String player = newPlayer.getText().toString();
					if (!player.equals("")) {
						System.err.println("editor action listener");
						GodSimDB.addPlayer(player, "password");
						GodSimDB.updateSetting("lastPlayer", player);
						//prefsEditor.putString(lastPlayerPref, player);
						//prefsEditor.commit();
						//System.err.println(prefs.getString(lastPlayerPref, "Guest"));
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
        
        List<String> players = GodSimDB.getAllRows("PLAYERS_TABLE");
        
        if (players.isEmpty()) {
        	System.err.println("PLAYERS table is empty; Adding guest player");
        	GodSimDB.addPlayer(defPlayer, "password");
        	
//        	String temp = GodSimDB.getOneValue("PLAYERS_TABLE", "PLAYER_NAME_COLUMN", "Guest", "PLAYER_NAME_COLUMN");
//        	System.err.printf("testing: %s\n", temp);
        	
        	GodSimDB.updateSetting("lastPlayer", defPlayer);
        	String temp = GodSimDB.getSettings().get(0);
        	System.err.printf("Verifying settings updated: %s\n", temp);
        	//prefsEditor.putString(lastPlayerPref, "Guest");
        	//prefsEditor.commit();
        	//System.err.println(prefs.getString(lastPlayerPref, "xx"));
        	
        	/* Update list to include new result */
        	players = GodSimDB.getAllRows("PLAYERS_TABLE");
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
    	final Bundle bundle = new Bundle();
    	Intent i;
    	thisPlayer = (String) spinner.getSelectedItem();
    	switch (view.getId()) {
	    	case R.id.PlayGameButton:
	    		if (isNewPlayer()) { GodSimDB.addPlayer(thisPlayer, "password"); } 
	    		bundle.putString("thisPlayer", thisPlayer);
	    		i = new Intent(this, GodSim.class);
	    		i.putExtras(bundle);
	    		System.err.println("starting game");
	    		startActivityForResult(i, PLAY_GAME);
	    		break;
	    	case R.id.PlayerMaintenanceButton:
	    		bundle.putString("defPlayer", (String) spinner.getSelectedItem());
	    		i = new Intent(this, PlayerMaintenance.class);
	    		i.putExtras(bundle);
	    		startActivityForResult(i, MAINT_ACTIVITY);
	    		break;
//	    	case R.id.MultiplayerButton:
//	    		/* TODO:
//	    		 * Wide open. Leave this button ineffective until much later
//	    		 */
//	    		feedback("to be implemented");
//	    		break;
	    	case R.id.QuitGameButton:
	    		System.err.println(thisPlayer);
	    		//prefsEditor.putString(lastPlayerPref, thisPlayer);
	    		finish();
	    		moveTaskToBack(true);
	    		break;
	    	}
    }

    private Boolean isNewPlayer () {
    	String p = (String) spinner.getSelectedItem();
    	return GodSimDB.isNewPlayer(p);    	
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {
	    	case PLAY_GAME:
	    		if (resultCode == RESULT_OK) {
	    			//TODO this is where we save the user's progress and other settings 
	    		}
	    	
	    		break;
		    case MAINT_ACTIVITY:
		    	if (resultCode == RESULT_OK) {
		    		String defPlayer = data.getStringExtra("player");
		    		attachPlayersToSpinner(defPlayer);
		    	}
		    	break;
		    default:
		    	break;
	    }
    }   
    
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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		System.err.println("onNothingSelected");
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//TODO: save game state
		System.err.printf("storing last player setting: %s\n", thisPlayer);
		GodSimDB.updateSetting("lastPlayer", thisPlayer);
		//prefsEditor.putString(lastPlayerPref, thisPlayer);
		
	}
}
