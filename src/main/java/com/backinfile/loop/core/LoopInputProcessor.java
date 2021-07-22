package com.backinfile.loop.core;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class LoopInputProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.UP:
		case Keys.W:
			GameManager.instance.moveHuman(0);
			break;
		case Keys.DOWN:
		case Keys.S:
			GameManager.instance.moveHuman(1);
			break;
		case Keys.LEFT:
		case Keys.A:
			GameManager.instance.moveHuman(2);
			break;
		case Keys.RIGHT:
		case Keys.D:
			GameManager.instance.moveHuman(3);
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
