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

			if (targetFile.equals(fileName)) {
				columnSize = ls.size() - 1;

				String recordDetails = this.targetRecord.getRecord().trim();
				String[] rowValues = recordDetails.split("/");
				if (rowValues.length == columnSize) {

					for (int u = 0; u < rowValues.length; u++) {
						String recordSettings = ls.get(u + 1);
						String[] settingsFragment = recordSettings.split(" ");
						if (settingsFragment[1].equals("date")) {
							DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
							try {
								format.parse(rowValues[u]);
							} catch (ParseException e) {
								ParsedInformation.invalidRecords.add(targetRecord);
								break;
							}
						} else if (settingsFragment[1].equals("int")) {
							try {
								Integer.parseInt(rowValues[u]);
							} catch (NumberFormatException e) {
								ParsedInformation.invalidRecords.add(targetRecord);
								break;
							}
						} else if (settingsFragment[1].equals("double")) {
							try {
								Double.parseDouble(rowValues[u]);
							} catch (NumberFormatException e) {
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
