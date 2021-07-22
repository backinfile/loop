package com.backinfile.loop.core;

import java.text.MessageFormat;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class MMap<T> {
	private int width;
	private int height;
	private Object[][] map;

	public MMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.map = new Object[width][height];
	}

	public void set(int x, int y, T value) {
		checkSize(x, y);
		map[x][y] = value;
	}

	public void set(Pos pos, T value) {
		set(pos.x, pos.y, value);
	}

	@SuppressWarnings("unchecked")
	public T get(int x, int y) {
		checkSize(x, y);
		return (T) map[x][y];
	}

	public T get(Pos pos) {
		return get(pos.x, pos.y);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isFit(int x, int y) {
		if (0 <= x && x < width && 0 <= y && y < height) {
			return true;
		}
		return false;
	}

	public boolean isFit(Pos pos) {
		return isFit(pos.x, pos.y);
	}

	private void checkSize(int x, int y) {
		if (!isFit(x, y)) {
			String msg = MessageFormat.format("超出MMap长度了 width={0},height={1},x={2},y={3}", width, height, x, y);
			throw new SysException(msg);
		}
	}

	@SuppressWarnings("unchecked")
	public void forEach(BiConsumer<Pos, T> func) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				func.accept(new Pos(i, j), (T) map[i][j]);
			}
		}
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("\n");
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (map[i][j] != null) {
					sj.add("(" + i + "," + j + "," + map[i][j].toString() + ")");
				}
			}
		}
		return sj.toString();
	}
}
