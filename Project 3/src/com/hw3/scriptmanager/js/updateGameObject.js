/*
 * This script adds movement to the game object
 * 
 * */

function update(iter,noOfPlatforms) {
	if(iter == 1)
	{
		game_object.setMotion(0,1);
	}
	else if (iter == noOfPlatforms / 2){
		game_object.setMotion(1,0);
	}
	print( 'Movement added to game object');
}
