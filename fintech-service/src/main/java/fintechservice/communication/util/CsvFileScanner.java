package fintechservice.communication.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fintechservice.communication.dao.CommunicationRepository;
import fintechservice.communication.model.Index;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvFileScanner implements CommandLineRunner {

	final CommunicationRepository communicationRepository; 

	@Override
	public void run(String... args) throws Exception {
		// Define the directory to scan for CSV files
		String directoryPath = ".";

		// Define the CSV file extension
		String csvFileExtension = ".csv";

		// Recursively scan the directory for CSV files
		Files.walk(Paths.get(directoryPath)).filter(Files::isRegularFile)
				.filter(file -> file.toString().toLowerCase().endsWith(csvFileExtension)).forEach(this::processCsvFile);
	}

	private void processCsvFile(Path csvFilePath) {
		String fileName = csvFilePath.getFileName().toString();
		String indexName = fileName.substring(0, fileName.lastIndexOf(".csv"));
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath.toFile()))) {
			// Skip the header line
			br.readLine();

			// Read and insert each line of the CSV file
			String line;
			List<Index> indexes = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				
				// Split the line by comma (CSV format)
				String[] cells = line.split(",");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(cells[0], formatter);

				
				/* @formatter:off */
				Index index = new Index(
						indexName, 
						localDate, 
						Double.parseDouble(cells[1]),
						Double.parseDouble(cells[2]),
						Double.parseDouble(cells[3]), 
						Double.parseDouble(cells[4]),
						Double.parseDouble(cells[5]),
						Integer.parseInt(cells[6]));
				indexes.add(index);
				/* @formatter:on */
				
			}

			// Save the list of Index objects to the database
			communicationRepository.saveAllAndFlush(indexes);
			System.out.println(indexes.size() + " indexes of " + indexName + " added to DB from file: " + csvFilePath);
		} catch (Exception e) {
			System.out.println("Error processing file: " + csvFilePath + ". " + e.getMessage());
		}
	}
}
