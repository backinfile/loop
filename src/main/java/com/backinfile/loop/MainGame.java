package com.backinfile.loop;

import com.backinfile.loop.actor.WorldView;
import com.backinfile.loop.core.GameManager;
import com.backinfile.loop.core.LoopInputProcessor;
import com.backinfile.loop.core.WorldData;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

/**
 * 游戏主程序的入口类, 实现 ApplicationListener 接口
 */
public class MainGame extends Game {
	private Stage stage;
	private OrthographicCamera camera;

	@Override
	public void create() {

		// 初始化资源，数据
		Res.init();
		GameManager.instance.init();
//		Gdx.input.setCursorImage(Res.Cursor, 0, 0);

		// 初始化场景
		camera = new OrthographicCamera();
		stage = new Stage(new FitViewport(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT, camera));
		stage.addActor(new WorldView());
		Gdx.input.setInputProcessor(new LoopInputProcessor());

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height);
	}

}