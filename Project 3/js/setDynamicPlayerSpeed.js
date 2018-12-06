/*
 * This script speeds up or slows down the player objects
 * Pretty Cool right
 * 
 * */

function update() {
		var speed = 40// Change this to 10 to reduce the speed by half and 20 to double speed
		if(game_object.speed[0] > 0 )
			game_object.speed[0] = speed;
		if(game_object.speed[1] > 0 )
			game_object.speed[1] = speed;
}
