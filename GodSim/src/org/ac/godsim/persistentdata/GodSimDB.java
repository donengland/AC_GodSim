package org.ac.godsim.persistentdata;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * (c) 2012 Randall Eastland
 *
 * @author Randall Eastland
 * @since 1-November-2012
 */

public class GodSimDB {
	
	/* The index (key) column name for use in where clauses. */
	public static final String KEY_ID = "_id";
	  
	/* The name and column index of each column in the database */
	public static final String PLAYER_NAME_COLUMN =  "PLAYER_NAME_COLUMN";
	public static final String GAME_COLUMN = "GAME_COLUMN";
	public static final String PASSWORD_COLUMN = "PASSWORD_COLUMN";
	public static final String CIV_COLUMN = "CIV_COLUMN";
	public static final String UNIT_COLUMN = "UNIT_COLUMN";
	public static final String HEALTH_COLUMN = "HEALTH_COLUMN";
	public static final String X_COLUMN = "X_COLUMN";
	public static final String Y_COLUMN = "Y_COLUMN";
	
	/* Database open/upgrade helper */
	private static GodSimDBOpenHelper godSimDBOpenHelper;
	
	public GodSimDB(Context context) {
		godSimDBOpenHelper = new GodSimDBOpenHelper(context, GodSimDBOpenHelper.DB_NAME, null, 
				  										GodSimDBOpenHelper.DB_VERSION);	
	}
	
	/* Need the ability to delete the db for testing */
	/* TODO: delete later */
	public static void deleteDB(Context context) {
		ContextWrapper cw = new ContextWrapper(context);
		cw.deleteDatabase(GodSimDBOpenHelper.DB_NAME);
	}
	
	public static List<String> queryAllRows(String table){ 
        List<String> labels = new ArrayList<String>(); 
  
        String selectAll = "SELECT * FROM " + table; 
  
        SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase(); 
        Cursor cursor = db.rawQuery(selectAll, null); 
   
        if (cursor.moveToFirst()) { 
            do { labels.add(cursor.getString(1)); }
            while (cursor.moveToNext()); 
        } 

        cursor.close(); 
        db.close(); 
  
        return labels; 
    } 
	
	/**
	 * Use this method to add a player to the godSimDB. Player names
	 * must be unique, so if the name you are trying to add already
	 * exists, this method will return false.
	 * @param player This is the name that will be added to the database.
	 * @param pw Include a password. Note that at this point, passwords
	 * are not checked, so this entry is currently meaningless.
	 * @return  Returns true if the insert is successful, false otherwise.
	 */
	public static Boolean addPlayer(String player, String pw) {
		/* Inserting new rows into a database */
		
		/* Check to see if this record already exists */
		if ( recordExists(GodSimDBOpenHelper.PLAYERS_TABLE, player) ) {
			/* the record exists, cancel the operation */
			return false;
		}
		
		/* Create a new row of values to insert. */
		ContentValues newValues = new ContentValues();
			  
		/* Assign values for each row */
		newValues.put(PLAYER_NAME_COLUMN, player);
		newValues.put(GAME_COLUMN, 1);
		newValues.put(PASSWORD_COLUMN, pw);
			
		/* Insert the row into the  table */
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.insert(GodSimDBOpenHelper.PLAYERS_TABLE, null, newValues);
			
		return true;
	}
	
	public static SQLiteDatabase getDB() {
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		return db;
	}
	
	/**
	 * Use the method to remove an existing player from the database.
	 * This will remove the player along with all related information
	 * including any games in progress associated with this player.
	 * This is not reversible.
	 * @param player  Name of the player as shown in the spinner
	 * @return Boolean  Returns true if the removal is successful
	 *                     or false otherwise.
	 */
	public static Boolean removePlayer(String player) {
		String whereClause = PLAYER_NAME_COLUMN + "=\"" + player + "\"";
		String whereArgs[] = null;
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.delete(GodSimDBOpenHelper.PLAYERS_TABLE, whereClause, whereArgs);
		return true;
	}
	
	/**
	 * Use this method to change the name of an existing player in the
	 * database. This change will cascade to all related tables and
	 * will not change the status or progress of any games in progress
	 * associated with this player.
	 * @param oldName  Player name as it currently exists in the database
	 * @param newName  New name to be stored in the database
	 * @return Returns true if the update is successful, false otherwise.
	 */
	public static Boolean updatePlayer(String oldName, String newName) {
		
		/* Check to see if this record exists */
		if ( !recordExists(GodSimDBOpenHelper.PLAYERS_TABLE, oldName) ) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		ContentValues updatedValues = new ContentValues();
		
		/* Assign values for each row */
		updatedValues.put(PLAYER_NAME_COLUMN, newName);
		//updatedValues.put(GAME_COLUMN, game);
		//updatedValues.put(PASSWORD_COLUMN, pw);
		
		/* SQL query clauses */
		String whereClause = PLAYER_NAME_COLUMN + "=\"" + oldName + "\"";
		String whereArgs[] = null;
		
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		db.update(GodSimDBOpenHelper.PLAYERS_TABLE, updatedValues, whereClause, whereArgs);
		
		return true;
	}
	
	private static Boolean recordExists(String table, String label) {
		/* note that switch statement doesn't work with Java 1.6 */
		String columns[] = new String[1];
		String selection;
		
		/* Select the parts of the query that are dependent upon which 
		 * table we are looking at 
		 * */
		if (table.equals(GodSimDBOpenHelper.PLAYERS_TABLE)) {
			columns[0] = PLAYER_NAME_COLUMN;
			selection = PLAYER_NAME_COLUMN;
		} else if (table.equals(GodSimDBOpenHelper.GAMES_TABLE)) {
			columns[0] = GAME_COLUMN;
			selection = GAME_COLUMN;
		} else if (table.equals(GodSimDBOpenHelper.CIVS_TABLE)) {
			columns[0] = CIV_COLUMN;
			selection = CIV_COLUMN;
		} else {
			columns[0] = UNIT_COLUMN;
			selection = UNIT_COLUMN;
		}
		selection = selection + "=\"" + label + "\"";
		
		String selectionArgs[] = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		/* if there are any records in the cursor, the label is not unique */
		if (cursor.moveToFirst()) {
			System.err.println("Duplicate record found. Canceling add new record");
			return true;
		}
		
		System.err.println("Record is unique. OK to add to table");
		return false;
	}
	
	
	/* Called when we no longer need access to the database. */
	public void closeDatabase() {
		godSimDBOpenHelper.close();
	}
	
	private static class GodSimDBOpenHelper extends SQLiteOpenHelper {
		
		private static final String DB_NAME = "godSimDB.db";
		private static final String PLAYERS_TABLE = "PLAYERS_TABLE";
		private static final String GAMES_TABLE = "GAMES_TABLE";
		private static final String CIVS_TABLE = "CIVS_TABLE";
		private static final String UNITS_TABLE = "UNITS_TABLE";
		private static final int DB_VERSION = 1;
		    
		/* SQL Statement to create a players table */
		private static final String PLAYERS_CREATE = "create table " +
			PLAYERS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			PLAYER_NAME_COLUMN + " text not null, " +
			GAME_COLUMN + " text not null, " +
			PASSWORD_COLUMN + " text not null);";
		
		private static final String GAMES_CREATE =  "create table " +
			GAMES_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			GAME_COLUMN + " text not null, " +
			CIV_COLUMN + " text not null);";
		
		private static final String CIVS_CREATE =  "create table " +
			CIVS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			CIV_COLUMN + " text not null, " +
			UNIT_COLUMN + " text not null);";
		
		private static final String UNITS_CREATE =  "create table " +
			UNITS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			UNIT_COLUMN + " text not null, " +
			HEALTH_COLUMN + " text, " +
			X_COLUMN + " text, " +
			Y_COLUMN + " text);";
		
		public GodSimDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			System.err.printf(PLAYERS_CREATE);
		}
		
		/* Create the new database */
		@Override
		public void onCreate(SQLiteDatabase db) {
			System.err.println("creating new db");
			db.execSQL(PLAYERS_CREATE);
			db.execSQL(GAMES_CREATE);
			db.execSQL(CIVS_CREATE);
			db.execSQL(UNITS_CREATE);
			System.err.println(PLAYERS_CREATE);
			System.err.println(GAMES_CREATE);
			System.err.println(CIVS_CREATE);
			System.err.println(UNITS_CREATE);
			System.err.println("created new db");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
			System.err.println("upgrading db");
			/* Drop the existing table if it exists */
			db.execSQL("DROP TABLE IF IT EXISTS " + PLAYERS_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + GAMES_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + CIVS_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + UNITS_TABLE);
			
			System.err.println("old tables dropped");
			/* Create a new table */
			onCreate(db);
			System.err.println("replacement tables created");
			System.err.println("db upgraded");
		}
		
	}
}