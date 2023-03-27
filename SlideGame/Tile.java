package SlideGame;

public class Tile 
{
	
	int scoreValue;
	
	TileType type;
	
	boolean isCombinable = false;
	boolean isMoveable = false;

	public Tile() 
	{
		//default is PointTile + 1 point
		
		type = TileType.PointTile;
		this.scoreValue = 1;
		isMoveable = true;
		isCombinable = true;
		
	}
	
	public Tile(TileType t)
	{
		this.type = t;
		
		switch(t)
		{
			case PointTile:
			scoreValue = 1;
			isMoveable = true;
			isCombinable = true;
				break;
			case BasicObstacle:
				break;
			case WildCard:
				isCombinable = true;
				break;
			case Bomb:
				isMoveable = true;
				break;
			case Shuffler:
				break;
			case Hole:
				break;
			default:
				break;
			
		}
		
		
	}

	
	public Tile(int scoreValue)
	{
		type = TileType.PointTile;
		this.scoreValue = scoreValue;
		isMoveable = true;
		isCombinable = true;
		
	}
	
	
	
	public int getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
		
	}
	public TileType getType() {
		return type;
	}
	public void setType(TileType type) {
		this.type = type;
	}
	public boolean isCombinable() {
		return isCombinable;
	}
	public void setCombinable(boolean isCombinable) {
		this.isCombinable = isCombinable;
	}
	public boolean isMoveable() {
		return isMoveable;
	}
	public void setMoveable(boolean isMoveable) {
		this.isMoveable = isMoveable;
	}


}
