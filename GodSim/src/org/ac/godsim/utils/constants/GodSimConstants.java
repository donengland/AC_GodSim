package org.ac.godsim.utils.constants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 12-December-2012
 */
public interface GodSimConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================
	
	public static final int CELL_WIDTH = 32;
	public static final int CELL_HEIGHT = CELL_WIDTH;
	
	public static final float gathererSpeed = 20.0f;
	public static final float warriorSpeed = 15.0f;
	
	public enum UNIT_STATUS{
		waiting, moving, fighting, thinking, gathering
	}
	
	public enum UNIT_COLOR{
		red, green, blue, yellow
	}

	// ===========================================================
	// Methods
	// ===========================================================

}
