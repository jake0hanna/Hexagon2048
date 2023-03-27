package SlideGame;

import java.awt.Point;

public class Pair 
{
	Tile tile;
	Point coord;
	
	Pair(Tile item1, Point item2)
	{
		tile = item1;
		coord = item2;
		
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public Point getCoord() {
		return coord;
	}

	public void setCoord(Point coord) {
		this.coord = coord;
	}



}
