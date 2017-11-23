package com.fid.util;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.zip.CRC32;

public class StringUtil {
	public static boolean isNullOrEmpty(String input) {
		return (input == null) || (input.isEmpty());
	}

	public static String toCamelCase(String input) {
		if (input.contains("_")) {
			input = input.replace("_", "-");
		}

		if (input.contains("-")) {
			String[] words = input.split("-");
			input = "";
			for (String word : words) {
				input = input + word.substring(0, 1).toUpperCase() + word.substring(1);
			}
		} else {
			input = input.substring(0, 1).toUpperCase() + input.substring(1);
		}
		return input;
	}

	public static String decodeURL(String value) {
		if (value != null) {
			try {
				value = URLDecoder.decode(value, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	public static String encodeURL(String value) {
		if (value != null) {
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	public static String trimPunctuation(String value) {
		if (value == null)
			return null;

		return value.replaceAll("\\pP", "").replaceAll("\\s*", "");
	}

	/**
	 * 判断是否为空白字符串 或为null
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		return value == null || value.trim().equals("");
	}

	/**
	 * crc32
	 * 
	 * @param bytes
	 * @return
	 */
	public static Long getCheckCode(byte[] bytes) {
		CRC32 crc32 = new CRC32();
		crc32.update(bytes);
		Long checkCode = Long.valueOf(crc32.getValue());
		return checkCode;
	}

	/**
	 * 过滤4个字节字符
	 * 
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

	// 向后推移，双个双引号遇见， 为分割条件 处理csv
	public static List<String> analysisCsv(String data) {
		List<String> resultList = new ArrayList<>();
		if (data == null)
			return resultList;
		if (data.equals(""))
			resultList.add("");
		String result = null;
		int signIndex = 0;
		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == '"') {
				signIndex++;
			}
			if (signIndex % 2 == 0) {
				if (i == data.length() - 1 || data.charAt(i + 1) == ',') {
					endIndex = i + 1;
					result = data.substring(beginIndex, endIndex);
					if (result.startsWith("\"")) {
						result = result.substring(1, result.length() - 1);
						result = result.replace("\"\"", "\"");
					}
					resultList.add(result);
					beginIndex = endIndex + 1;
				}
			}
		}
		return resultList;
	}

	/**
	 * 队列比较
	 * 
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
		if (a.size() != b.size())
			return false;
		Collections.sort(a);
		Collections.sort(b);
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * 根据主键路径获取文本名称路径
	 * 
	 * @param pathKey
	 * @param pathMap
	 * @return
	 */
	public static String getPathNameByKey(String namedb, String pathKey, Map<String, String> pathMap) {
		if (StringUtil.isBlank(pathKey))
			return namedb;
		List<String> pathName = new ArrayList<>();
		for (String key : pathKey.split("-")) {
			String value = pathMap.get(key);
			if (!StringUtil.isBlank(value)) {
				pathName.add(pathMap.get(key));
			}
		}
		pathName.add(namedb);
		String result = String.join("-", pathName);
		return result;
	}

	public static String[] sortStringArray(String[] arrStr) {
		String temp;
		for (int i = 0; i < arrStr.length; i++) {
			for (int j = arrStr.length - 1; j > i; j--) {
				if (arrStr[i].length() < arrStr[j].length()) {
					temp = arrStr[i];
					arrStr[i] = arrStr[j];
					arrStr[j] = temp;
				}
			}
		}
		return arrStr;
	}

	/**
	 * 根据主键路径获取文本名称路径
	 * 
	 * @param pathKey
	 * @param pathMap
	 * @return
	 */
	public static String getPathByPidName(Map<Long, String> idNameMap, Map<Long, Long> idsMap, Long id) {
		List<String> listPath = new ArrayList<>();
		while (true) {
			if (id == null || id == 0)
				break;
			String faName = idNameMap.get(id);
			listPath.add(faName);
			id = idsMap.get(id);
		}
		Collections.reverse(listPath);
		return String.join("-", listPath);
	}

	/**
	 * 根据主键路径获取文本路径id
	 * 
	 * @param pathKey
	 * @param pathMap
	 * @return
	 */
	public static String getPathByPid(Map<Long, Long> idsMap, Long id) {
		List<String> listPath = new ArrayList<>();
		while (true) {
			id = idsMap.get(id);
			if (id == null || id == 0)
				break;
			listPath.add(id.toString());
		}
		Collections.reverse(listPath);
		return String.join("-", listPath);
	}

	public static <T> Map<String, T> duplicateCoor(Map<String, T> coorMap) {
		if (coorMap == null)
			return Collections.emptyMap();
		coorMap.remove(null);
		Map<String, T> result = new HashMap<>();
		Set<String> coorSet = coorMap.keySet();
		Set<String> needRemoveKeys = new HashSet<>();
		// Iterator<String> it0 = coorSet.iterator();
		// while(it0.hasNext()){
		// String coor = it0.next();
		for (String coor : coorSet) {
			String[] coors = coor.split("-");
			Long beginIndex = Long.valueOf(coors[0]);
			Long endIndex = Long.valueOf(coors[1]);
			// Iterator<String> it = coorSet.iterator();
			// while(it.hasNext()){
			// String string = it.next();
			for (String string : coorSet) {
				if (string.equals(coor))
					continue;
				String[] strings = string.split("-");
				Long beginIndexOth = Long.valueOf(strings[0]);
				Long endIndexOth = Long.valueOf(strings[1]);
				if (beginIndexOth >= beginIndex && endIndexOth <= endIndex) {
					needRemoveKeys.add(string);
				}
			}
		}
		result.putAll(coorMap);
		Iterator<String> it = coorSet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (needRemoveKeys.contains(key)) {
				result.remove(key);
			}
		}
		return result;
	}

	/**
	 * 判断是否为空值
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmptyCollection(Collection<?> collection) {
		if (collection == null || collection.size() == 0)
			return true;
		return false;
	}

	public static String MD5Digest(String theUrl) throws Exception {

		// 获取一个MD5算法

		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(theUrl.getBytes());

		byte[] bt = md.digest();

		// System.out.println("已经转换了字节流:"+bt.length+" 摘要数据");

		// 要把字节流转换成字符串。

		StringBuffer buf = new StringBuffer("");

		// 用来获取每个byte的数值

		int i;

		for (int offset = 0; offset < bt.length; offset++) {

			// 用每一个byte填充StringBuffer

			i = bt[offset];

			// System.out.print("字节:"+i);

			if (i < 0) {

				i += 256;

			}

			// 两位的,在前边补齐0,以使一个byte显示为两个16进制的数字

			if (i < 16) {

				buf.append("0");

			}

			buf.append(Integer.toHexString(i));

		}

		return buf.toString();

	}

	/**
	 * 判断是否在该范围
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isRange(Double value, Double min, Double max) {
		if (value == null || min == null || max == null)
			return false;
		if (value >= min && value <= max)
			return true;
		return false;
	}

	public static void main(String[] args) {
		// Map<Long,String> idNameMap = new HashMap<>();
		// idNameMap.put(1L, "1");
		// idNameMap.put(2L, "2");
		// idNameMap.put(3L, "3");
		// idNameMap.put(4L, "4");
		// Map<Long,Long> idsMap = new HashMap<>();
		// idsMap.put(4L, 2L);
		// idsMap.put(2L, 1L);
		// idsMap.put(1L, null);
		// System.out.println(getPathByPidName(idNameMap, idsMap, 4L));

		// 坐标
		Map<String, String> map = new HashMap<>();
		map.put("103-105", "2020");
		map.put("83-85", "20");
		map.put("26-28", "10");
		map.put("24-26", "首届");
		map.put("22-26", "中国首届");
		map.put("3-5", "首届");
		System.out.println(duplicateCoor(map));
	}

}
