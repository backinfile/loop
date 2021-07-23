package com.backinfile.loop.support;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static <T> ArrayList<T> subList(List<T> list, int fromIndex, int toIndex) {
		return new ArrayList<T>(list.subList(fromIndex, toIndex));
	}
}
