package assignmentthree.utilities;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import assignmentthree.parsedcontent.ParsedInformation;

public class OutputFileProducer implements Runnable{

	@Override
	public void run() {
		File output = new File("D:/Exhibit Monitor Folder/Output Folder");
		String[] outputList = output.list();
		try {
			String finalFileDetails = ParsedInformation.configurationInfo.get(2).get(0);
			String[] fileFragmentDetails = finalFileDetails.split(" ");
			if (outputList.length == 0) {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connectionCsv = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root",
						"superman555");

				Statement stmt = connectionCsv.createStatement();
				stmt.executeQuery("use sakila");

				String filename = "D:/Exhibit Monitor Folder/Output Folder/" + fileFragmentDetails[0];

				ResultSet res = stmt.executeQuery("select * from records");
				FileWriter fw = new FileWriter(filename);
				int columnCount = res.getMetaData().getColumnCount();
				for (int t = 1; t <= columnCount; t++) {
					fw.append(res.getMetaData().getColumnName(t));
					fw.append(',');
				}
				fw.append('\n');

				while (res.next()) {
					for (int u = 1; u <= columnCount; u++) {
						if (res.getObject(u) != null) {
							String data = res.getObject(u).toString();
							fw.append(data);
							fw.append(',');
						} else {
							String data = "null";
							fw.append(data);
							fw.append(',');
						}

					}
					fw.append('\n');
				}

				fw.flush();
				fw.close();
			} else {
				boolean fileExists = false;
				for (String s : outputList) {
					if (s.equals(fileFragmentDetails[0])) {
						fileExists = true;
					}
				}

				if (!fileExists) {
					Class.forName("com.mysql.jdbc.Driver");
					Connection connectionCsv = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root",
							"superman555");

					Statement stmt = connectionCsv.createStatement();
					stmt.executeQuery("use sakila");

					String filename = "D:/Exhibit Monitor Folder/Output Folder/" + fileFragmentDetails[0];

					ResultSet res = stmt.executeQuery("SELECT * FROM RECORDS");
					FileWriter fw = new FileWriter(filename);
					int columnCount = res.getMetaData().getColumnCount();
					for (int t = 1; t <= columnCount; t++) {
						fw.append(res.getMetaData().getColumnName(t));
						fw.append(",");
					}
					fw.append(System.getProperty("line.separator"));

					while (res.next()) {
						for (int u = 1; u <= columnCount; u++) {

							if (res.getObject(u) != null) {
								String data = res.getObject(u).toString();
								fw.append(data);
								fw.append(",");
							} else {
								String data = "null";
								fw.append(data);
								fw.append(",");
							}

						}
						fw.append('\n');
					}

					fw.flush();
					fw.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
