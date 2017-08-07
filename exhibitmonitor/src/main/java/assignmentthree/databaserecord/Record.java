package assignmentthree.databaserecord;

import java.util.Date;

public class Record {
	
	private String fileName;
	private Date date;
	private int RecordNo;
	private String record;
	
	public Record(String fileName, Date date, int recordNo, String record) {
		this.fileName = fileName;
		this.date = date;
		RecordNo = recordNo;
		this.record = record;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRecordNo() {
		return RecordNo;
	}

	public void setRecordNo(int recordNo) {
		RecordNo = recordNo;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

}
