package assignmentthree.databaserecord;

import java.util.Date;

public class Record {
	
	private String fileName;
	private Date date;
	private int recordNo;
	private String record;
	
	public Record(String fileName, Date date, int recordNo, String record) {
		this.fileName = fileName;
		this.date = date;
		this.recordNo = recordNo;
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
		return recordNo;
	}

	public void setRecordNo(int recordNo) {
		recordNo = recordNo;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

}
