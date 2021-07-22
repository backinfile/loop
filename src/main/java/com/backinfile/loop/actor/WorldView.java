package com.backinfile.loop.actor;

import com.backinfile.loop.Res;
import com.backinfile.loop.Settings;
import com.backinfile.loop.core.Cube;
import com.backinfile.loop.core.GameManager;
import com.backinfile.loop.core.WorldData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ScreenUtils;

public class WorldView extends Group {

	private TextureRegion lastFrame = null;

	public WorldView() {
		GameManager.instance.worldView = this;

		WorldData data = GameManager.instance.getWorldData();
		for (int i = 0; i < data.baseWidth; i++) {
			for (int j = 0; j < data.baseHeight; j++) {
				Cube cube = data.actualMap.get(i, j);
				if (cube != null) {
					CubeView cubeView = new CubeView(cube);
					addActor(cubeView);
				}
			}
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public TextureRegion getLastFrame() {
		return lastFrame;
	}

	public TextureRegion getScreen() {
		WorldData worldData = GameManager.instance.worldData;
		int width = worldData.baseWidth * Res.CUBE_SIZE;
		int height = worldData.baseHeight * Res.CUBE_SIZE;
		int offsetWidth = (Settings.SCREEN_WIDTH - width) / 2;
		int offsetHeight = (Settings.SCREEN_HEIGHT - height) / 2;
		TextureRegion frameBufferTexture = ScreenUtils.getFrameBufferTexture(offsetWidth, offsetHeight, width, height);
		return frameBufferTexture;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (lastFrame != null) {
			lastFrame.getTexture().dispose();
		}
		lastFrame = getScreen();
	}
	/*
	 * private float m_fboScaler = 1.5f; private boolean m_fboEnabled = true;
	 * private FrameBuffer m_fbo = null; private TextureRegion m_fboRegion = null;
	 * 
	 * public void render(SpriteBatch spriteBatch) { int width =
	 * Gdx.graphics.getWidth(); int height = Gdx.graphics.getHeight();
	 * 
	 * if (m_fboEnabled) // enable or disable the supersampling { if (m_fbo == null)
	 * { // m_fboScaler increase or decrease the antialiasing quality
	 * 
	 * m_fbo = new FrameBuffer(Format.RGB565, (int) (width * m_fboScaler), (int)
	 * (height * m_fboScaler), false); m_fboRegion = new
	 * TextureRegion(m_fbo.getColorBufferTexture()); m_fboRegion.flip(false, true);
	 * }
	 * 
	 * m_fbo.begin(); }
	 * 
	 * // this is the main render function // my_render_impl();
	 * 
	 * if (m_fbo != null) { m_fbo.end();
	 * 
	 * spriteBatch.begin(); spriteBatch.draw(m_fboRegion, 0, 0, width, height);
	 * spriteBatch.end(); } }
	 */
}
