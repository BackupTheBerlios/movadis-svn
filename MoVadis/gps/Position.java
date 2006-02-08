/*
 * Created on 04.02.2006
 *
 */
package gps;

import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Transform;

// import java.util.Date;

public class Position {
	private int degLon;
	private int degLat;
	
	private float minLon;
	private float minLat;
	private LongitudeDirection loDir;
	private LatitudeDirection laDir;
	
	public abstract static class GeoDirection {
		private char dir;
		protected GeoDirection(char c) {
			this.dir = c;
		}
		public String toString() {
			return ""+dir;
		}
	}
	
	public static final class LongitudeDirection extends GeoDirection {
		public static final LongitudeDirection WEST = new LongitudeDirection('W');
		public static final LongitudeDirection EAST = new LongitudeDirection('E');		
		
		private LongitudeDirection(char c) {
			super(c);
		}
	}

	public static final class LatitudeDirection extends GeoDirection {
		public static final LatitudeDirection NORTH = new LatitudeDirection('N');
		public static final LatitudeDirection SOUTH = new LatitudeDirection('S');
		
		private LatitudeDirection(char c) {
			super(c);
		}
	}
	
	public Position(int degLat, float minLat, LatitudeDirection laDir, int degLon, float minLon, LongitudeDirection loDir) {
		this.degLon = degLon;
		this.minLon = minLon;
		this.loDir = loDir;
		this.degLat = degLat;
		this.minLat = minLat;
		this.laDir = laDir;
	}

	public String getLatitudeAsString() {
		return degLat+"° "+minLat+"' "+laDir;
	}

	public String getLongitudeAsString() {
		return degLon+"° "+minLon+"'"+loDir;
	}
	
	public String toString() {
		return getLatitudeAsString()+"/"+getLongitudeAsString();
	}
	
	protected static float toDecimal(int deg, float min) {
		float result = 0;
		result += deg;
		result += min/60;
		return result;
	}
	
	public float getLatitudeAsFloat() {
		return toDecimal(this.degLat, this.minLat) * (this.laDir == LatitudeDirection.SOUTH ? -1 : 1); 
	}
	
	public float getLongitudeAsFloat() {
		return toDecimal(this.degLon, this.minLon) * (this.loDir == LongitudeDirection.EAST ? -1 : 1); 
	}
	
	public double distanceTo(Position p) {
		// not exact, but should be enough
		// distance in km = arccos(sin(a) * sin(c) + cos(a) * cos(c) * cos(min(b-d,360-(b-d)))) * 111
		double result = 0;
		float a = getLatitudeAsFloat();
		float b = getLongitudeAsFloat();
		
		float c = p.getLatitudeAsFloat();
		float d = p.getLongitudeAsFloat();
		
		// Great, there is no acos(...) in J2ME 
		// TODO Implement a real calculation
		
		return result;
	}
}
