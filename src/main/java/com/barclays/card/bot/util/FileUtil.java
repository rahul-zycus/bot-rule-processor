package com.barclays.card.bot.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

	public static void main(String[] args) {
		FileUtil fileUtil = new FileUtil();
		System.out.println(fileUtil.checkFilePresence("D:\\temp\\src\\main\\resources\\test",
				"D:\\temp\\src\\main\\resources\\prod"));
	}

	public Optional <Map<String, String>> checkFilePresence(String refDir, String srcDir) {
		Map<String, String> filesInRefrenceDir = getFile(new File(refDir));
		Map<String, String> filesInSrcDir = getFile(new File(srcDir));
		Map<String, String> fileDiffMap = diffInMap(filesInRefrenceDir, filesInSrcDir);
		if (!fileDiffMap.isEmpty()) {
			LOGGER.error("MISMATCH_IN_CONFIG_FILE_LOCATION");
			return Optional.of(fileDiffMap);
		}
		return Optional.empty();
	}

	// Return Map of key and value which is present in refMap but not present in
	// srcMap.
	private Map<String, String> diffInMap(Map<String, String> refMap, Map<String, String> srcMap) {
		Map<String, String> diffMap = new HashMap<String, String>();
		for (Entry<String, String> entry : refMap.entrySet()) {
			if (!srcMap.containsKey(entry.getKey())) {
				diffMap.put(entry.getKey(), entry.getValue());
			}
		}
		return diffMap;
	}

	private Map<String, String> getFile(File folder) {
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
