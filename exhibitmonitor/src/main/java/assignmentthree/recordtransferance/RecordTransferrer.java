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
			String[] fragmentDetails = fileDetails.split(" ");
			String targetFile = fragmentDetails[0];

			// checking of validity of record should be done here
			if (targetFile.equals(fileName)) {
				columnSize = ls.size() - 1;

				String recordDetails = this.targetRecord.getRecord().trim();
				String[] rowValues = recordDetails.split("/");

				if (rowValues.length == columnSize) {
					ParsedInformation.validRecords.add(targetRecord);
				} else {
					ParsedInformation.invalidRecords.add(targetRecord);
				}
			}
		}
	}

}
