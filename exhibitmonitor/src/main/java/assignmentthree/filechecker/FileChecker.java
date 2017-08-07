package assignmentthree.filechecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import assignmentthree.databaserecord.DatabaseInvalidRecorder;
import assignmentthree.databaserecord.DatabaseRecorder;
import assignmentthree.databaserecord.Record;
import assignmentthree.parsedcontent.ParsedInformation;
import assignmentthree.recordtransferance.RecordTransferrer;
import assignmentthree.utilities.FileValiditor;

public class FileChecker implements Runnable {

	public static final String INPUT_FOLDER = "D:/Exhibit Monitor Folder/Input Folder";
	public static final String PROCESS_FOLDER = "D:/Exhibit Monitor Folder/Process Folder";

	@Override
	public void run() {
		int i = 0;
		while (i==0) {
			File inputFolder = new File(INPUT_FOLDER);
			String[] fileList = inputFolder.list();

			for (String s : fileList) {

				boolean fileValidity = FileValiditor.isValidFile(s);
				boolean fileDuplicity = true;
				boolean fileOnTime = false;
				if (fileValidity) {
					fileDuplicity = FileValiditor.isDuplicate(s);
				}
				if (!fileDuplicity) {
					fileOnTime = FileValiditor.isOnTime(s);
				}
				if (fileOnTime) {
					// Move to Process Folder
					File aFile = new File(INPUT_FOLDER + "/" + s);
					File bFile = new File(PROCESS_FOLDER + "/" + s);
					try {
						InputStream inStream = new FileInputStream(aFile);
						OutputStream outStream = new FileOutputStream(bFile);

						byte[] buffer = new byte[1024];
						int length;

						while ((length = inStream.read(buffer)) > 0) {
							outStream.write(buffer, 0, length);
						}

						inStream.close();
						outStream.close();

						aFile.delete();

						System.out.println(s + " File successfully copied");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			File processFolder = new File(PROCESS_FOLDER);
			String[] processDirectories = processFolder.list();
			List<List<List<String>>> wantedRecords = new ArrayList<>();

			for (String file : processDirectories) {
				String recordFilePath = PROCESS_FOLDER + "/" + file;
				List<List<String>> perFile = new ArrayList<>();
				List<String> fileName = new ArrayList<>();
				fileName.add(file);
				File processFile = new File(PROCESS_FOLDER + "/" + file);
				fileName.add(String.valueOf(processFile.lastModified()));
				perFile.add(fileName);
				String line = "";

				try (BufferedReader br = new BufferedReader(new FileReader(recordFilePath))) {

					while ((line = br.readLine()) != null) {
						List<String> recordSegment = new ArrayList<>();
						String[] records = line.split(",");
						for (String s : records) {
							recordSegment.add(s);
						}
						perFile.add(recordSegment);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
				wantedRecords.add(perFile);
			}

			// Move to Archive Folder
			for (String file : processDirectories) {
				File aFile = new File(PROCESS_FOLDER + "/" + file);
				File bFile = new File("D:/Exhibit Monitor Folder/Archive Folder/" + file);
				try {
					InputStream inStream = new FileInputStream(aFile);
					OutputStream outStream = new FileOutputStream(bFile);

					byte[] buffer = new byte[1024];
					int length;

					while ((length = inStream.read(buffer)) > 0) {
						outStream.write(buffer, 0, length);
					}

					inStream.close();
					outStream.close();

					aFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// Create collection of record objects to be sorted
			List<Record> records = new ArrayList<>();
			for (List<List<String>> recordsList : wantedRecords) {
				List<String> fileDetails = recordsList.get(0);
				String fileName = fileDetails.get(0);
				String lastModifiedDate = fileDetails.get(1);
				// Not getting the column details yet so start from 2
				for (int t = 2; t < recordsList.size(); t++) {
					List<String> perRecord = recordsList.get(t);
					int recordNo = t - 1;
					String recordDetails = "";
					for (String s : perRecord) {
						recordDetails += s;
						recordDetails += "/";
					}
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong(lastModifiedDate));
					Record record = new Record(fileName, cal.getTime(), recordNo, recordDetails);
					records.add(record);
				}
			}

			List<Thread> workers = new ArrayList<>();
			for (Record r : records) {
				// System.out.println(r.getFileName()+" "+r.getRecord()+"
				// "+r.getFileName()+" "+r.getDate());
				RecordTransferrer rt = new RecordTransferrer(r);
				Thread worker = new Thread(rt);
				workers.add(worker);

			}

			try {
				for (Thread t : workers) {
					t.start();
					t.join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			

			try {
				for (Record re : ParsedInformation.validRecords) {
					DatabaseRecorder dr = new DatabaseRecorder(re);
					Thread databaseThread = new Thread(dr);
					databaseThread.start();
					databaseThread.join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				for (Record rec : ParsedInformation.invalidRecords) {
					DatabaseInvalidRecorder dr = new DatabaseInvalidRecorder(rec);
					Thread databaseThread = new Thread(dr);
					databaseThread.start();
					databaseThread.join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

			ParsedInformation.validRecords = new Vector();
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
							for (int u = 1; u <= columnCount; i++) {

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

			i++;
		}

	}

}
