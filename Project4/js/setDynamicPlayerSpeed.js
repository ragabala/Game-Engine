/*
 * This script speeds up or slows down the player objects
 * Pretty Cool right
 * 
 * */

function update(grid) {
		var speed = 1// Change this to 2 for 2x or 3 for 3x speed
		if(game_object.speed[0] > 0 )
			game_object.speed[0] = grid * speed;
		if(game_object.speed[1] > 0 )
			game_object.speed[1] = grid * speed;
}
