package org.ac.godsim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GodSimDB {
  
	/* The index (key) column name for use in where clauses. */
	public static final String KEY_ID = "_id";
	  
	/* The name and column index of each column in the database */
	public static final String USER_NAME_COLUMN =  "USER_NAME_COLUMN";
	public static final String LEVEL_COLUMN = "LEVEL_COLUMN";
	public static final String PASSWORD_COLUMN = "PASSWORD_COLUMN";
	  
	/* Database open/upgrade helper */
	private GodSimDBOpenHelper godSimDBOpenHelper;
	
	public GodSimDB(Context context) {
		godSimDBOpenHelper = new GodSimDBOpenHelper(context, GodSimDBOpenHelper.DB_NAME, null, 
				  										GodSimDBOpenHelper.DB_VERSION);
	}
	  
	/* Called when we no longer need access to the database. */
	public void closeDatabase() {
		godSimDBOpenHelper.close();
	}
	
	private Cursor queryUser(String user) {
		/* Querying the user database */
		  
		/* Specify the result column projection.  */
		String[] result_columns = new String[] {KEY_ID, USER_NAME_COLUMN, LEVEL_COLUMN, PASSWORD_COLUMN }; 
		   
		/* Specify the where clause that will limit the results */
		String where = USER_NAME_COLUMN + "=" + user;
		   
		/* Replace these with valid SQL statements as necessary */
		String whereArgs[] = null;
		String groupBy = null;
		String having = null;
		String order = null;
		   
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		Cursor cursor = db.query(GodSimDBOpenHelper.DB_TABLE, 
		                         result_columns, where,
		                         whereArgs, groupBy, having, order);
		    
		return cursor;
	  }
  
	
	
	public Boolean getUser(String user) {
		Boolean hasEntry = false;
		  
		Cursor cursor = queryUser(user);
		    
		/* Iterate over the cursors rows. 
		 * The Cursor is initialized at before first, so we can 
		 * check only if there is a "next" row available. If the
		 * result Cursor is empty this will return false.
		 */
		while (cursor.moveToNext()) {
			/* user exist in db */
		  	hasEntry = true;
		}
		
		/* Close the Cursor to prvent memory leaks */
		cursor.close();
		 
		return hasEntry;
	}
  
	public Boolean addNewUser(String user, String pw) {
		/* Inserting new rows into a database */
	  
		/* user name already exists. Must be unique */
		if ( getUser(user) ) {return false;}
		  
		/* Create a new row of values to insert. */
		ContentValues newValues = new ContentValues();
			  
		/* Assign values for each row */
		newValues.put(USER_NAME_COLUMN, user);
		newValues.put(LEVEL_COLUMN, 1);
		newValues.put(PASSWORD_COLUMN, pw);
			
		/* Insert the row into the  table */
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.insert(GodSimDBOpenHelper.DB_TABLE, null, newValues);
			
		return true;
	}
  
	public void updateUserInfo(String user, int level) {
		/* Updating a database row */
		  
		/* Create the updated row Content Values. */
		ContentValues updatedValues = new ContentValues();
		  
		/* Assign values for each row */
		updatedValues.put(USER_NAME_COLUMN, user);
		updatedValues.put(LEVEL_COLUMN, level);
		  
		/* Specify a where clause that defines which rows should be
		 * updated. Specify where arguments as necessary.
		 */
		String where = USER_NAME_COLUMN + "=" + user;
		String whereArgs[] = null;
		  
		/* Update the row with the specified index with the new values. */
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.update(GodSimDBOpenHelper.DB_TABLE, updatedValues, 
		          where, whereArgs);
	}
  
	public void deleteUser(String user) {
		/* Delete a database row */
		  
		/* Specify a where clause that determines which row(s) to delete.
		 * Specify where arguments as necessary.
		 */
		String where = USER_NAME_COLUMN + "=" + user;
		String whereArgs[] = null;
		  
		// Delete the rows that match the where clause.
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.delete(GodSimDBOpenHelper.DB_TABLE, where, whereArgs);
	}

  
	private static class GodSimDBOpenHelper extends SQLiteOpenHelper {
	    
		private static final String DB_NAME = "godSimDB.db";
		private static final String DB_TABLE = "USERS_TABLE";
		private static final int DB_VERSION = 1;
		    
		/* SQL Statement to create a new database */
		private static final String DB_CREATE = "create table " +
			DB_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			USER_NAME_COLUMN + " text not null, " +
			LEVEL_COLUMN + " integer);";

		public GodSimDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		/* Create the new database */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
		}
		  
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
			// TODO Auto-generated method stub
		}
	
	}
}