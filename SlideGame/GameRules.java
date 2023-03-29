package SlideGame;

public enum GameRules 
{


	/* 		Instead of having a bunch of different rules, I think it would be better to have a few different rulesets
	 * 		
	 * 		2048 Mode: classic gamemode, no obstacles, no holes, no special effects on a very small board
	 * 
	 * 		Slide Game: Multiple sub game modes with different rulesets
	 * 					-Classic: Obstacles, special effects, 
	 * 					-Obstacle Chaos: different types that spawn and must be destroyed before you get locked out
	 *
	 * 
	 */

	EdgeOfBoardDelete, 
	EdgeOfBoardCombine,
	EdgeOfBoardWrapAround,


	/*
	 * Obstacles could be generated in static set patterns, randomly, or continously and destroyed by bombs
	 * 
	 * 
	 * 
	 */


	Obstacles,
	ObstaclesWalls,
	RandomObstacles,
	ProgressiveObstacles,
	DestructableObstacles,
	ShuffleObstacles,

	/*
	 * Holes could be generated similar to obstacles, but with a slightly different effect
	 * 
	 * 
	 */

	Holes,
	ConditionalHoles,
	SnowflakeHoles,
	RandomHoles,
	ProgressiveHoles,

	/*
	 *Bombs:
	 * 
	 *Shuffler:
	 * 
	 *WildCard:	 
	*/

	SpecialEffects,
	ActivateEffectsWhenCombined,
	ActivateEffectsCombinedAgainst,
	ActivateEffectsNextToCombined,
	ActivateEffectOnDeletion;
	

}
