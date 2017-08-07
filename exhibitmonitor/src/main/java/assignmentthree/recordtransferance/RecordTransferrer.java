package assignmentthree.recordtransferance;

import assignmentthree.databaserecord.Record;
import assignmentthree.parsedcontent.ParsedInformation;
import java.util.List;

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
			System.out.println("file details: " + fileDetails);
			String[] fragmentDetails = fileDetails.split(" ");
			String targetFile = fragmentDetails[0];
			System.out.println("target file test: " + targetFile);

			// checking of validity of record should be done here
			System.out.println("fileName test in record transfer: " + fileName);
			if (targetFile.equals(fileName)) {
				columnSize = ls.size() - 1;
				System.out.println("TEST: " + fileDetails + " " + columnSize);

				String recordDetails = this.targetRecord.getRecord().trim();
				System.out.println("record details test: " + recordDetails);
				String[] rowValues = recordDetails.split("/");

				if (rowValues.length == columnSize) {
					System.out.println("Adding to valid Records: " + recordDetails);
					ParsedInformation.validRecords.add(targetRecord);
				} else {
					ParsedInformation.invalidRecords.add(targetRecord);
				}
			}
		}
	}

}
