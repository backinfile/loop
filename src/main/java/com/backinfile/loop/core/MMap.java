package com.backinfile.loop.core;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class MMap<T extends Movable> {
	private int width;
	private int height;
	private List<T> valueList = new ArrayList<>();

	public MMap(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void add(T value) {
		checkSize(value.pos.x, value.pos.y);
		valueList.add(value);
	}

	public T get(int x, int y) {
		checkSize(x, y);
		for (T value : valueList) {
			if (value.pos.x == x && value.pos.y == y) {
				return value;
			}
		}
		return null;
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

	public void forEach(BiConsumer<Pos, T> func) {
		for (T value : valueList) {
			func.accept(value.pos, value);
		}
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("\n");
		forEach((pos, value) -> {
			sj.add("(" + pos.x + "," + pos.y + "," + value.toString() + ")");
		});
		return sj.toString();
	}
}
