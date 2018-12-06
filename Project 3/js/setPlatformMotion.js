/*
 * This script adds movement to the game object
 * 
 * */

function update(iter,noOfPlatforms) {
	// Indexes start from 1 for this function
	// If there are 5 platforms, 
	// The below implementation makes platform 1 move horizontal and 3 vertical
	platforms_to_move_horizontal =  [1]
	platforms_to_move_vertical =  [3]
	
	if(platforms_to_move_vertical.indexOf(iter) != -1)
		game_object.setMotion(0,1);
	
	if(platforms_to_move_horizontal.indexOf(iter) != -1)
		game_object.setMotion(1,0);
	
}
