package com.backinfile.loop.core;

public class Cube {
	public CubeType type;
	public Pos pos = new Pos(0, 0);

	public static enum CubeType {
		Empty, Wall, Rock, Human, Trans;
	}

	public Cube(CubeType type) {
		this.type = type;
	}

	public void setPosition(int x, int y) {
		this.pos.x = x;
		this.pos.y = y;
	}

	@Override
	public String toString() {
		return type.name();
	}

}
