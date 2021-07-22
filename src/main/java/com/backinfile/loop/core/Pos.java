package com.backinfile.loop.core;

import java.util.Objects;

public class Pos {
	public int x;
	public int y;

	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof Pos) {
			Pos obj = (Pos) other;
			return this.x == obj.x && this.y == obj.y;
		}
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
