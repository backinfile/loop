package com.backinfile.loop.actor;

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

import java.util.ArrayList;
import java.util.List;

public class WorldView extends Group {
    public List<CubeView> transViews = new ArrayList<>();

    public WorldView() {
        GameManager.instance.worldView = this;
    }

    public void init(WorldData data) {
        clearChildren();
        transViews.clear();
        clearFB();

        for (Cube cube : data.actualMap.getUnitList()) {
            CubeView cubeView = new CubeView(cube);
            if (cubeView.cube.type == CubeType.Trans) {
                transViews.add(cubeView);
            }
            addActor(cubeView);
        }
        for (Cube cube : data.floorMap.getUnitList()) {
            CubeView cubeView = new CubeView(cube);
            addActor(cubeView);
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public TextureRegion getScreen() {
        Viewport viewport = getStage().getViewport();
        int offsetWidth = viewport.getLeftGutterWidth();
        int offsetHeight = viewport.getBottomGutterHeight();
        return ScreenUtils.getFrameBufferTexture(offsetWidth, offsetHeight,
                (Settings.SCREEN_WIDTH - offsetWidth * 2), (Settings.SCREEN_HEIGHT - offsetHeight * 2));
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

    private static final float m_fboScaler = 1.5f;
    private static final boolean m_fboEnabled = true;
    private FrameBuffer m_fbo = null;
    private TextureRegion m_fboRegion = null;
    private TextureRegion gameSceneRegion = null;

    public void preRender() {
        Pos worldSize = GameManager.instance.getWorldSize();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (m_fboEnabled) {
            if (m_fbo == null) {

                m_fbo = new FrameBuffer(Format.RGB565, (int) (width * m_fboScaler), (int) (height * m_fboScaler),
                        false);
                m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
                gameSceneRegion = new TextureRegion(m_fboRegion, 0, 0, (int) (worldSize.x * m_fboScaler),
                        (int) (worldSize.y * m_fboScaler));
                m_fboRegion.flip(false, true);
                gameSceneRegion.flip(false, true);
            }
            m_fbo.begin();
            Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    public void clearFB() {
        if (m_fbo != null) {
            m_fbo.dispose();
            m_fbo = null;
        }
    }


    public void afterRender(Batch batch) {
        if (m_fbo != null) {
            m_fbo.end();

            Pos worldSize = GameManager.instance.getWorldSize();
            int offsetX = (Gdx.graphics.getWidth() - worldSize.x) / 2;
            int offsetY = (Gdx.graphics.getHeight() - worldSize.y) / 2;
            int shrink = 1;
            batch.draw(m_fboRegion, offsetX, offsetY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            // 绘制分形
            for (CubeView cubeView : transViews) {
                if (cubeView.cube.type == CubeType.Trans) {
                    batch.draw(gameSceneRegion, cubeView.getX() + offsetX + shrink, cubeView.getY() + offsetY + shrink,
                            cubeView.getWidth() - shrink * 2, cubeView.getHeight() - shrink * 2);
                }
            }
        }
    }

}
