package com.barclays.card.bot.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
	public static void main(String[] args) {
		System.out.println(
				checkFilePresence("D:\\temp\\src\\main\\resources\\test", "D:\\temp\\src\\main\\resources\\prod"));
	}

	public static boolean checkFilePresence(String refDir, String srcDir) {
		Map<String, String> filesInRefrenceDir = getFile(new File(refDir));
		Map<String, String> filesInSrcDir = getFile(new File(srcDir));
		return filesInRefrenceDir.equals(filesInSrcDir);

	}

	private static Map<String, String> getFile(File folder) {
		Map<String, String> fileDirMap = new HashMap<String, String>();
		if (folder.isDirectory()) {
			File[] listOfFile = folder.listFiles();
			Arrays.stream(listOfFile).forEach(p -> {
				if (p.isDirectory()) {
					fileDirMap.putAll(getFile(p));
				} else {
					fileDirMap.put(p.getName(), p.getPath());
				}
			});
		} else {
			fileDirMap.put(folder.getName(), folder.getPath());
		}
		return fileDirMap;
	}

}
