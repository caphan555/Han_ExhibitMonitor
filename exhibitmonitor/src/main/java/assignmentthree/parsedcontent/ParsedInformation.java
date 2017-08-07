package assignmentthree.parsedcontent;

import java.util.List;
import java.util.Vector;

import assignmentthree.databaserecord.Record;

public class ParsedInformation {
	public static List<List<String>> configurationInfo;
	public static List<Record> validRecords = new Vector();
	public static List<Record> invalidRecords = new Vector();
	
	public ParsedInformation(List<List<String>> configInfo) {
		ParsedInformation.configurationInfo = configInfo;
	}

	public static List<List<String>> getConfigurationInfo() {
		return configurationInfo;
	}

	public static void setConfigurationInfo(List<List<String>> configurationInfo) {
		ParsedInformation.configurationInfo = configurationInfo;
	}
	
	
}
