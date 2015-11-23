package geon.game.topview.system;

import geon.game.topview.Resources;
import geon.game.topview.components.HealthComponent;
import geon.game.topview.components.PlayerComponent;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * <pre>
 * geon.game.topview.system
 * 	|_UISystem
 * 
 * 개요 : 
 * 작성일 : Nov 15, 2015
 * </pre>
 *
 * @author	박건
 * @version	1.0
 */
public class UISystem extends EntitySystem{

	private Stage stage;
	private Image bossBar, playerBar;
	private Label timeLabel1, timeLabel2;
	private float playTime = 0;
	private final int BAR_WIDTH = 300;
	
	@Override
	public void addedToEngine (Engine engine) {

		ScreenViewport viewport = new ScreenViewport();
		stage = new Stage(viewport, Resources.batch);
		
		Table table = new Table();
		stage.addActor(table);
		
		table.setFillParent(true);
		//table.debug();
		table.pad(10);
		
		table.add(new Label("장문광", Resources.skin)).top().padRight(10);
		
		Stack playerBarStack = new Stack();
		playerBarStack.add(new Image(Resources.healthBar));
		playerBar = new Image(Resources.healthBarFill);
		playerBarStack.add(playerBar);
		table.add(playerBarStack).width(BAR_WIDTH).left();

		Table timeTable = new Table();
		table.add(timeTable).expandX();
		timeLabel1 = new Label("00", Resources.skin);
		timeLabel2 = new Label("00", Resources.skin);
		timeTable.add(timeLabel1).uniformX().padRight(10).right();
		timeTable.add(new Label(":", Resources.skin)).width(2);
		timeTable.add(timeLabel2).uniformX().padLeft(10).left();

		Stack bossBarStack = new Stack();
		bossBarStack.add(new Image(Resources.healthBar));
		bossBar = new Image(Resources.healthBarFill);
		bossBarStack.add(bossBar);
		table.add(bossBarStack).width(BAR_WIDTH).right();
		table.add(new Label("박준래", Resources.skin)).top().padLeft(10);
		
		table.row();
		table.add().expandY();
		
		table.pack();
		
		Table cross = new Table();
		stage.addActor(cross);
		
		cross.setFillParent(true);
		cross.add(new Image(Resources.cross)).expand();
		
		cross.pack();
	}

	@Override
	public void update (float deltaTime) {
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		HealthComponent player = getEngine()
			.getEntitiesFor(Family.all(PlayerComponent.class, HealthComponent.class).get())
			.get(0).getComponent(HealthComponent.class);
		
		playerBar.setWidth((float)player.health / player.maxHealth * BAR_WIDTH);
		
		playTime += deltaTime;
		if(playTime > 60) timeLabel1.setText(String.format("%d : %02.0f", (int)playTime / 60, playTime % 60));
		else timeLabel1.setText(String.format("%02.0f",playTime));
		timeLabel2.setText(String.format("%02d", (int)(playTime * 100) % 100));
		
		stage.act(deltaTime);
		stage.draw();
	}

}
