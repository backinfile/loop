package com.backinfile.loop.actor;

import com.backinfile.loop.Res;
import com.backinfile.loop.core.Cube;
import com.backinfile.loop.core.Cube.CubeType;
import com.backinfile.loop.core.GameManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CubeView extends Group {
	private Cube cube;
	private Image mainImage;
	private Image border = new Image(Res.CUBE_BORDER_BLACK);

	public CubeView(Cube cube) {
		this.cube = cube;
		switch (cube.type) {
		case Empty:
			break;
		case Human:
			mainImage = new Image(Res.CUBE_HUMAN);
			addActor(mainImage);
			addActor(border);
			break;
		case Rock:
			mainImage = new Image(Res.CUBE_ROCK);
			addActor(mainImage);
			addActor(border);
			break;
		case Trans:
			break;
		case Wall:
			mainImage = new Image(Res.CUBE_WALL);
			addActor(mainImage);
			addActor(border);
			break;
		default:
			break;
		}

	}

	public void setBorder(boolean white) {
		if (white) {
			border.setDrawable(new TextureRegionDrawable(Res.CUBE_BORDER_WHITE));
		} else {
			border.setDrawable(new TextureRegionDrawable(Res.CUBE_BORDER_BLACK));
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setX(cube.pos.x * Res.CUBE_SIZE);
		setY(cube.pos.y * Res.CUBE_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (cube.type != CubeType.Trans) {
			return;
		}
		TextureRegion screen = GameManager.instance.worldView.getLastFrame();
		if (screen != null) {
			batch.draw(screen, getX(), getY(), Res.CUBE_HUMAN.getRegionWidth(), Res.CUBE_HUMAN.getRegionHeight());
		}
	}

}
