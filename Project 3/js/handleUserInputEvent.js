/*
 * This script sets how the user input has to be handled
 * Pretty Cool right
 * 
 * */

function update(x,y,player,event) {
	// TODO Auto-generated constructor stub
	event.x = x;
	event.y = y;
	event.pos_x = player.x_pos;
	event.pos_y = player.y_pos;
	event.player = player;

	/*
	 *  For experimentation of that the script changes the way input is handled
	 * Change the below line to
	 * player.setMovement(1, y); and press the left/right or space key to activate it
	 *  You can see the player keeps moving right
	 * player.setMovement(x, 1); and press the left/right or space key to activate it
	 *  You can see the player keeps on jumping
	 * player.setMovement(1, 1); and press the left/right or space key to activate it
	 *  You can see the player keeps on moving right whilejumping
	 * 
	 */
	
	player.setMovement(x, y);
}
