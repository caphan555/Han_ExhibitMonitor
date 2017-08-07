package assignmentthree.databaserecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseInvalidRecorder implements Runnable{
private Record record;
	
	
	
	public DatabaseInvalidRecorder(Record record) {
		this.record = record;
	}

	@Override
	public void run() {
		
		try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root","superman555");
		try {
				
				
				//Statement stmt =
				Statement stmt = connection.createStatement();
				stmt.executeQuery("use sakila");
				
				//converting java.util.date to java.sql.date
				java.sql.Date sqlDate = new java.sql.Date(this.getRecord().getDate().getTime());
				String fileName = this.getRecord().getFileName();
				int recordNo = this.getRecord().getRecordNo();
				String recordDetails = this.getRecord().getRecord();
				
				
				System.out.println("testing database insertion");
				 String query = " insert into invalidrecords (fileName, date, recordNo, recordDetails) values (?, ?, ?, ?)";
			      PreparedStatement preparedStmt = connection.prepareStatement(query);
			      preparedStmt.setString(1, fileName);
			      preparedStmt.setDate(2, sqlDate);
			      preparedStmt.setInt (3, recordNo);
			      preparedStmt.setString (4, recordDetails);
			      
			      preparedStmt.execute();
			      System.out.println("executed invalid insertion");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			connection.close();
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
}
