
package geon.game.topview;

import geon.game.topview.screen.GameplayScreen;
import geon.game.topview.screen.StoryScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameMain extends Game {
	
	private Screen storyScreen;
	private Screen gameplayScreen;


	@Override
	public void create () {
		Resources.load();
		
		storyScreen = new StoryScreen(this);
		gameplayScreen = new GameplayScreen(this);
		
		setScreen(storyScreen);
		
	}
	
	public void switchToGamePlay() {
		setScreen(gameplayScreen);
	}

}
