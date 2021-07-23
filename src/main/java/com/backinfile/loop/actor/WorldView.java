package com.backinfile.loop.actor;

import java.util.ArrayList;
import java.util.List;

import com.backinfile.loop.Settings;
import com.backinfile.loop.core.Cube;
import com.backinfile.loop.core.Cube.CubeType;
import com.backinfile.loop.core.GameManager;
import com.backinfile.loop.core.Pos;
import com.backinfile.loop.core.WorldData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldView extends Group {

	private TextureRegion lastFrame = null;
	public List<CubeView> cubeViews = new ArrayList<>();
	public boolean requreFlushCamera = true;

	public WorldView() {
		GameManager.instance.worldView = this;

		WorldData data = GameManager.instance.getWorldData();
		for (int i = 0; i < data.baseWidth; i++) {
			for (int j = 0; j < data.baseHeight; j++) {
				Cube cube = data.actualMap.get(i, j);
				if (cube != null) {
					CubeView cubeView = new CubeView(cube);
					cubeViews.add(cubeView);
					addActor(cubeView);
				}
			}
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);

//		// 调整视口位置
//		if (requreFlushCamera) {
//			Pos worldSize = GameManager.instance.getWorldSize();
//			Viewport viewport = getStage().getViewport();
//			viewport.setWorldSize(worldSize.x, worldSize.y);
//			viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
//		}
	}

	public TextureRegion getScreen() {
		Viewport viewport = getStage().getViewport();
		int offsetWidth = viewport.getLeftGutterWidth();
		int offsetHeight = viewport.getBottomGutterHeight();
		TextureRegion frameBufferTexture = ScreenUtils.getFrameBufferTexture(offsetWidth, offsetHeight,
				(Settings.SCREEN_WIDTH - offsetWidth * 2), (Settings.SCREEN_HEIGHT - offsetHeight * 2));
		return frameBufferTexture;
	}
//
//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
//
//		if (lastFrame != null) {
//			lastFrame.getTexture().dispose();
//		}
//		lastFrame = getScreen();
//	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		preRender();
		super.draw(batch, parentAlpha);
		afterRender(batch);
	}

	private float m_fboScaler = 1.5f;
	private boolean m_fboEnabled = true;
	private FrameBuffer m_fbo = null;
	private TextureRegion m_fboRegion = null;
	private TextureRegion gameScenceRegion = null;

	public void preRender() {
		Pos worldSize = GameManager.instance.getWorldSize();
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		if (m_fboEnabled) {
			if (m_fbo == null) {

				m_fbo = new FrameBuffer(Format.RGB565, (int) (width * m_fboScaler), (int) (height * m_fboScaler),
						false);
				m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
				gameScenceRegion = new TextureRegion(m_fboRegion, 0, 0, (int) (worldSize.x * m_fboScaler),
						(int) (worldSize.y * m_fboScaler));
				m_fboRegion.flip(false, true);
				gameScenceRegion.flip(false, true);
			}
			m_fbo.begin();
			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
	}

	public void afterRender(Batch batch) {
		if (m_fbo != null) {
			m_fbo.end();

			batch.draw(m_fboRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			// 绘制分形
			Pos worldSize = GameManager.instance.getWorldSize();
			for (CubeView cubeView : cubeViews) {
				if (cubeView.cube.type == CubeType.Trans) {
					batch.draw(gameScenceRegion, cubeView.getX(), cubeView.getY(), cubeView.getWidth(),
							cubeView.getHeight());
				}
			}
		}
	}

}
