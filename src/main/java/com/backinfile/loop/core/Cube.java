package com.backinfile.loop.core;

public class Cube extends Movable {
	public CubeType type;

	public static enum CubeType {
		Empty, Wall, Rock, Human, Trans;
	}

	public Cube(CubeType type) {
		this.type = type;
	}

	public void resetPosition() {
		this.pos.set(this.oriPos);
	}

	public void setPosition(Pos pos) {
		this.pos.x = pos.x;
		this.pos.y = pos.y;
	}

	public void setPosition(int x, int y) {
		this.pos.x = x;
		this.pos.y = y;
	}

	public void setOriPosition(int x, int y) {
		this.oriPos.x = x;
		this.oriPos.y = y;
	}

	@Override
	public String toString() {
		return type.name();
	}

}
