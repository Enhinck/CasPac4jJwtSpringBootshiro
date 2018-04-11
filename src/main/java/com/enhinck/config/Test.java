package com.enhinck.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		List<Map<String, String>> listMaps = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			Map<String, String> map = new HashMap<>();
			map.put("no", i+"");
			map.put("name", "测试"+i);
			map.put("content", "内容"+i);
			listMaps.add(map);
		}
		System.out.println(listMaps);
	}
}
