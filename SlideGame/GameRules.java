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
	 * 					-Speelflake: Having more than 6 tiles of the same type 	
	 * 								 or of anytype in a row or column will cause a hole to spawn
	 * 
	 *
	 * 
	 * 		HexaGone: 
	 * 					Ideas: - pacman style game with changing obstacles, powerups, enemies, and wrap around
	 * 					       - 
	 * 		
	 * 
	 */
	
	/*
	 * Wrap around would create unique issues as the game ends when it cant spawn a tile,
	 * unless it was required to play with obstacles
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
	 * 
	 *BlackHole: maybe a tile that eats a certain amount of other tiles
	 *			 or eats endless tiles until it has none to eat at which point it dies
	 */

	SpecialEffects,
	ActivateEffectsWhenCombined,
	ActivateEffectsCombinedAgainst,
	ActivateEffectsNextToCombined,
	ActivateEffectOnDeletion;
	

}
