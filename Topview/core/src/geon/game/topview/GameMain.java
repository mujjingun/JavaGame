
package geon.game.topview;

import geon.game.topview.screen.GameplayScreen;
import geon.game.topview.screen.StoryScreen;
import geon.game.topview.system.*;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

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
