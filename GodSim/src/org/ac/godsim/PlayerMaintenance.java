package org.ac.godsim;

import java.util.List;

import org.ac.godsim.persistentdata.GodSimDB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eastland
 * @since 1-November-2012
 */

public class PlayerMaintenance extends Activity implements OnClickListener, OnItemSelectedListener {

	private TextView msgBox;
	Spinner spinner;
	private TextView newNameField;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_maintenance);
        
        /* This will capture the value sent by the calling activity
         * That value is the player name in the spinner */
        String defaultPlayer = getIntent().getExtras().getString("defPlayer");
        
        msgBox = (TextView)findViewById(R.id.msgBox);
        newNameField = (TextView) findViewById(R.id.txtNewName);
        
        View deleteButton = findViewById(R.id.DeletePlayerButton);
		deleteButton.setOnClickListener(this);
        
        View renameButton = findViewById(R.id.RenamePlayerButton);
		renameButton.setOnClickListener(this);
		
		View closeButton = findViewById(R.id.CloseButton);
		closeButton.setOnClickListener(this);
		
		/* populate the spinner with user names */
		spinner = (Spinner) findViewById(R.id.maintenanceSpinner);
		spinner.setOnItemSelectedListener(this);
		attachPlayersToSpinner(defaultPlayer);
		
    }
	
	/**
	 * Use the method to get the name of the player as currently
	 * selected in the spinner. Returns a string.
	 * @return The name of the player selected.
	 */
	public String getPlayerSelection() {
		return (String) spinner.getSelectedItem();
	}

	private void attachPlayersToSpinner(String defPlayer) {
        
        List<String> players = GodSimDB.getAllRows("PLAYERS_TABLE");
        
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
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View view) {
		String player = (String) spinner.getSelectedItem();
		String feedback;
		switch (view.getId()) {
    	case R.id.DeletePlayerButton:
    		if (player.equals("Guest")) {
    			msgBox.setText("You cannot delete the Guest player account.");
    			break;
			}
    		GodSimDB.removePlayer(player);
    		feedback = player + " has been removed along with all associated game activity.";
    		msgBox.setText(feedback);
    		attachPlayersToSpinner("Guest");
    		break;
    	case R.id.RenamePlayerButton:
    		if (player.equals("Guest")) {
    			msgBox.setText("You cannot rename the Guest player account.");
    			break;
			}
    		TextView tv2 = (TextView) findViewById(R.id.txtNewName);
    		String newName = tv2.getText().toString();
    		System.err.println(newName);
    		if (newName.equals("")) {
    			msgBox.setText("Please enter a replacement name.");
    			break;
    		}
    		GodSimDB.updatePlayer(player, newName);
    		feedback = "The name of player " + player + " has been changed to " + newName;
    		msgBox.setText(feedback);
    		System.err.println("clearning new name field");
    		newNameField.setText("");
    		attachPlayersToSpinner(newName);
    		break;
    	case R.id.CloseButton:
    		player = (String) spinner.getSelectedItem();
    		Intent returnIntent = new Intent();
    		returnIntent.putExtra("player", player);
    		setResult(RESULT_OK, returnIntent);
    		finish();
    		break;
    	}
	}	
}
