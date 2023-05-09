package SlideGame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Controller implements ActionListener
{

	/*
	 * 	gui should pass game rules, start, stop, directional, and specific grid coordinate commands
	 * 
	 * 	game should pass list of Pairs, turnCount, current Score, 
	 * 
	 */
	
	private UI gui;
	private GameInstance game;
	
	Controller()
	{
		
		gui = new UI();
		gui.startButton.addActionListener(this);
		gui.eastButton.addActionListener(this);
		gui.westButton.addActionListener(this);
		gui.northwestButton.addActionListener(this);	
		gui.northeastButton.addActionListener(this);
		gui.southwestButton.addActionListener(this);
		gui.southeastButton.addActionListener(this);
		gui.quitButton.addActionListener(this);
		
		
		
	}




	public void startGame(int boardSize, ArrayList<GameRules> rules)
	{

		game = new GameInstance(boardSize, rules);

		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//getGui().testCase(1);
		getGui().paintBoard(game.refreshBoard());
		
		
	}

	public ArrayList<Pair> inputAction(Actions act)
	{
		return game.inputAction(act);

	}

	public ArrayList inputAction(Actions act, int x, int y)
	{
		if(!act.equals(Actions.TILEACTIVATE))
		{
			return null;

		}
		else
		{
			return game.inputAction(act, x, y);
		}

	}



	@Override
	public void actionPerformed(ActionEvent e) 
	{
	
		if(e.getSource().equals(getGui().getStartButton()))
		{
			startGame(getGui().getBoardSize(), getGui().getRules());

		}
		else 
		{

		ArrayList<Pair> temp;

		if(e.getSource().equals(getGui().getEastButton()))
			{
				temp = inputAction(Actions.EAST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}
			}
			else if(e.getSource().equals(getGui().getWestButton()))
			{
				temp = inputAction(Actions.WEST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}

			}
			else if(e.getSource().equals(getGui().getNorthwestButton()))
			{
				temp = inputAction(Actions.NORTHWEST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}
			}
			else if(e.getSource().equals(getGui().getNortheastButton()))
			{
				temp = inputAction(Actions.NORTHEAST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}
			}
			else if(e.getSource().equals(getGui().getSouthwestButton()))
			{
				temp = inputAction(Actions.SOUTHWEST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}
			}
			else if(e.getSource().equals(getGui().getSoutheastButton()))
			{
				temp = inputAction(Actions.SOUTHEAST);
				if(temp != null)
					getGui().paintBoard(temp);
				else
				{
					getGui().endGame();
				}
			}
			else if(e.getSource().equals(getGui().getQuitButton()))
			{
				game.endGame();
				getGui().goToMainMenu();

			}
		}

	}


	public UI getGui() {
		return gui;
	}

}