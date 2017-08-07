package assignmentthree.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import assignmentthree.parsedcontent.ParsedInformation;

public class FileValiditor {
	
	public static Map directoryBook = new HashMap();
	
	public static boolean isValidFile(String targetFileName) {
		List<List<String>> configurationInfo = ParsedInformation.configurationInfo;
		int configSize = configurationInfo.size();
		--configSize;
		List<List<String>> fileInfo = new ArrayList<>();
		for(int i=0; i<configSize; i++) {
			fileInfo.add(configurationInfo.get(i));
		}
		
		for(List<String> l: fileInfo) {
			String targetInfo = l.get(0);
			String[] fileName = targetInfo.split(" ");
			if(fileName[0].equals(targetFileName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isDuplicate(String fileName) {
		for(Object k: directoryBook.keySet()) {
			String fileKey = (String) k;
			if(fileKey.equals(fileName)) {
				return true;
			} else {
				File targetFile = new File("D:/Exhibit Monitor Folder/Input Folder/"+fileName);
				if(targetFile.lastModified() == (long)directoryBook.get(fileKey)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isOnTime(String targetFileName) {
		List<List<String>> configurationInfo = ParsedInformation.configurationInfo;
		int configSize = configurationInfo.size();
		--configSize;
		List<List<String>> fileInfo = new ArrayList<>();
		for(int i=0; i<configSize; i++) {
			fileInfo.add(configurationInfo.get(i));
		}
		
		for(List<String> l: fileInfo) {
			String targetInfo = l.get(0);
			String[] fileName = targetInfo.split(" ");
			if(fileName[0].equals(targetFileName)) {
				String targetTime = fileName[1];
				String gracePeriod = fileName[2];
				String[] timeParts = targetTime.split(":");
				int targetMinutes = Integer.parseInt(timeParts[1]) + Integer.parseInt(gracePeriod);
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
				cal.set(Calendar.MINUTE, targetMinutes);
				
				File targetFile = new File("D:/Exhibit Monitor Folder/Input Folder/"+targetFileName);
				
				if(cal.getTimeInMillis() > targetFile.lastModified()) {
					System.out.println("returning true for on time");
					return true;
				}
			}
		}
		System.out.println("returning false for on time");
		return false;
	}
}
