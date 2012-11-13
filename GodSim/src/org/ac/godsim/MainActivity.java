package org.ac.godsim;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private TextView tv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_sim);
        
        tv = (TextView)findViewById(R.id.msgBox);
        
        View newGameButton = findViewById(R.id.NewGameButton);
		newGameButton.setOnClickListener(this);
        
        View continueButton = findViewById(R.id.ContinueGameButton);
		continueButton.setOnClickListener(this);
        
		View multiplayerButton = findViewById(R.id.MultiplayerButton);
		multiplayerButton.setOnClickListener(this);
		
		View quitButton = findViewById(R.id.QuitGameButton);
		quitButton.setOnClickListener(this);
		
		/* TODO:
		 * Add call to db to check for existenct
		 * if no db found, create skeleton with guest player
		 */
		new GodSimDB(this);
		
    }
    
    public void onClick(View view) {
    	switch (view.getId()) {
    	
    	case R.id.NewGameButton:
    		/* TODO:
    		 * Activity to solicit information relevant to new game
    		 * Save this data to db
    		 */
    		startActivity(new Intent(this, NewGame.class));
    		feedback("to be implemented");
    		break;
    	case R.id.ContinueGameButton:
    		/* TODO:
    		 * Ask for user id (new activity)
    		 * Load saved game state from db
    		 */
    		startActivity(new Intent(this, GodSim.class));
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
    		 */
    		finish();
    		moveTaskToBack(true);
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
}
