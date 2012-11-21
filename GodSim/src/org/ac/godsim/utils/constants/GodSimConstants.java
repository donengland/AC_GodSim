package org.ac.godsim.utils.constants;

/**
 * (c) 2012 Don England
 *
 * @author Don England
 * @since 11-November-2012
 */
public interface GodSimConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================
	
	public static final int CELL_WIDTH = 32;
	public static final int CELL_HEIGHT = CELL_WIDTH;
	
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
