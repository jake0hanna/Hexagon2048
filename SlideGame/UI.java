package SlideGame;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

public class UI implements ActionListener, MouseListener
{
	JFrame coreFrame;
	Container mainMenuContainer, gameContainer, boardContainer;
	JButton startButton, quitButton, eastButton, westButton, northwestButton, northeastButton, southwestButton, southeastButton;
	BufferedImage banner, hexagon;
	ImageIcon eastArrow, westArrow, northwestArrow, northeastArrow, southwestArrow, southeastArrow;
	JLabel renderedBanner, renderedHexagon, renderedArrow, boardSizeLabel;
	JFormattedTextField holesAmount, obstaclesAmount, specialEffectsFrequency;
	Checkbox obstacles, holes, specialEffects, activateEffectsIfDeleted;
	ButtonGroup gamemodeButtons;
	JRadioButton ripoffMode, classicMode, lockoutMode, speelflakeMode;
	ArrayList<GameRules> rules;
	Dictionary obstAmounts;

	int boardSize, //includes center so, 1 less than actual board size
					   //values 2 - 6 only
			boardCenterOffset = (-1*boardSize),
			hexRenderSize = 75, 
			gamePieceHexSize = hexRenderSize/10 *9,
			gamePieceOffset = hexRenderSize/12,
			perHexOffset = (int) (hexRenderSize-7),
			perHexHorizontalOffset = (int) (hexRenderSize/10),
			screenDimensionsOffset = 450, 			
			screenCenterOffset = 37*(7-boardSize), //472 is the center (size 7)
			horizontalOffset = 200,
			rowOffset = hexRenderSize/2,
			heightOffset = (hexRenderSize/4)*3,
			startingRowOffset = hexRenderSize-10;


	
	
	public final static int screenSize = 1050;

	UI()
	{
		rules = new ArrayList<GameRules>();
		

		
		try 
		{
			banner = ImageIO.read(getClass().getResource("/Assets/slidegame.png"));
			hexagon = ImageIO.read(getClass().getResource("/Assets/hexagonTEST.png"));
			renderedBanner = new JLabel(new ImageIcon(banner));
			renderedHexagon = new JLabel(new ImageIcon(hexagon));

			eastArrow = new ImageIcon("Assets/arrowRIGHT.jpg");
			westArrow = new ImageIcon("Assets/arrowLEFT.jpg");
			northwestArrow = new ImageIcon("Assets/arrowNW.jpg");
			northeastArrow = new ImageIcon("Assets/arrowNE.jpg");
			southwestArrow = new ImageIcon("Assets/arrowSW.jpg");
			southeastArrow = new ImageIcon("Assets/arrowSE.jpg");

			eastButton = new JButton(eastArrow);
			westButton = new JButton(westArrow);
			northwestButton = new JButton(northwestArrow);
			northeastButton = new JButton(northeastArrow);
			southwestButton = new JButton(southwestArrow);
			southeastButton = new JButton(southeastArrow);



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			

		coreFrame = new JFrame();
		coreFrame.setSize(screenSize, screenSize);
		coreFrame.setResizable(false);
		coreFrame.setBackground(Color.CYAN);
		coreFrame.setTitle("Slide Game");
		coreFrame.setVisible(true);
	
	
		//Main Menu

		mainMenuContainer = new Container();
		mainMenuContainer.setSize(screenSize, screenSize);

		
		
		startButton = new JButton();
		startButton.setBounds(450, 800, screenSize/5, screenSize/10);
		startButton.setText("Start");
		startButton.addActionListener(this);
		mainMenuContainer.add(startButton);

		
		gamemodeButtons = new ButtonGroup();

		ripoffMode = new JRadioButton("Classic Mode");
		ripoffMode.setActionCommand("Classic");
		ripoffMode.setBounds(450, 600, screenSize/3, screenSize/20);
		ripoffMode.addActionListener(this);
		gamemodeButtons.add(ripoffMode);
		gamemodeButtons.setSelected(ripoffMode.getModel(), true);
		mainMenuContainer.add(ripoffMode);

		classicMode = new JRadioButton("Slide Game");
		classicMode.setActionCommand("Slide Game");
		classicMode.setBounds(450, 650, screenSize/3, screenSize/20);
		classicMode.addActionListener(this);
		gamemodeButtons.add(classicMode);
		mainMenuContainer.add(classicMode);

	

		

		
		renderedBanner.setBounds(0, 0, screenSize, screenSize/3);
		mainMenuContainer.add(renderedBanner);
		
		coreFrame.add(mainMenuContainer);


		//Game 

		gameContainer = new Container();
		gameContainer.setSize(screenSize, screenSize);
		gameContainer.setBackground(Color.CYAN);
		gameContainer.setVisible(false);

		refreshGameContainer();

		coreFrame.add(gameContainer);
		
		updateRules();
		coreFrame.setVisible(true);
		coreFrame.repaint();
	
	}

	public void refreshGameContainer()
	{
		gameContainer.removeAll();

		northeastButton.setBounds(900, 700, screenSize/10, screenSize/10);
		eastButton.setBounds(900, 800, screenSize/10, screenSize/10);
		southeastButton.setBounds(900, 900, screenSize/10, screenSize/10);

		northwestButton.setBounds(10, 700, screenSize/10, screenSize/10);
		westButton.setBounds(10, 800, screenSize/10, screenSize/10);		
		southwestButton.setBounds(10, 900, screenSize/10, screenSize/10);

		eastButton.setOpaque(false);
		eastButton.setContentAreaFilled(false);
		eastButton.setBorderPainted(false);
		eastButton.setFocusPainted(false);

		westButton.setOpaque(false);
		westButton.setContentAreaFilled(false);
		westButton.setBorderPainted(false);
		westButton.setFocusPainted(false);

		northwestButton.setOpaque(false);
		northwestButton.setContentAreaFilled(false);
		northwestButton.setBorderPainted(false);
		northwestButton.setFocusPainted(false);

		northeastButton.setOpaque(false);
		northeastButton.setContentAreaFilled(false);
		northeastButton.setBorderPainted(false);
		northeastButton.setFocusPainted(false);

		southwestButton.setOpaque(false);
		southwestButton.setContentAreaFilled(false);
		southwestButton.setBorderPainted(false);
		southwestButton.setFocusPainted(false);

		southeastButton.setOpaque(false);
		southeastButton.setContentAreaFilled(false);
		southeastButton.setBorderPainted(false);
		southeastButton.setFocusPainted(false);

		quitButton = new JButton("X");
		quitButton.setBounds(10, 10, screenSize/20, screenSize/20);
		

		gameContainer.add(quitButton);
		gameContainer.add(eastButton);
		gameContainer.add(westButton);
		gameContainer.add(northwestButton);
		gameContainer.add(northeastButton);
		gameContainer.add(southwestButton);
		gameContainer.add(southeastButton);
	}
	
	public void paintBoard(ArrayList<Pair> tiles) 
	{	
		refreshGameContainer();
		boardCenterOffset = (-1*boardSize);
		screenCenterOffset = 37*(7-boardSize);
		int currentRowPosition = 0;
		int rowResetPosition = 0;
		
		boolean rowReset = false;

			// Background of board
			for (int r = -boardSize ; r < boardSize +1; r++) 
			{
				
				int r1 = Math.max(-boardSize, -r - boardSize);
				int r2 = Math.min(boardSize, -r + boardSize);
				for (int q = r1; q <= r2; q++) 
				{
					int s = -q - r;
					
		/*0,-4,4 first coord renders at 450,150 should be much further left (-x)
		// the issue currently is I dont account for the row being 1 longer

		// need to add logic to account for the desired interlocking
		// and logic for each row being 1 longer or shorter, (rowReset + currentRowPosition)

		// each row should be 3/4th of a hexagon lower ===== easy universal offset where I add 1/4th
		// top half (so currentRowPosition > boardSize) is 1/2 left, while bottom half is 1/2 right 
		*/ 
					HexagonUI c = new HexagonUI(hexRenderSize, TileType.BackgroundTile);
					
					
					int x =
					// actual screen 0,0
					((screenDimensionsOffset - horizontalOffset) 

					// current hex * offset (hexSize - 10%)|||| responsible for the sapce between each hex
					+ ((q * perHexOffset) 
					//      current hex * hexrender size/10 |||| adds a little extra horizontal space between each hex
							+q*perHexHorizontalOffset) 

					//responsible for moving the row over by 1/2 a hexagon
					+ (rowOffset*(currentRowPosition)));



					int y = screenDimensionsOffset + (int) (r * perHexOffset);

					if((!rowReset)){
						x -= perHexOffset;
					}

					if(currentRowPosition == 0)
					{
						x += startingRowOffset;
					}
		
					//w/O:  247 @ s1 AND 435 @ s6 
					x += screenCenterOffset -40;
					c.setBounds(x, y, hexRenderSize, hexRenderSize);
					c.setToolTipText(q + "," + r + "," + s + " --- " + x + "," + y);
					gameContainer.add(c);
		
				}

				currentRowPosition++;

				if(rowResetPosition < boardSize  && !rowReset)
				{
					rowResetPosition++;
					if(rowResetPosition < boardSize)
						rowReset = true;
				}
				else
					rowResetPosition--;
				

			}
		
			 currentRowPosition = 0;
			 rowResetPosition = 0;
			
			 rowReset = false;
			
			 // Tiles on the board
			if (tiles != null) 
				for (Pair tilePair : tiles) 
				{
					Tile tile = tilePair.getTile();
					Point coord = tilePair.getCoord();
					int q = (int)coord.getX();
					int r = (int)coord.getY();


					if(isValid(q,r))
					{

						HexagonUI tileHexagon;
			
						if(tile.getType() == TileType.PointTile)
						{
							tileHexagon = new HexagonUI(gamePieceHexSize, tile.getType(), tile.getScoreValue());
						}
						else
						{
							tileHexagon = new HexagonUI(gamePieceHexSize, tile.getType());
						}
			
						if(r == 0)
						{
							currentRowPosition = boardSize;
						}
						else if(r < 0)
						{
							currentRowPosition = boardSize - Math.abs(r);
						}
						else if(r > 0)
						{
							currentRowPosition = boardSize + Math.abs(r);
						}


						int x = ((screenDimensionsOffset- horizontalOffset) + ((q * perHexOffset) +perHexHorizontalOffset*q) + (rowOffset*(currentRowPosition)));
						int y = screenDimensionsOffset + (int) (r * perHexOffset);

						if(currentRowPosition == 0)
						{
							x += startingRowOffset - perHexOffset;
						}
						

						x += screenCenterOffset -40;

						tileHexagon.setBounds(x + gamePieceOffset, y + gamePieceOffset, hexRenderSize, hexRenderSize);
						tileHexagon.setToolTipText(q + "," + r + " --- " + x + "," + y);
						gameContainer.add(tileHexagon);
						gameContainer.setComponentZOrder(gameContainer.getComponent(gameContainer.getComponentCount() - 1), 0);
		
			
					

					currentRowPosition++;

					if(rowResetPosition < boardSize  && !rowReset)
					{
						rowResetPosition++;
						if(rowResetPosition < boardSize)
							rowReset = true;
					}
					else
						rowResetPosition--;
						
						
					}
					else 
					{
					}
				}
		
			coreFrame.repaint();
		}
	

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		updateRules();
		if(e.getSource() == startButton)
		{
			//paint default board
			mainMenuContainer.setVisible(false);
			gameContainer.setVisible(true);
			coreFrame.repaint();
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) // <---- if game is active, check location
	{
		e.getX(); 
		e.getY();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void setMouseListener(MouseListener listener) {
		coreFrame.addMouseListener(listener);
	}	

	public boolean isValid(int q, int r)
	{
		if(
		(q >= -boardSize && q <= boardSize && r >= -boardSize && r <= boardSize && -q-r >= -boardSize && -q-r <= boardSize) 
		&& 
		
		(q+r+(-q-r) == 0)	
		  )return true;
			
		else return false;
	}
	public boolean isValid(Point qrs)
	{
		return isValid((int)qrs.getX(),(int) qrs.getY());
	}


	/*public void testCase(int caseno)
	{

		switch(caseno)
		{
		case 1:
			//send start
			
			//test case
			ArrayList<Pair> p = new ArrayList<Pair>();
			p.add(new Pair(new Tile(EffectType.Bomb), new ThreePointCoordinate(0,-4,4)));
			p.add(new Pair(new Tile(TileType.BasicObstacle), new ThreePointCoordinate(1,-4,3)));
			p.add(new Pair(new Tile(TileType.PointTile), new ThreePointCoordinate(2,-4,2)));
			p.add(new Pair(new Tile(2), new ThreePointCoordinate(3,-4,1)));
			p.add(new Pair(new Tile(EffectType.Bomb), new ThreePointCoordinate(0,0,0)));			
			
			p.add(new Pair(new Tile(4), new ThreePointCoordinate(1,0,-1)));
			p.add(new Pair(new Tile(8), new ThreePointCoordinate(2,0,-2)));
			p.add(new Pair(new Tile(16), new ThreePointCoordinate(3,0,-3)));
			p.add(new Pair(new Tile(32), new ThreePointCoordinate(4,0,-4)));

			p.add(new Pair(new Tile(64), new ThreePointCoordinate(0,4,-4)));
			p.add(new Pair(new Tile(128), new ThreePointCoordinate(1,3,-4)));
			p.add(new Pair(new Tile(256), new ThreePointCoordinate(2,2,-4)));
			p.add(new Pair(new Tile(512), new ThreePointCoordinate(3,1,-4)));

			p.add(new Pair(new Tile(1024), new ThreePointCoordinate(0,6,-4)));
			p.add(new Pair(new Tile(2048), new ThreePointCoordinate(1,5,-4)));
			p.add(new Pair(new Tile(4096), new ThreePointCoordinate(2,4,-4)));
			p.add(new Pair(new Tile(8192), new ThreePointCoordinate(3,3,-4)));
			p.add(new Pair(new Tile(16384), new ThreePointCoordinate(4,2,-4)));
			p.add(new Pair(new Tile(32768), new ThreePointCoordinate(5,1,-4)));
			paintBoard(p);

			break;


		}


	}
*/


	public void updateRules()
	{

		switch(gamemodeButtons.getSelection().getActionCommand())
		{
			case "Slide Game":
				this.boardSize = RuleSet.ClassicSlideGame.getBoardSize();
				this.setRules(RuleSet.ClassicSlideGame.getRules());
				
				break;
			case "Lockout":
				this.boardSize = RuleSet.Lockout.getBoardSize();
				this.setRules(RuleSet.Lockout.getRules());
				break;
			case "Classic":
				this.boardSize = RuleSet.TwentyFortyEight.getBoardSize();
				this.setRules(RuleSet.TwentyFortyEight.getRules());
				break;
			case "Speelflake":
				this.boardSize = RuleSet.Speelflake.getBoardSize();
				this.setRules(RuleSet.Speelflake.getRules());
				break;

		}


	}

	public void goToMainMenu() 
	{

	     gameContainer.setVisible(false);
	     mainMenuContainer.setVisible(true);

	}



	public JFrame getCoreFrame() {
		return coreFrame;
	}




	public void setCoreFrame(JFrame coreFrame) {
		this.coreFrame = coreFrame;
	}




	public Container getMainMenuContainer() {
		return mainMenuContainer;
	}




	public void setMainMenuContainer(Container mainMenuContainer) {
		this.mainMenuContainer = mainMenuContainer;
	}




	public Container getGameContainer() {
		return gameContainer;
	}




	public void setGameContainer(Container gameContainer) {
		this.gameContainer = gameContainer;
	}




	public Container getBoardContainer() {
		return boardContainer;
	}




	public void setBoardContainer(Container boardContainer) {
		this.boardContainer = boardContainer;
	}




	public JButton getStartButton() {
		return startButton;
	}




	public void setStartButton(JButton startButton) {
		this.startButton = startButton;
	}




	public JButton getQuitButton() {
		return quitButton;
	}




	public void setQuitButton(JButton quitButton) {
		this.quitButton = quitButton;
	}




	public JButton getEastButton() {
		return eastButton;
	}




	public void setEastButton(JButton eastButton) {
		this.eastButton = eastButton;
	}




	public JButton getWestButton() {
		return westButton;
	}




	public void setWestButton(JButton westButton) {
		this.westButton = westButton;
	}




	public JButton getNorthwestButton() {
		return northwestButton;
	}




	public void setNorthwestButton(JButton northwestButton) {
		this.northwestButton = northwestButton;
	}




	public JButton getNortheastButton() {
		return northeastButton;
	}




	public void setNortheastButton(JButton northeastButton) {
		this.northeastButton = northeastButton;
	}




	public JButton getSouthwestButton() {
		return southwestButton;
	}




	public void setSouthwestButton(JButton southwestButton) {
		this.southwestButton = southwestButton;
	}




	public JButton getSoutheastButton() {
		return southeastButton;
	}




	public void setSoutheastButton(JButton southeastButton) {
		this.southeastButton = southeastButton;
	}




	public BufferedImage getBanner() {
		return banner;
	}




	public void setBanner(BufferedImage banner) {
		this.banner = banner;
	}




	public BufferedImage getHexagon() {
		return hexagon;
	}




	public void setHexagon(BufferedImage hexagon) {
		this.hexagon = hexagon;
	}




	public ImageIcon getEastArrow() {
		return eastArrow;
	}




	public void setEastArrow(ImageIcon eastArrow) {
		this.eastArrow = eastArrow;
	}




	public ImageIcon getWestArrow() {
		return westArrow;
	}




	public void setWestArrow(ImageIcon westArrow) {
		this.westArrow = westArrow;
	}




	public ImageIcon getNorthwestArrow() {
		return northwestArrow;
	}




	public void setNorthwestArrow(ImageIcon northwestArrow) {
		this.northwestArrow = northwestArrow;
	}




	public ImageIcon getNortheastArrow() {
		return northeastArrow;
	}




	public void setNortheastArrow(ImageIcon northeastArrow) {
		this.northeastArrow = northeastArrow;
	}




	public ImageIcon getSouthwestArrow() {
		return southwestArrow;
	}




	public void setSouthwestArrow(ImageIcon southwestArrow) {
		this.southwestArrow = southwestArrow;
	}




	public ImageIcon getSoutheastArrow() {
		return southeastArrow;
	}




	public void setSoutheastArrow(ImageIcon southeastArrow) {
		this.southeastArrow = southeastArrow;
	}




	public JLabel getRenderedBanner() {
		return renderedBanner;
	}




	public void setRenderedBanner(JLabel renderedBanner) {
		this.renderedBanner = renderedBanner;
	}




	public JLabel getRenderedHexagon() {
		return renderedHexagon;
	}




	public void setRenderedHexagon(JLabel renderedHexagon) {
		this.renderedHexagon = renderedHexagon;
	}




	public JLabel getRenderedArrow() {
		return renderedArrow;
	}




	public void setRenderedArrow(JLabel renderedArrow) {
		this.renderedArrow = renderedArrow;
	}




	public JLabel getBoardSizeLabel() {
		return boardSizeLabel;
	}




	public void setBoardSizeLabel(JLabel boardSizeLabel) {
		this.boardSizeLabel = boardSizeLabel;
	}








	public JFormattedTextField getHolesAmount() {
		return holesAmount;
	}




	public void setHolesAmount(JFormattedTextField holesAmount) {
		this.holesAmount = holesAmount;
	}




	public JFormattedTextField getObstaclesAmount() {
		return obstaclesAmount;
	}




	public void setObstaclesAmount(JFormattedTextField obstaclesAmount) {
		this.obstaclesAmount = obstaclesAmount;
	}




	public JFormattedTextField getSpecialEffectsFrequency() {
		return specialEffectsFrequency;
	}




	public void setSpecialEffectsFrequency(JFormattedTextField specialEffectsFrequency) {
		this.specialEffectsFrequency = specialEffectsFrequency;
	}




	public Checkbox getObstacles() {
		return obstacles;
	}




	public void setObstacles(Checkbox obstacles) {
		this.obstacles = obstacles;
	}




	public Checkbox getHoles() {
		return holes;
	}




	public void setHoles(Checkbox holes) {
		this.holes = holes;
	}




	public Checkbox getSpecialEffects() {
		return specialEffects;
	}




	public void setSpecialEffects(Checkbox specialEffects) {
		this.specialEffects = specialEffects;
	}




	public Checkbox getActivateEffectsIfDeleted() {
		return activateEffectsIfDeleted;
	}




	public void setActivateEffectsIfDeleted(Checkbox activateEffectsIfDeleted) {
		this.activateEffectsIfDeleted = activateEffectsIfDeleted;
	}




	




	public int getBoardSize() {
		return boardSize;
	}




	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}




	public int getHexRenderSize() {
		return hexRenderSize;
	}




	public void setHexRenderSize(int hexRenderSize) {
		this.hexRenderSize = hexRenderSize;
	}




	public int getGamePieceHexSize() {
		return gamePieceHexSize;
	}




	public void setGamePieceHexSize(int gamePieceHexSize) {
		this.gamePieceHexSize = gamePieceHexSize;
	}




	public int getGamePieceOffset() {
		return gamePieceOffset;
	}




	public void setGamePieceOffset(int gamePieceOffset) {
		this.gamePieceOffset = gamePieceOffset;
	}




	public int getPerHexOffset() {
		return perHexOffset;
	}




	public void setPerHexOffset(int perHexOffset) {
		this.perHexOffset = perHexOffset;
	}




	public int getPerHexHorizontalOffset() {
		return perHexHorizontalOffset;
	}




	public void setPerHexHorizontalOffset(int perHexHorizontalOffset) {
		this.perHexHorizontalOffset = perHexHorizontalOffset;
	}




	public int getScreenDimensionsOffset() {
		return screenDimensionsOffset;
	}




	public void setScreenDimensionsOffset(int screenDimensionsOffset) {
		this.screenDimensionsOffset = screenDimensionsOffset;
	}




	public int getScreenCenterOffset() {
		return screenCenterOffset;
	}




	public void setScreenCenterOffset(int screenCenterOffset) {
		this.screenCenterOffset = screenCenterOffset;
	}




	public int getHorizontalOffset() {
		return horizontalOffset;
	}




	public void setHorizontalOffset(int horizontalOffset) {
		this.horizontalOffset = horizontalOffset;
	}




	public int getBoardCenterOffset() {
		return boardCenterOffset;
	}




	public void setBoardCenterOffset(int boardCenterOffset) {
		this.boardCenterOffset = boardCenterOffset;
	}




	public int getRowOffset() {
		return rowOffset;
	}




	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}




	public int getHeightOffset() {
		return heightOffset;
	}




	public void setHeightOffset(int heightOffset) {
		this.heightOffset = heightOffset;
	}




	public int getStartingRowOffset() {
		return startingRowOffset;
	}




	public void setStartingRowOffset(int startingRowOffset) {
		this.startingRowOffset = startingRowOffset;
	}




	public static int getScreensize() {
		return screenSize;
	}




	public ArrayList<GameRules> getRules() {
		return rules;
	}




	public void setRules(ArrayList<GameRules> rules) {
		this.rules = rules;
	}

	public void endGame() {
	}

	








	
}


