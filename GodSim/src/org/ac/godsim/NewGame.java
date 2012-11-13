package org.ac.godsim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NewGame extends Activity implements OnClickListener {

	private TextView tv; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_game);
        
        tv = (TextView)findViewById(R.id.msgBox);
        
        View PlayButton = findViewById(R.id.PlayButton);
		PlayButton.setOnClickListener(this);

        View createUserButton = findViewById(R.id.CreateUserButton);
		createUserButton.setOnClickListener(this);
		
		View cancelButton = findViewById(R.id.CancelButton);
		cancelButton.setOnClickListener(this);
		
        View newUser = (View) findViewById(R.id.txtUserName);
        newUser.requestFocus();
	}

	//@Override
	public void onClick(View view) {
		switch (view.getId()) {
    	
		case R.id.PlayButton:
    		/* TODO:
    		 * Activity to solicit information relevant to new game
    		 * Save this data to db
    		 */
    		feedback("to do: update db, start new game");
    		break;
    	case R.id.CreateUserButton:
    		/* TODO:
    		 * Activity to solicit information relevant to new game
    		 * Save this data to db
    		 */
    		feedback("to do: implement create new user");
    		break;
    	case R.id.CancelButton:
    		/* TODO:
    		 * Save state of this activity???
    		 */
    		finish();
    		moveTaskToBack(true);
    		break;
    	}
		
	}
	
	private void feedback(String msg) {
		String currentText = tv.getText().toString();
		currentText += "\n" + msg;
		tv.setText(currentText);
	}

}
