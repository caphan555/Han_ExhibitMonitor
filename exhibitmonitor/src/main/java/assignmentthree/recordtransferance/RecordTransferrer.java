package assignmentthree.recordtransferance;

import assignmentthree.databaserecord.Record;
import assignmentthree.parsedcontent.ParsedInformation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class RecordTransferrer implements Runnable {

	private Record targetRecord;

	public RecordTransferrer(Record targetRecord) {
		this.targetRecord = targetRecord;
	}

	@Override
	public void run() {
		String fileName = this.targetRecord.getFileName();

		int columnSize = 0;
		for (List<String> ls : ParsedInformation.configurationInfo) {
			String fileDetails = ls.get(0);
			String[] fragmentDetails = fileDetails.split(" ");
			String targetFile = fragmentDetails[0];

			// checking of validity of record should be done here
			if (targetFile.equals(fileName)) {
				columnSize = ls.size() - 1;

				String recordDetails = this.targetRecord.getRecord().trim();
				String[] rowValues = recordDetails.split("/");
				//System.out.println("record details: " + recordDetails);
				if (rowValues.length == columnSize) {

					for (int u = 0; u < rowValues.length; u++) {
						String recordSettings = ls.get(u + 1);
						String[] settingsFragment = recordSettings.split(" ");
						//System.out.println("settings fragment: " + settingsFragment[1]);
						if (settingsFragment[1].equals("date")) {
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
							try {
								Date date = format.parse(rowValues[u]);
							} catch (ParseException e) {
								System.out.println("Adding to invalid records date: " + recordDetails);
								ParsedInformation.invalidRecords.add(targetRecord);
								break;
							}
						} else if (settingsFragment[1].equals("int")) {
							try {
								int recordInt = Integer.parseInt(rowValues[u]);
							} catch (NumberFormatException e) {
								System.out.println("Adding to invalid records int: " + recordDetails);
								ParsedInformation.invalidRecords.add(targetRecord);
								break;
							}
						} else if (settingsFragment[1].equals("double")) {
							try {
								double recordInt = Double.parseDouble(rowValues[u]);
							} catch (NumberFormatException e) {
								System.out.println("Adding to invalid records double: " + recordDetails);
								ParsedInformation.invalidRecords.add(targetRecord);
								break;
							}
						}
						int counter = u+1;
						if(counter==rowValues.length) {
							ParsedInformation.validRecords.add(targetRecord);
						}
					}
				} else {
					ParsedInformation.invalidRecords.add(targetRecord);
					continue;
				}
			}
		}
	}

}
