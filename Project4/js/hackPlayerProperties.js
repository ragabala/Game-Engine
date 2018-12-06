/*
 * This script speeds up or slows down the player objects
 * Pretty Cool right
 * 
 * */


function changeSpeed(game_object){
	var speed = 30// Change this to 10 to reduce the speed by half and 20 to double speed
	if(game_object.speed[0] > 0 )
		game_object.speed[0] = speed;
	if(game_object.speed[1] > 0 )
		game_object.speed[1] = speed;	
}


function changeLivesOfPlayer(game_object){
	// Changing the below property gives more lives to the player
	//print("Changing lives of player to 10")
	game_object.maxhits = 10;
}

function hackPlayerScore(game_object){
	game_object.score = 50;
}


/*The Update function does the following 
 * 
 *  The ChangeSpeed function can change the speed of the player.
 *   Set the speed values in the changespeed function.
 *   
 *  The changeLivesOfPlayer can give additional lifelines to the player during gameplay.
 *   
 *  The HackplayerScore function can add scores to the player 
 * 
 * 
 * */

function update() {
	changeSpeed(game_object);
	changeLivesOfPlayer(game_object);
	hackPlayerScore(game_object);
}



