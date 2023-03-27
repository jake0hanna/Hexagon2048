package SlideGame;

import java.util.ArrayList;

public enum RuleSet 
{

    TwentyFortyEight (new ArrayList<GameRules>(){{add(GameRules.EdgeOfBoardCombine);}},
                                                 2),

    ClassicSlideGame (new ArrayList<GameRules>(){{add(GameRules.EdgeOfBoardCombine);
                                                 add(GameRules.Obstacles); 
                                                 add(GameRules.SpecialEffects); 
                                                 add(GameRules.ObstaclesWalls);}}, 
                                                 4),
    
    Lockout (new ArrayList<GameRules>(){{add(GameRules.EdgeOfBoardCombine); 
                                        add(GameRules.Obstacles); 
                                        add(GameRules.SpecialEffects);
                                        add(GameRules.DestructableObstacles);
                                        add(GameRules.ShuffleObstacles);
                                        add(GameRules.ProgressiveObstacles);}},
                                        4),
    
    Speelflake (new ArrayList<GameRules>(){{add(GameRules.EdgeOfBoardDelete); 
                                            add(GameRules.Holes); 
                                            add(GameRules.ConditionalHoles); 
                                            add(GameRules.SpecialEffects);
                                            add(GameRules.ObstaclesWalls);}}, 
                                            6);

    //HexaGone (new ArrayList<GameRules>(){{add(GameRules.);}});

    private int boardSize;
    private ArrayList<GameRules> rules;



    RuleSet(ArrayList<GameRules> rules, int size)
    {
        this.boardSize = size;
        this.rules = rules;
    }

    public ArrayList<GameRules> getRules() {
        return rules;
    }

    public int getBoardSize() {
        return boardSize;
    }

}
