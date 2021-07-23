package com.backinfile.loop.core;

import java.util.ArrayList;
import java.util.List;

public class History {
	private static class Movement {
		public Cube cube;
		public Pos oriPos;
	}

	private List<Movement> movements = new ArrayList<>();

	private History() {
	}

	public static History getHistory(List<Cube> cubes) {
		History history = new History();
		for (Cube cube : cubes) {
			Movement movement = new Movement();
			movement.cube = cube;
			movement.oriPos = cube.pos.copy();
			history.movements.add(movement);
		}
		return history;
	}

	public void playback() {
		for (Movement movement : movements) {
			movement.cube.setPosition(movement.oriPos);
		}
	}
}
