package geon.game.topview.screen;

import geon.game.topview.AnimatedActor;
import geon.game.topview.GameMain;
import geon.game.topview.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * <pre>
 * geon.game.topview
 * 	|_StoryScreen
 * 
 * 개요 : 
 * 작성일 : Nov 14, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class StoryScreen implements Screen {

	Stage stage;
	ScreenViewport viewport;
	Table table;
	GameMain game;
	
	public StoryScreen (final GameMain game) {
		viewport = new ScreenViewport();
		stage = new Stage(viewport, Resources.batch);
		this.game = game;
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		table.debug();
		stage.addActor(table);
		table.setFillParent(true);
		
		AnimatedActor titleImage=new AnimatedActor(Resources.title, 2f);
		table.add(titleImage).size(600, 300);
		titleImage.addAction(Actions.sequence(
			Actions.alpha(0),
			Actions.alpha(1, 1f)
		));
		
		table.pack();
		
		//loadingComplete();
	}
	
	public void loadingComplete(){
		Actor delayActor = new Actor();
		stage.addActor(delayActor);
		delayActor.addAction(Actions.sequence(Actions.delay(0.5f), new Action() {
			@Override
			public boolean act (float delta) {
				table.addAction(Actions.color(Color.WHITE, 0.5f));
				game.switchToGamePlay();
				return true;
			}
		}));
	}
	
	@Override
	public void show () {
		
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide () {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose () {
		// TODO Auto-generated method stub
		
	}

}
