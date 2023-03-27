package SlideGame;

import java.awt.Point;
import java.awt.PointerInfo;
import java.util.ArrayList;
 import java.util.concurrent.ThreadLocalRandom;

public class GameInstance 
{
	int turnCount, 
	score,
	boardSize, 
	boardArraySize,
	amountOfStartingTiles = 5, 
	bombRadius = 1,
	defaultAmountOfHoles = boardSize*3, 
	defaultAmountOfObstacles = boardSize*3;
	
	boolean 
	randomizeObstacles, 
	randomizeHoles, 
	enableSpecialTile;
	
	Tile[][] QRSboard;
	ArrayList<GameRules> rules;
	int[] types;
	double[] odds;

	private static final int[][] snowflakeCoordinates =
		{
		{+1, -2},
		{-1, -1},
		{-2, 1},
		{-1, 2},
		{1, 1},
		{+2, -1}
		};

	private double[] probabilities;
	AliasMethod rng;

	public GameInstance(int size, ArrayList<GameRules> inputRules) //ADD RULES INPUT FROM UI
	{
		

		this.boardSize = size;
		this.boardArraySize =  size*2+1;
		
		QRSboard = new Tile[boardArraySize][boardArraySize];
		
		rules = inputRules;
		
		//RULES SETUP
	


		if(getRules().contains(GameRules.SpecialEffects))
		{
			enableSpecialTile = true;
		}

		probabilitiesSetup();

		initializeObstacles();

		initializeHoles();

		intiializeTiles();

		
		rules.add(GameRules.ActivateEffectsCombinedAgainst);
		
		Pair p;
		
		

		
		
		
	}
	
	public ArrayList<Pair> refreshBoard() 
	{
		ArrayList<Pair> tilesAndLocations = new ArrayList<Pair>();
		
		for(int x = -boardSize; x < boardSize+1; x++) 
		{
			for(int y = -boardSize; y < boardSize+1; y++) 
			{	
				
					
					
					
						if(isThereATile(x, y))
						{
							tilesAndLocations.add(new Pair(getTileAt(x,y)
							, new Point(x,y)));
							
						}

					
				
					
				
				
			}
			
		}


	
		
		return tilesAndLocations;
		
		
	}
	
 	public ArrayList<Point> ringCheck(int centerQ, int centerR, int radius) 
	{
		
		ArrayList<Point> Locations = new ArrayList<Point>();
		
		//find edge
		//move [radius] tiles in direction from edge
		//switch directions and move again
		


		int searchQ = centerQ - radius;
		int searchR = centerR + radius;
		
		
		for(DirectionVector direction : DirectionVector.values()) 
		{
			for(int y = 0; y < radius; y++)
			{
				Locations.add(new Point(
						searchQ += direction.getQ(),
						searchR += direction.getR()));
				
				
			}	

		}
		
		
		
		return Locations;
		
	}

	public void directionallyMoveAllTiles(DirectionVector movementDirection) 
	{

		
		//Start iteration at max distance of the chosen direction
		int startQ = movementDirection.getQ() * boardSize;
		int startR = movementDirection.getR() * boardSize;
		
		int iterationQ1 = Integer.valueOf(startQ);
		int iterationR1 = Integer.valueOf(startR);

		int iterationQ2 = Integer.valueOf(startQ);
		int iterationR2 = Integer.valueOf(startR);
		
		DirectionVector direction1 = null, direction2 = null;

		
		//Set directions to iterate based on movement direction
		switch(movementDirection)
		{ 
		
		//-60 -s -q -r
		// q r s
		//+60 -r -s -q
		
		
			case EAST:
			{
				direction1 = DirectionVector.NORTHWEST;
				direction2 = DirectionVector.SOUTHWEST;
				break;
				
			}
			case NORTHEAST:
			{
				//west + se
				direction1 = DirectionVector.WEST;
				direction2 = DirectionVector.SOUTHEAST;
				break;
			}
			case NORTHWEST:
			{
				//east + sw
				direction1 = DirectionVector.EAST;
				direction2 = DirectionVector.SOUTHWEST;
				break;
			}
			case WEST:
			{
				//ne+se
				direction1 = DirectionVector.NORTHEAST;
				direction2 = DirectionVector.SOUTHEAST;
				break;
			}
			case SOUTHWEST:
			{
				//east+nw
				direction1 = DirectionVector.EAST;
				direction2 = DirectionVector.NORTHWEST;
				break;
			}
			case SOUTHEAST:
			{
				//ne +west
				direction1 = DirectionVector.NORTHEAST;
				direction2 = DirectionVector.WEST;
				break;
			}
		
		}
				
		
		for(int iterationWIDTH = boardSize*2+1; iterationWIDTH > 0; iterationWIDTH--)
		{ //iterate through the whole width of the board

			attemptToMoveTile(startQ, startR, startQ+movementDirection.getQ(), startR+movementDirection.getR());
	
				//iterate in both directions and move each tile
				for(int i = 0;  i < boardSize; i++)
				{

					iterationQ1 = iterationQ1 + direction1.getQ();
					iterationR1 = iterationR1 + direction1.getR();

					iterationQ2 = iterationQ2 + direction2.getQ();
					iterationR2 = iterationR2 + direction2.getR();

					

					//First move the iteration q to the first tiles
					//then move the tile in the chosen direction
					//repeat for the size of the board
					//reset the iteration q and then subtract the movement direction
					attemptToMoveTile(
							iterationQ1,
							iterationR1,
							
							
							iterationQ1+movementDirection.getQ(),
							iterationR1+movementDirection.getR()						
							);
					
					attemptToMoveTile(
							iterationQ2,
							iterationR2, 
							
							iterationQ2+movementDirection.getQ(),
							iterationR2+movementDirection.getR()
							
							);
										
					
				}
				//reset iteration values subtract vector
			
				startQ -= movementDirection.getQ();
				startR -= movementDirection.getR();


				iterationQ1 = startQ;
				iterationR1 = (startR);
				
				iterationQ2 = (startQ);
				iterationR2 = (startR);

			
	
				//once at 0,0,0 repeat the process but count down ON zero to the radius value
				//ALTERNATIVELY HAVE THE TILEMOVE FUNCTION HANDLE WHAT HAPPENS WITH INVALID/WEIRD MOVES
			
		}
		
		
		
		
	
		
	}
	
	public void shuffleTiles() {
		ArrayList<Pair> startingTiles = refreshBoard();
		ArrayList<Pair> newLocations = new ArrayList<Pair>();
		ArrayList<Point> potentialCoords = new ArrayList<Point>();
	
		System.out.println("Starting tiles: " + startingTiles);
	
		for (int i = 0; i < startingTiles.size(); i++) {
			Tile currentTile = startingTiles.get(i).getTile();
			Point currentCoord = startingTiles.get(i).getCoord();
	
			if (currentTile.getType() == TileType.BasicObstacle && !isARule(GameRules.ShuffleObstacles)) {
				potentialCoords.add(currentCoord);
				newLocations.add(new Pair(currentTile, currentCoord));
			} else {
				Point newCoord;
				do {
					newCoord = randomValidEmptyCoordinate();
				} while (potentialCoords.contains(newCoord));
	
				potentialCoords.add(newCoord);
				newLocations.add(new Pair(currentTile, newCoord));
			}
		}
	
		System.out.println("New locations: " + newLocations);
	
		Tile[][] newBoard = new Tile[boardArraySize][boardArraySize];
	
		for (int i = 0; i < newLocations.size(); i++) {
			Point oldCoord = startingTiles.get(i).getCoord();
			Point newCoord = newLocations.get(i).getCoord();
			Point arrayCoord = convertToArrayCoord((int) newCoord.getX(), (int) newCoord.getY());
	
			newBoard[(int) arrayCoord.getX()][(int) arrayCoord.getY()] = getTileAt((int) oldCoord.getX(), (int) oldCoord.getY());
		}
	
		this.setQRSBoard(newBoard);
	}
	
	
	
	public void intiializeTiles()
	{
		for(int i = 0; i < amountOfStartingTiles; i++)
		{
			Pair p = generateNewTile(false);
			putTileAt(p.getTile(), (int) p.getCoord().getX(), (int)p.getCoord().getY());
		}

	}

	public void initializeObstacles()
	{
		if(rules.contains(GameRules.Obstacles))
		{
			if(rules.contains(GameRules.ObstaclesWalls))
			{
				for(int i = 2; i <= boardSize; i+=2)
				{
					for(DirectionVector d : DirectionVector.values())
					{
						putTileAt(new Tile(TileType.BasicObstacle), i*d.getQ(), i*d.getR());
					}

				}

			}
			if(rules.contains(GameRules.RandomObstacles) || rules.contains(GameRules.ProgressiveObstacles))
			{

				for(int i = 0; i < defaultAmountOfObstacles; i++)
				{
					Pair p = generateNewTile(TileType.BasicObstacle);
					putTileAt(p.getTile(), (int) p.getCoord().getX(), (int)p.getCoord().getY());

				}

			}

		}

	}

	public void initializeHoles()
	{
		
		if(rules.contains(GameRules.Holes))
		{
			if(rules.contains(GameRules.SnowflakeHoles))
			{	
				for(int i = 1; i < boardSize; i+=2)
				{
					if(i % 3 == 0)
					{
						for(DirectionVector d : DirectionVector.values())
						{
							putTileAt(new Tile(TileType.BasicObstacle), i*d.getQ(), i*d.getR());
						}
					}
					else
					{
						for(int p = 0; p < 2; p++)
						{
							for(int j = 0; j<6; j++)
							{
								putTileAt(new Tile(TileType.Hole), snowflakeCoordinates[j][p], snowflakeCoordinates[j][p]);
							}
						}
				}
	
			}
				
			}
			if(rules.contains(GameRules.RandomHoles) || rules.contains(GameRules.ProgressiveHoles) || rules.contains(GameRules.ConditionalHoles))
			{
				for(int i = 0; i < defaultAmountOfHoles; i++)
				{
					Pair p = generateNewTile(TileType.Hole);
					putTileAt(p.getTile(), (int) p.getCoord().getX(), (int)p.getCoord().getY());

				}

			}

		}

	}

	//MOVEMENT AND INTERACTION RULES/CHECK
	public void attemptToMoveTile(int fromQ, int fromR, int newQ, int newR) 
	{
		//NEEDS NULLCHECK
		//(
		//once at 0,0,0 repeat the process but count down ON zero to the radius value
		//ALTERNATIVELY HAVE THE TILEMOVE FUNCTION HANDLE WHAT HAPPENS WITH INVALID/WEIRD MOVES
		//)
		
		
		//Also needs to check if the position on the board is legal
		
		//Different settings for what happens when a tile reaches the edge or 
		// attempts to move onto the same space as another tile 
		
		//check if space is legal, check if tile exists, check if new location is legal, check if occupied
		
				
		if(isWithinBoard(fromQ,fromR)) 
		{
			if(isThereATile(fromQ, fromR) && (getTileAt(fromQ, fromR).isMoveable()))
			{			
					if(isWithinBoard(newQ,newR)) 
					{
						
						if(!isThereATile(newQ,newR))//IS EMPTY?
						{
							validToEmptyMove(fromQ, fromR, newQ, newR);
						
						}
						else if((isCombinable(fromQ, fromR) && isCombinable(newQ, newR)) 
								&& (isARule(GameRules.EdgeOfBoardCombine)))
						{
							
							combineTiles(fromQ, fromR, newQ, newR);
							if(isARule(GameRules.ActivateEffectsCombinedAgainst))
							{
								DirectionVector d = getDirection(fromQ, fromR, newQ, newR);
								if(isWithinBoard(newQ+d.getQ(), newR+d.getR()))
								{
									if(isThereATile(newQ+d.getQ(), newR+d.getR()))
									{
										if(getTileAt(newQ+d.getQ(), newR+d.getR()).getType().equals(TileType.Bomb)
												|| getTileAt(newQ+d.getQ(), newR+d.getR()).getType().equals(TileType.Shuffler)
												|| getTileAt(newQ+d.getQ(), newR+d.getR()).getType().equals(TileType.WildCard))
										{
											activateTile(newQ+d.getQ(), newR+d.getR());
										}
									}
									
								}
							}
						
							
						}
						else if(isARule(GameRules.Obstacles)) 
						{
							if(getTileAt(newQ, newR).getType().equals(TileType.BasicObstacle))
							{
								
							}
						/*	else if(getTileAt(newQ, newR, newS).getType().equals(TileType.ObstacleA))
							{
								
							}
							else if(getTileAt(newQ, newR, newS).getType().equals(TileType.ObstacleB))
							{
								
							}
						*/	
							
						}
						else if((isARule(GameRules.Holes) && getTileAt(newQ, newR).getType().equals(TileType.Hole))
								|| (isAtEdgeOfBoard(fromQ, fromR) && !isWithinBoard(newQ, newR)))
						{
							if(isARule(GameRules.EdgeOfBoardDelete)) 
							{
						
								deleteTileAt(newQ, newR);
								
								
							}
							else if(isARule(GameRules.EdgeOfBoardWrapAround))
							{
								//IDK MAN, HEXES ARENT MEANT TO WRAP
								
							}
							
						}
						
						
						
					}//IF NOT WITHIN BOARD BUT ORIGINAL SPACE/TILE EXISTS
					else if(isARule(GameRules.EdgeOfBoardDelete))
					{
						
						deleteTileAt(fromQ, fromR);
						
						
					}
					else if(isARule(GameRules.EdgeOfBoardWrapAround))
					{
						
						// MODULO
						
					}
					//IF NO RULE IS ACTIVE TILE SHOULD REMAIN STATIONARY
				
			}

		
		}
		
		
	}	
	
	private DirectionVector getDirection(int fromQ, int fromR, int newQ, int newR) 
	{
		Point p = new Point(newQ-fromQ, newR-fromR);
		
		DirectionVector d;

		for(int i = 0; i < DirectionVector.values().length; i++)
		{
			if(DirectionVector.values()[i].getQ() == p.getX() && DirectionVector.values()[i].getR() == p.getY())
			{
				d = DirectionVector.values()[i];
				return d;
			}
		}
		

		return null;

	}

	public Pair generateNewTile(boolean genericTile)//needs work 
	{
		Tile t = new Tile();
		
		if(genericTile)
		{
			t = new Tile();
		}
		else
		{
		 t = new Tile(randomType());
		}
		
		return new Pair(t, randomValidEmptyCoordinate());
		
	}

	public void probabilitiesSetup()
	{
/*
 * 	
	0 PointTile, 
	1 BasicObstacle,
	2 WildCard,
	3 Bomb,
	4 Shuffler,	
	5 Hole,
	6 BackgroundTile,

 */

		
		probabilities = new double[TileType.values().length-1];

		//default probabilities :: 2048 default
		
		if(rules.containsAll(RuleSet.ClassicSlideGame.getRules()))
		{

			probabilities[0] = 0.89;
			probabilities[1] = 0;
			probabilities[2] = 0.01;
			probabilities[3] = 0.05;
			probabilities[4] = 0.05;
			probabilities[5] = 0.0;

		}
		else if(rules.containsAll(RuleSet.Lockout.getRules()))
		{

			probabilities[0] = 0.7;
			probabilities[1] = 0.2;
			probabilities[2] = 0.25;
			probabilities[3] = 0.25;
			probabilities[4] = 0.2499;
			probabilities[5] = 0.0001;

		}
		else if(rules.containsAll(RuleSet.Speelflake.getRules()))
		{

			probabilities[0] = 1;
			probabilities[1] = 0.0;
			probabilities[2] = 0.0;
			probabilities[3] = 0.0;
			probabilities[4] = 0.0;
			probabilities[5] = 0.0;


		}
		else if(rules.containsAll(RuleSet.TwentyFortyEight.getRules()))
		{

			probabilities[0] = 0.9999;
			probabilities[1] = 0.0;
			probabilities[2] = 0.0001;
			probabilities[3] = 0.0;
			probabilities[4] = 0.0;
			probabilities[5] = 0.0;

		}
		
		rng = new AliasMethod(probabilities);		


	}

	public TileType randomType()
	{
		
        int randomNumber = rng.sample();

		return TileType.values()[randomNumber];

	}
	
	public Pair generateNewTile(TileType type)//needs work 
	{
		Tile t = new Tile(type);
		
		return new Pair(t, randomValidEmptyCoordinate());
		
	}
	
	public ArrayList<Pair> inputAction(Actions act)
	{
		switch(act)
		{
			case EAST:
				directionallyMoveAllTiles(DirectionVector.EAST);
				break;
			case NORTHEAST:
				directionallyMoveAllTiles(DirectionVector.NORTHEAST);
				break;
			case NORTHWEST:
				directionallyMoveAllTiles(DirectionVector.NORTHWEST);
				break;
			case SOUTHEAST:
				directionallyMoveAllTiles(DirectionVector.SOUTHEAST);
				break;
			case SOUTHWEST:
				directionallyMoveAllTiles(DirectionVector.SOUTHWEST);
				break;
			case WEST:
				directionallyMoveAllTiles(DirectionVector.WEST);
				break;
			default:
				break;
		
		}
		if(checkGameOver()){
			System.out.print(true);
			ArrayList<Pair> a = new ArrayList<Pair>();
			a.add(new Pair(new Tile(score), new Point(0,0)));
			return a;
		}

		Pair p;
		if(isARule(GameRules.SpecialEffects))
		{
			p= generateNewTile(false);
		}
		else
		{
			p= generateNewTile(true);
		}

		putTileAt(p.getTile(),(int) p.getCoord().getX(), (int)p.getCoord().getY());

		

		return refreshBoard();
	}

	public ArrayList<Pair> inputAction(Actions act, int x, int y) 
	{
		switch(act)
		{
			case TILEACTIVATE:
				activateTile(x,  y);
				break;
			case EAST:
				directionallyMoveAllTiles(DirectionVector.EAST);
				break;
			case NORTHEAST:
				directionallyMoveAllTiles(DirectionVector.NORTHEAST);
				break;
			case NORTHWEST:
				directionallyMoveAllTiles(DirectionVector.NORTHWEST);
				break;
			case SOUTHEAST:
				directionallyMoveAllTiles(DirectionVector.SOUTHEAST);
				break;
			case SOUTHWEST:
				directionallyMoveAllTiles(DirectionVector.SOUTHWEST);
				break;
			case WEST:
				directionallyMoveAllTiles(DirectionVector.WEST);
				break;
			default:
				break;
		
		}
		
		Pair perTurn = generateNewTile(false);
		putTileAt(perTurn.getTile(), (int)perTurn.getCoord().getX(), (int)perTurn.getCoord().getY());
	
		turnCount++;
		return refreshBoard();
		
	}
	
		
	//Direct Tile Manipulation
	public void validToEmptyMove(int q1, int r1, int q2, int r2) 
	{
		Point c2 = convertToArrayCoord(q2, r2);
		QRSboard[(int)c2.getX()][(int)c2.getY()] = getTileAt(q1, r1);
		deleteTileAt(q1, r1);	
		
	}

	public void activateTile(int q, int r)
	{
		//logic for each special effect type
		
		switch(getTileAt(q,r).getType())
		{
			case Shuffler:
			{
				deleteTileAt(q, r);
				shuffleTiles();
				break;
			}
			case Bomb:
			{
				ArrayList<Point> kaboom = ringCheck(q, r, bombRadius);
				
				deleteTileAt(q, r);

				for(Point p : kaboom)
				{
					if(isWithinBoard((int)p.getX(),(int) p.getY()))
					{
						if(isThereATile((int)p.getX(), (int)p.getY()) 
						&& getTileAt((int)p.getX(),(int)p.getY()).getType().equals(TileType.BasicObstacle))
						{
							if(isARule(GameRules.DestructableObstacles))
								deleteTileAt((int)p.getX(), (int)p.getY());
						}
						else deleteTileAt((int)p.getX(), (int)p.getY());
					}
				}
				break;
			}
		default:
			break;
			
		}
			
		
	}
	
	public void combineTiles(int q1, int r1, int q2, int r2)
	{
		//ORDER MATTERS
		//COMBINATION LOGIC
		//TWO POINTS TILES? ONE POINT TILE AND ONE EFFECT TILE?
		
		Tile a = getTileAt(q1,r1);
		Tile b = getTileAt(q2,r2);
		
		
		if(a.isCombinable() && b.isCombinable())
		{
			
			if(a.getType().equals(TileType.PointTile) && b.getType().equals(TileType.PointTile)) //double tile value and delete first
			{
				
				if(a.getScoreValue() == b.getScoreValue()) 
				{
					
					b.setScoreValue(b.getScoreValue()*2);
					deleteTileAt(q1,r1);
					
					
				}
				
				
			}
			else if( (a.getType().equals(TileType.PointTile) && b.getType().equals(TileType.WildCard))
					|| a.getType().equals(TileType.WildCard) && b.getType().equals(TileType.PointTile))
					{
						
						if(a.getScoreValue() != 0) 
						{
							getTileAt(q2,r2).setScoreValue(a.getScoreValue()*4);
						}
						else
						{
							getTileAt(q2,r2).setScoreValue(b.getScoreValue()*4);
						}
						
						deleteTileAt(q1,r1);
						getTileAt(q2,r2).setType(TileType.PointTile);;
						getTileAt(q2, r2).setMoveable(true);
						
						
					}
				
				
			
			
			
		}
		
		
		
		
	}
				
	//Forcibly set a tile
	public void putTileAt(Tile t, int q, int r)
	{
		
		Point coord = convertToArrayCoord(q, r);
		QRSboard[(int)coord.getX()][(int)coord.getY()] = t;
		
		
	}


	
	public boolean isThereATile(int q, int r)
	{

		if(getTileAt((int)q,(int)r) == (null))
		{
			return false;
		}
		else return true;
	}
	
	public Tile getTileAt(int q, int r)
	{
		
		Point coord = convertToArrayCoord(q, r);

		

		return QRSboard[(int) coord.getX()][(int)coord.getY()];
	
		
	}

	public void deleteTileAt(int q, int r)
	{
		Point c = convertToArrayCoord(q, r);
		QRSboard[(int)c.getX()][(int)c.getY()] = null;
	}

	
	public boolean isCombinable(int q, int r) 
	{
		
		return getTileAt(q, r).isCombinable();
		
	}

	public Point randomValidEmptyCoordinate()
	{
		Point c =  randomCoord();

		int i = 0;

		while((isThereATile((int)c.getX() ,(int)c.getY())))
			{
				c =  randomCoord();
				i++;
			}
		
		
		return c;
		
		
	}

	//Not amazing but always valid
	public Point randomCoord()
	{

		Point c = new Point();
    int length, totalDirections;
    DirectionVector currentDirection;

    do {
        length = ThreadLocalRandom.current().nextInt(0, boardSize + 1);
        totalDirections = ThreadLocalRandom.current().nextInt(0, length + 1);
        currentDirection = DirectionVector.values()[ThreadLocalRandom.current().nextInt(0, 6)];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < totalDirections; j++) {
                currentDirection = DirectionVector.values()[ThreadLocalRandom.current().nextInt(0, 6)];
            }

            c.setLocation(c.getX() + currentDirection.getQ(),
                          c.getY() + currentDirection.getR());
        }
    } while (!isWithinBoard((int) c.getX(), (int) c.getY()));

		return c;
		
		
	}


	/*public int getDistance(int q1, int r1, int q2, int r2)
	{
		
		return ((Math.abs(q1-q2) + Math.abs(r1-r2) + Math.abs(s1-s2)) /2);
		
		
	}*/
	
	public boolean checkGameOver()
	{
		//iterate through board
		//if full of tiles, return true
		//else return false

		for(int i = -boardSize; i < boardSize; i++)
		{
			for(int j = -boardSize; j < boardSize; j++)
			{
				
					if(!isWithinBoard(i,j))
						if(getTileAt(i,j) == (null))
						return false;
				
			}
		}

		return true;

	}

	public void endGame() 
	{
		for(int i = -boardSize; i < boardSize; i++)
		{
			for(int j = -boardSize; j < boardSize; j++)
			{
				
					if(!isWithinBoard(i,j))
						score += getTileAt(i,j).getScoreValue();
				
			}
		}

	}

	public Point convertToArrayCoord(int q, int r)
	{//Assumes the point is valid

		int x = q + (boardSize);
		int y = r + (boardSize);
		

		return new Point(x,y);


	}

	public Point convertFromArrayCoord(int x, int y)
	{//Assumes the point is valid

		int q = x - (boardSize);
		int r = y - (boardSize);
		

		return new Point(q,r);
	}

	//getters setters

	public void setQRSBoard(Tile[][] board)
	{
		QRSboard = board;
	}

	public boolean isWithinBoard(int q, int r) {
		if(q >= -boardSize && q <= boardSize && r >= -boardSize && r <= boardSize && -q-r >= -boardSize && -q-r <= boardSize &&
				(q+r+(-q-r) == 0)
				/*&&
				(
					((q >= 0 && r <= 0) || (r >= 0 && q <= 0))
				&&
					(q == 0 && Math.abs(r)+Math.abs(-q-r) <= boardSize)
					||
					(r == 0 && Math.abs(q)+Math.abs(-q-r) <= boardSize)
					||
					(Math.abs(-q-r) == 0 && Math.abs(q)+Math.abs(r) <= boardSize)

				)*/

				)
				return true;
		else return false;
		
		
		//if Abs(x) = Abs(y) and z = 0 , is valid
		//if Abs(x) + abs(y) = abs(z), is valid
		
	}
	
	public boolean isAtEdgeOfBoard(int q, int r)
	{

		if(
			(Math.abs(q)+Math.abs(r) == Math.abs(-q-r) && Math.abs(q)+Math.abs(r) == boardSize)
			||
			(Math.abs(q)+Math.abs(-q-r) == Math.abs(r) && Math.abs(q)+Math.abs(-q-r) == boardSize)
			||
			((Math.abs(-q-r)+Math.abs(r) == Math.abs(q) && Math.abs(-q-r)+Math.abs(r) == boardSize))
		)
			return true;
		else return false;

	}




	public int getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public ArrayList<GameRules> getRules() {
		return rules;
	}
	
	public void setRules(ArrayList<GameRules> rules) {
		this.rules = rules;
	}

	public boolean isARule(GameRules rule) 
	{
		
		if(rules.contains(rule))
			return true;
		else return false;

		
	}
	
	
	public int getAmountOfStartingTiles() {
		return amountOfStartingTiles;
	}

	public void setAmountOfStartingTiles(int amountOfStartingTiles) {
		this.amountOfStartingTiles = amountOfStartingTiles;
	}

	public int getBombRadius() {
		return bombRadius;
	}

	public void setBombRadius(int bombRadius) {
		this.bombRadius = bombRadius;
	}

	public int getDefaultAmountOfHoles() {
		return defaultAmountOfHoles;
	}

	public void setDefaultAmountOfHoles(int defaultAmountOfHoles) {
		this.defaultAmountOfHoles = defaultAmountOfHoles;
	}

	public int getDefaultAmountOfObstacles() {
		return defaultAmountOfObstacles;
	}

	public void setDefaultAmountOfObstacles(int defaultAmountOfObstacles) {
		this.defaultAmountOfObstacles = defaultAmountOfObstacles;
	}

	public boolean isRandomizeObstacles() {
		return randomizeObstacles;
	}

	public void setRandomizeObstacles(boolean randomizeObstacles) {
		this.randomizeObstacles = randomizeObstacles;
	}

	public boolean isRandomizeHoles() {
		return randomizeHoles;
	}

	public void setRandomizeHoles(boolean randomizeHoles) {
		this.randomizeHoles = randomizeHoles;
	}

	public boolean isEnableSpecialTile() {
		return enableSpecialTile;
	}

	public void setEnableSpecialTile(boolean enableSpecialTile) {
		this.enableSpecialTile = enableSpecialTile;
	}

	public Tile[][] getQRSboard() {
		return QRSboard;
	}

	

 

	
	
}
