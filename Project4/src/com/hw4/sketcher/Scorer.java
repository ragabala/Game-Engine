package com.hw4.sketcher;

public class Scorer extends GameObject implements Renderable{

	private static final long serialVersionUID = 1L;
	int hits, score;
	public Scorer() {
		// TODO Auto-generated constructor stub
		hits = 0;
		score = 0;
		x_pos = 10;
		y_pos = 30;
	}
	
	public void updateScore(int hits, int score) {
		this.hits = hits;
		this.score = score;
	}
	
	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "SCORER~" + GAME_OBJECT_ID + "~" + hits + "~" + score ;

	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		hits = Integer.parseInt(vals[2]);
		score = Integer.parseInt(vals[3]);
	}
	

	@Override
	public void render() {
		// TODO Auto-generated method stub
		sketcher.textSize(32);
		sketcher.text("Score : "+score+" Hits : "+hits, 10, 30); 
	}

}
