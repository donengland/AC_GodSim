package org.ac.godsim.persistentdata;

import java.util.LinkedList;
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
	private static final String PLAYER_NAME_COLUMN =  "PLAYER_NAME_COLUMN";
	private static final String GAME_COLUMN = "GAME_COLUMN";
	private static final String PASSWORD_COLUMN = "PASSWORD_COLUMN";
	private static final String CIV_COLUMN = "CIV_COLUMN";
	private static final String UNIT_COLUMN = "UNIT_COLUMN";
	private static final String HEALTH_COLUMN = "HEALTH_COLUMN";
	private static final String X_COLUMN = "X_COLUMN";
	private static final String Y_COLUMN = "Y_COLUMN";
	private static final String SETTING_COLUMN = "SETTING_COLUMN";
	private static final String VALUE_COLUMN = "VALUE_COLUMN";
	
	private String thisPlayer;
	
	
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
	
	public static List<String> getAllRows(String table){ 
        List<String> labels = new ArrayList<String>(); 
  
        String selectAll = "SELECT * FROM " + table; 
  
        SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase(); 
        Cursor cursor = db.rawQuery(selectAll, null);  
        
        while (cursor.moveToNext()) {
			labels.add(cursor.getString(1));
		}

        cursor.close(); 
        db.close(); 
  
        return labels; 
    }
	
	/**
	 * Use this method to add a new Civ unit. You must provide a game number, which should
	 * be available via the class variable 'thisGame'. This will do two things:
	 * 1) update the games table to associate this new civ with an existing game.
	 * 2) add a record to the civs table with a null unit value.
	 * @param gameNum
	 */
	public static void addCiv(String gameNum) {
		
		int next;
		ContentValues newValues = new ContentValues();
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		
		/* get the maximum value in the civs table - this is the number to increment
		 * and assign to our new civ */
		Cursor cursor = db.query(GodSimDBOpenHelper.CIVS_TABLE, new String [] {"MAX("+CIV_COLUMN+") AS COL"}, null, null, null, null, null);
		
		/* Insert a row into the Civs table */
        if ( cursor.getCount() > 0 ) {
        	cursor.moveToFirst();
        	next = cursor.getInt(cursor.getColumnIndex("COL")) + 1;
        } else {next = 1;}
        
        newValues.put(CIV_COLUMN, next);
        newValues.put(UNIT_COLUMN, "");
		db.insert(GodSimDBOpenHelper.CIVS_TABLE, null, newValues);
        cursor.close();
        
        /* Update the Games table to reflect this new Civ */
        String where = GAME_COLUMN + " = " + gameNum + " AND " + CIV_COLUMN + " = " + next;
        
		//String[] whereArgs = new String[] {player};
		Cursor cursor2 = db.query(GodSimDBOpenHelper.GAMES_TABLE, new String[] {GAME_COLUMN, CIV_COLUMN}, where, null, null, null, null); 
        
		newValues.clear();
		newValues.put(GAME_COLUMN, gameNum);
		newValues.put(CIV_COLUMN, next);
		
		if (cursor2.getCount() > 0) {
			/* This is an existing record - update it with the new civ number */
			cursor2.moveToFirst();
			db.update(GodSimDBOpenHelper.GAMES_TABLE, newValues, where, null);
		} else {
			/* This is a new record - add to the games table */
			db.insert(GodSimDBOpenHelper.GAMES_TABLE, null, newValues);
		}	
	}
	
	/**
	 * Use this method to add new units to a particular civilization.
	 * You must supply the following variables
	 * @param civNum  The number of the civilization you are adding this unit to.
	 *                You can find this using the getCivs() method.
	 * @param health  integer value - usually controlled via the hud
	 * @param x       integer value
	 * @param y       integer value
	 */
	public static void addUnit(int civNum, int health, int x, int y) {
		int next;
		ContentValues newValues = new ContentValues();
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		
		/* get the maximum value in the units table - this is the number to increment
		 * and assign to our new unit */
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String [] {"MAX("+UNIT_COLUMN+") AS UNIT"}, null, null, null, null, null);
		
		/* Insert a row into the Units table */
        if ( cursor.getCount() > 0 ) {
        	cursor.moveToFirst();
        	next = cursor.getInt(cursor.getColumnIndex("UNIT")) + 1;
        } else {next = 1;}
        
        newValues.put(UNIT_COLUMN, next);
        newValues.put(HEALTH_COLUMN, health);
        newValues.put(X_COLUMN, x);
        newValues.put(Y_COLUMN, y);
		db.insert(GodSimDBOpenHelper.UNITS_TABLE, null, newValues);
        cursor.close();
        
        /* Update the Civs table to reflect this new Unit */
        String where = CIV_COLUMN + " = " + civNum + " AND " + UNIT_COLUMN + " = " + next;
        
		//String[] whereArgs = new String[] {player};
		Cursor cursor2 = db.query(GodSimDBOpenHelper.CIVS_TABLE, new String[] {CIV_COLUMN, UNIT_COLUMN}, where, null, null, null, null); 
        
		newValues.clear();
		newValues.put(CIV_COLUMN, civNum);
		newValues.put(UNIT_COLUMN, next);
		
		if (cursor2.getCount() > 0) {
			/* This is an existing record - update it with the new civ number */
			cursor2.moveToFirst();
			db.update(GodSimDBOpenHelper.CIVS_TABLE, newValues, where, null);
		} else {
			/* This is a new record - add to the games table */
			db.insert(GodSimDBOpenHelper.CIVS_TABLE, null, newValues);
		}	
	}
	
	private static void addGame(int g) {
		ContentValues newValues = new ContentValues();
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase(); 
		newValues.put(GAME_COLUMN, g);
		newValues.put(CIV_COLUMN, "");
		db.insert(GodSimDBOpenHelper.GAMES_TABLE, null, newValues);
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
	public static Boolean addPlayer (String player, String pw) {
		
		int nextGame = 1;
		System.err.println(player);
		/* Check to see if this record already exists */
		String where = PLAYER_NAME_COLUMN + "= ?";
		String[] whereArgs = new String[] {player};
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		Cursor exists = db.query(GodSimDBOpenHelper.PLAYERS_TABLE, new String[] {PLAYER_NAME_COLUMN}, where, whereArgs, null, null, null); 
		if ( exists.moveToFirst() ) {
			/* the record exists, cancel the operation */
			System.err.println("player was indeed found in the table");
			return false;
		}
		
		/* Create a new row of values to insert. */
		ContentValues newValues = new ContentValues();
		
		Cursor cursor = db.query(GodSimDBOpenHelper.GAMES_TABLE, new String [] {"MAX("+GAME_COLUMN+") AS GC"}, null, null, null, null, null);
		 
        if ( player != "Guest" && cursor.getCount() > 0) {
        	cursor.moveToFirst();
        	nextGame = cursor.getInt(cursor.getColumnIndex("GC")) + 1;
        	System.err.printf("next game: %d\n", nextGame);
        	addGame(nextGame);
        } else {
        	System.err.println("adding game #1");
        	addGame(1);
        }
        cursor.close();
        
        /* confirm new game entry */
        /* keep this commented in case it is useful later */
//        Cursor cursor2 = db.query(GodSimDBOpenHelper.GAMES_TABLE, new String [] {"MAX("+GAME_COLUMN+") AS GC"}, null, null, null, null, null);
//        if(cursor2.getCount() > 0) {
//        	cursor2.moveToFirst();
//        	String temp = cursor2.getString(cursor.getColumnIndex("GC"));
//        	System.err.println(temp);
//        	System.err.printf("new game added: %s\n", temp);
//        }
//    	
//        cursor2.close();
			  
		/* Assign values for each row */
		newValues.put(PLAYER_NAME_COLUMN, player);
		newValues.put(GAME_COLUMN, nextGame);
		newValues.put(PASSWORD_COLUMN, pw);
			
		/* Insert the row into the  table */
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
	 * Use this method to update values for a specific unit. This will typically be invoked
	 * when the user taps on one of the hud control arrows.
	 * @param unit
	 * @param health
	 * @param x
	 * @param y
	 * @return
	 */
	public static Boolean updateUnit(String unit, int health, int x, int y) {
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = UNIT_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {UNIT_COLUMN}, where, new String[] {unit}, null, null, null);
		if (cursor != null) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(UNIT_COLUMN, unit);
		updatedValues.put(HEALTH_COLUMN, health);
		updatedValues.put(X_COLUMN, x);
		updatedValues.put(Y_COLUMN, y);
		
		/* SQL query clauses */
		String whereClause = UNIT_COLUMN + "=\"" + unit + "\"";
		String whereArgs[] = null;
		
		db.update(GodSimDBOpenHelper.UNITS_TABLE, updatedValues, whereClause, whereArgs);
		
		return true;
	}
	
	/**
	 * Use this method to update values for a specific civ.
	 * Make sure to send the arguments as strings
	 * @param oldVal
	 * @param newVal
	 */
	public static Boolean updateCiv(int oldValue, int newValue) {
		String oldVal = Integer.toString(oldValue);
		String newVal = Integer.toString(newValue);
		System.err.printf("old val: %s, new val: %s\n", oldVal, newVal);
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = CIV_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.CIVS_TABLE, new String[] {CIV_COLUMN}, where, new String[] {oldVal}, null, null, null);
		
		if (!cursor.moveToNext()) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(CIV_COLUMN, newVal);
		
		/* SQL query clauses */
		String whereClause = CIV_COLUMN + "=\"" + oldVal + "\"";
		
		db.update(GodSimDBOpenHelper.CIVS_TABLE, updatedValues, whereClause, null);
		
		/* Now update the corresponding value in the GAMES_TABLE */
		db.update(GodSimDBOpenHelper.GAMES_TABLE, updatedValues, whereClause, null);
		
		return true;
	}
	
	/**
	 * Use this method to update the health value of a specific unit.
	 * @param oldVal
	 * @param newVal
	 */
	public static Boolean updateHealth (int unitVal, int oldValue, int newValue) {
		String unit = Integer.toString(unitVal);
		String oldVal = Integer.toString(oldValue);
		String newVal = Integer.toString(newValue);
		
		System.err.printf("old val: %s, new val: %s\n", oldVal, newVal);
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = UNIT_COLUMN + " = " + unit + " AND " + HEALTH_COLUMN + " = " + oldVal;
		//String where = CIV_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {UNIT_COLUMN}, where, null, null, null, null);
		
		if (!cursor.moveToNext()) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(HEALTH_COLUMN, newVal);
		db.update(GodSimDBOpenHelper.UNITS_TABLE, updatedValues, where, null);
		
		return true;
	}
	
	/**
	 * Use this method to update the X value of a specific unit.
	 * @param oldVal
	 * @param newVal
	 */
	public static Boolean updateX (int unitVal, int oldValue, int newValue) {
		String unit = Integer.toString(unitVal);
		String oldVal = Integer.toString(oldValue);
		String newVal = Integer.toString(newValue);
		
		System.err.printf("old val: %s, new val: %s\n", oldVal, newVal);
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = UNIT_COLUMN + " = " + unit + " AND " + X_COLUMN + " = " + oldVal;
		//String where = CIV_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {UNIT_COLUMN}, where, null, null, null, null);
		
		if (!cursor.moveToNext()) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(X_COLUMN, newVal);
		db.update(GodSimDBOpenHelper.UNITS_TABLE, updatedValues, where, null);
		
		return true;
	}
	
	/**
	 * Use this method to update the Y value of a specific unit.
	 * @param oldVal
	 * @param newVal
	 */
	public static Boolean updateY (int unitVal, int oldValue, int newValue) {
		String unit = Integer.toString(unitVal);
		String oldVal = Integer.toString(oldValue);
		String newVal = Integer.toString(newValue);
		
		System.err.printf("old val: %s, new val: %s\n", oldVal, newVal);
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = UNIT_COLUMN + " = " + unit + " AND " + Y_COLUMN + " = " + oldVal;
		//String where = CIV_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {UNIT_COLUMN}, where, null, null, null, null);
		
		if (!cursor.moveToNext()) {
			/* the record doesn't exist, cancel the operation */
			return false;
		}
		
		ContentValues updatedValues = new ContentValues();
		updatedValues.put(Y_COLUMN, newVal);
		db.update(GodSimDBOpenHelper.UNITS_TABLE, updatedValues, where, null);
		
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
		
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase();
		String where = PLAYER_NAME_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.PLAYERS_TABLE, new String[] {PLAYER_NAME_COLUMN}, where, new String[] {oldName}, null, null, null);
		if (!cursor.moveToNext()) {
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
		
		db.update(GodSimDBOpenHelper.PLAYERS_TABLE, updatedValues, whereClause, whereArgs);
		
		return true;
	}
	
	/** Use this method to retrieve all of the individual Civs associated with a 
	 * given game. The game is referenced by a unique integer that can be found by
	 * referencing the name of the player via the getGame() method. The data retrieve
	 * by this method is from the GAMES_TABLE.
	 * 
	 * @param game  integer variable referencing the game number. 
	 * @return  List<Strings> (these 'strings' can be parsed as integers)
	 */
	public static List<String> getCivs(String game) {
		List<String> civs = new LinkedList<String>();;
		
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = GAME_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.GAMES_TABLE, new String[] {CIV_COLUMN}, where, new String[] {game}, null, null, null);
		
		while (cursor.moveToNext()) {
			civs.add(cursor.getString(0));
		}
		
		return civs;
	}
	
	
	/** Use this method to retrieve all of the individual Units associated with a 
	 * given Civ. The Civ is referenced by a unique integer that can be found by
	 * using the getCivs(String game) method. 
	 * 
	 * @param game  integer variable referencing the game number. 
	 * @return  List<Strings> (these 'strings' can be parsed as integers)
	 */
	public static List<String> getUnits(String civ) {
		List<String> units = new LinkedList<String>();;
		
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = CIV_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.CIVS_TABLE, new String[] {UNIT_COLUMN}, where, new String[] {civ}, null, null, null);
		
		while (cursor.moveToNext()) {
			units.add(cursor.getString(0));
		}
		
		return units;
	}
	
	/** Use this method to retrieve the health level for a particular unit. 
	 * The unit can be found via the getUnits(String civ) method.
	 * Note that if this unit is not found, the method will return -1.
	 * 
	 * @param unit  integer variable referencing the unit number. 
	 * @return  integer value representing the health level.
	 */
	public static int getHealth (int unit) {
		String u = Integer.toString(unit);
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = UNIT_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {HEALTH_COLUMN}, where, new String[] {u}, null, null, null);
		
		if (cursor.moveToNext()) {return cursor.getInt(0);}
		
		/* If we get here, the unit does not exist. Return -1 value to indicate error. */
		return -1;
	}
	
	/** Use this method to retrieve the x coordinate for a particular unit. 
	 * The unit can be found via the getUnits(String civ) method.
	 * Note that if this unit is not found, the method will return -1.
	 * 
	 * @param unit  integer variable referencing the unit number. 
	 * @return  integer value representing the x coordinate.
	 */
	public static int getX (int unit) {
		String u = Integer.toString(unit);
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = UNIT_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {X_COLUMN}, where, new String[] {u}, null, null, null);
		
		if (cursor.moveToNext()) {return cursor.getInt(0);}
		
		/* If we get here, the unit does not exist. Return -1 value to indicate error. */
		return -1;
	}
	
	/** Use this method to retrieve the x coordinate for a particular unit. 
	 * The unit can be found via the getUnits(String civ) method.
	 * Note that if this unit is not found, the method will return -1.
	 * 
	 * @param unit  integer variable referencing the unit number. 
	 * @return  integer value representing the x coordinate.
	 */
	public static int getY (int unit) {
		String u = Integer.toString(unit);
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = UNIT_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.UNITS_TABLE, new String[] {Y_COLUMN}, where, new String[] {u}, null, null, null);
		
		if (cursor.moveToNext()) {return cursor.getInt(0);}
		
		/* If we get here, the unit does not exist. Return -1 value to indicate error. */
		return -1;
	}
	
	public static String getLastPlayer() {
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = SETTING_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.SETTINGS_TABLE, new String[] {VALUE_COLUMN}, where, new String[] {"lastPlayer"}, null, null, null);
		if (cursor.moveToNext()) {return cursor.getString(0);}
		System.err.println("in GodSimDB.getLastPlayer, no last player found");
		return "Guest";
	}
	
	/** Use this method to retrieve the number of the game associated with a 
	 * given player. The player name is the String variable that matches the 
	 * name in the spinner.
	 * @param player  String variable matching the name of the player in the spinner. 
	 * @return
	 */
	public static int getGame(String player) {
		String temp = getOneValue(GodSimDBOpenHelper.PLAYERS_TABLE, PLAYER_NAME_COLUMN, player, GAME_COLUMN);
		System.err.printf("game %s\n", temp);
		return Integer.parseInt(temp);
	}
	
	public static String getOneValue (String table, String searchCol, String searchVal, String returnCol) {
		
		String[] resultColumns = new String[] {returnCol};
		String where = searchCol + "= ?";
		String whereArgs[] = new String[] { searchVal };
		
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		System.err.println("here");
		Cursor cursor = db.query(table, resultColumns, where, whereArgs, null, null, null);
		
		if (cursor.moveToFirst()) {
			
			String val = cursor.getString(cursor.getColumnIndex(returnCol));
			System.err.printf("test: %s\n", val);
			cursor.close();
			return val;
		}
		cursor.close();
		return "record not found";
	}
	
	/** 
	 * Returns the true if the currently exists in the DB.
	 * If the player does not exist, returns false.
	 * @param p
	 * @return
	 */
	public static Boolean isNewPlayer (String p) {
		SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
		String where = PLAYER_NAME_COLUMN + "= ?";
		Cursor cursor = db.query(GodSimDBOpenHelper.PLAYERS_TABLE, new String[] {PLAYER_NAME_COLUMN}, where, new String[] {p}, null, null, null);
		if (cursor != null) {
			System.err.println("isNewPlayer says: false");
			return false;
		}
		System.err.println("isNewPlayer says: true");
		return true;
	}
	
	public static List<String> getSettings() {
		/* Note that there is only a single row in this table, so querying 'all' rows is OK */
		return getAllRows("SETTINGS_TABLE");
	}
	
	/** 
	 * Use this method to change default settings that you want to use the next time
	 * the game re-loads. These settings will be saved and can then be recalled at any time.
	 * For example: the default player to display at startup.
	 * @param settingType  Legal Values are {"lastPlayer"}
	 * @param value        This is the value you want to store
	 */
	public static void updateSetting(String setting, String newVal) {
		System.err.printf("setting: %s, newVal: %s\n", setting, newVal);
		String table = GodSimDBOpenHelper.SETTINGS_TABLE;
		String[] resultColumn = new String[] {VALUE_COLUMN};
		String where = SETTING_COLUMN + "= ?";
		String whereArgs[] = new String[] { setting };
		
		/* Get existing settings */
		//String currentValue = null;
		SQLiteDatabase db = godSimDBOpenHelper.getWritableDatabase(); 
		Cursor cursor = db.query(table, resultColumn, where, whereArgs, null, null, null);
		
		ContentValues newValue = new ContentValues();
		newValue.put(SETTING_COLUMN, setting);
		newValue.put(VALUE_COLUMN, newVal);
		
        if (cursor.moveToNext()) { /* record exists - we need to update it */
    		db.update(table, newValue, where, whereArgs);
    		System.err.printf("lastplayer updated to: %s\n", newVal);
        } else { /* record does not exist, add it */
    		db.insert(table, null, newValue);
        }
        cursor.close();
        db.close();
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
		private static final String SETTINGS_TABLE = "SETTINGS_TABLE";
		private static final int DB_VERSION = 1;
		    
		/* SQL Statement to create a players table */
		private static final String PLAYERS_CREATE = "create table " +
			PLAYERS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			PLAYER_NAME_COLUMN + " text not null, " +
			GAME_COLUMN + " text not null, " +
			PASSWORD_COLUMN + " text);";
		
		private static final String GAMES_CREATE =  "create table " +
			GAMES_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			GAME_COLUMN + " text not null, " +
			CIV_COLUMN + " text);";
		
		private static final String CIVS_CREATE =  "create table " +
			CIVS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			CIV_COLUMN + " text not null, " +
			UNIT_COLUMN + " text);";
		
		private static final String UNITS_CREATE =  "create table " +
			UNITS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			UNIT_COLUMN + " text not null, " +
			HEALTH_COLUMN + " text, " +
			X_COLUMN + " text, " +
			Y_COLUMN + " text);";
		
		private static final String SETTINGS_CREATE = "create table " +
			SETTINGS_TABLE + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			SETTING_COLUMN + " text, " +
			VALUE_COLUMN + " text);"; 
				
		public GodSimDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		@SuppressWarnings("unused")
		protected static String getTableName (String t) {
			if (t == "defaultplayer") {return PLAYERS_TABLE;}
			return "table not found";
		}
		
		/* Create the new database */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(PLAYERS_CREATE);
			db.execSQL(GAMES_CREATE);
			db.execSQL(CIVS_CREATE);
			db.execSQL(UNITS_CREATE);
			db.execSQL(SETTINGS_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
			System.err.println(SETTINGS_CREATE);
			/* Drop the existing table if it exists */
			db.execSQL("DROP TABLE IF IT EXISTS " + PLAYERS_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + GAMES_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + CIVS_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + UNITS_TABLE);
			db.execSQL("DROP TABLE IF IT EXISTS " + SETTINGS_TABLE);
			
			/* Create new tables */
			onCreate(db);
		}
		
	}
	
	
//	private static Boolean recordExists(String table, String label) {
//	/* note that switch statement doesn't work with Java 1.6 */
//	String columns[] = new String[1];
//	String selection;
//	
//	/* Select the parts of the query that are dependent upon which 
//	 * table we are looking at 
//	 * */
//	if (table.equals(GodSimDBOpenHelper.PLAYERS_TABLE)) {
//		columns[0] = PLAYER_NAME_COLUMN;
//		selection = PLAYER_NAME_COLUMN;
//	} else if (table.equals(GodSimDBOpenHelper.GAMES_TABLE)) {
//		columns[0] = GAME_COLUMN;
//		selection = GAME_COLUMN;
//	} else if (table.equals(GodSimDBOpenHelper.CIVS_TABLE)) {
//		columns[0] = CIV_COLUMN;
//		selection = CIV_COLUMN;
//	} else {
//		columns[0] = UNIT_COLUMN;
//		selection = UNIT_COLUMN;
//	}
//	selection = selection + "=\"" + label + "\"";
//	
//	String selectionArgs[] = null;
//	String groupBy = null;
//	String having = null;
//	String orderBy = null;
//	
//	SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
//	Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
//	
//	/* if there are any records in the cursor, the label is not unique */
//	if (cursor.moveToFirst()) {
//		System.err.println("Duplicate record found. Canceling add new record");
//		return true;
//	}
//	cursor.close();
//	
//	System.err.println("Record is unique. OK to add to table");
//	return false;
//}
// */
//public static Cursor getRecord (String table, String[] result_columns, String where, String[] where_args) {
//	
//	//String selectionArgs[] = selArgs;
//	
//	
//	SQLiteDatabase db = godSimDBOpenHelper.getReadableDatabase();
//	Cursor cursor = db.query(table, result_columns, where, where_args, null, null, null);
//	
//	return cursor;
//}
	
}