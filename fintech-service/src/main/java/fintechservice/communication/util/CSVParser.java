package fintechservice.communication.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class CSVParser {
	
	/**
	 * @CSVParser FOR DEV USE ONLY 
	 */

	private String indexName;
	private String csvPath;

	// @Value("${datasource.url}")
	private String url = "jdbc:h2:mem:testdb";

	// @Value("${datasource.username}")
	private String username = "sa";

	// @Value("${datasource.password}")
	private String password = "password";

	public CSVParser(String indexName, String csvPath) {
		this.indexName = indexName;
		this.csvPath = csvPath;

	}

	public boolean doParse() {
		try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
			// Establish database connection
			try (Connection connection = DriverManager.getConnection(url, username, password)) {
				// Prepare SQL statement for insertion
				try (PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO indexes (index, date, open, high, low, close, adj_close, volume) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

					// Skip the header line
					br.readLine();

					// Read and insert each line of the CSV file
					String line;
					while ((line = br.readLine()) != null) {
						// Split the line by comma (CSV format)
						String[] cells = line.split(",");

						// Parse the date from the first column
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						java.util.Date parsedDate = dateFormat.parse(cells[0]);
						java.sql.Date date = new java.sql.Date(parsedDate.getTime());

						// Set values for each column
						preparedStatement.setString(1, indexName);
						preparedStatement.setDate(2, date);
						preparedStatement.setDouble(3, Double.parseDouble(cells[1]));
						preparedStatement.setDouble(4, Double.parseDouble(cells[2]));
						preparedStatement.setDouble(5, Double.parseDouble(cells[3]));
						preparedStatement.setDouble(6, Double.parseDouble(cells[4]));
						preparedStatement.setDouble(7, Double.parseDouble(cells[5]));
						preparedStatement.setInt(8, Integer.parseInt(cells[6]));

						// Execute the SQL statement
						preparedStatement.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}
}
