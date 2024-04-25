package fintechservice.communication.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import fintechservice.communication.dao.CommunicationRepository;
import fintechservice.communication.dto.IndexCloseValueDto;
import fintechservice.communication.dto.IndexCorrelationRequestDto;
import fintechservice.communication.dto.IndexHistoryResponseDto;
import fintechservice.communication.dto.IndexIncomeApyAllDateDto;
import fintechservice.communication.dto.IndexIncomeApyResponseDto;
import fintechservice.communication.dto.IndexIncomeIrrResponseDto;
import fintechservice.communication.dto.IndexRequestDto;
import fintechservice.communication.dto.IndexRequestWithAmountFieldDto;
import fintechservice.communication.dto.SourceHistoryDto;
import fintechservice.communication.dto.SourceLinkDto;
import fintechservice.communication.dto.SourceLinksRequestDto;
import fintechservice.communication.dto.SourceRequestDto;
import fintechservice.communication.dto.SourceResponseDto;
import fintechservice.communication.model.Index;
import fintechservice.communication.util.IndexStatisticsCalculator;
import fintechservice.exceptions.PathInvalidException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

	final CommunicationRepository communicationRepository;
	final ModelMapper modelMapper;

	@Override
	public boolean addHistoryWithFile(String name, String path) throws PathInvalidException {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				// Skip the header line
				br.readLine();
				// Read and insert each line of the CSV file
				String line;
				List<Index> indexes = new ArrayList<Index>();
				while ((line = br.readLine()) != null) {
					// Split the line by comma (CSV format)
					String[] cells = line.split(",");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate localDate = LocalDate.parse(cells[0], formatter);
					/* @formatter:off */
					Index index = new Index(
							name, 
							localDate,
							Double.parseDouble(cells[1]), 
							Double.parseDouble(cells[2]),
							Double.parseDouble(cells[3]),
							Double.parseDouble(cells[4]),
							Double.parseDouble(cells[5]),
							Integer.parseInt(cells[6]));
					/* @formatter:on */
					indexes.add(index);
				}
				communicationRepository.saveAllAndFlush(indexes);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		System.out.println(communicationRepository.count() + " Indexes added to DB");
		return true;
	}

	@Override
	public Iterable<SourceResponseDto> parserForYahooFinance(SourceRequestDto sourceRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<SourceLinkDto> addNewIndexes(SourceLinksRequestDto sourceLinksRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SourceHistoryDto getTimeHistoryForIndex(@PathVariable String index) {
		LocalDate from = communicationRepository.findMinDateInIndex(index);
		LocalDate to = communicationRepository.findMaxDateInIndex(index);
		return new SourceHistoryDto(index, from, to);
	}

	@Transactional
	@Override
	public List<String> getAllIndexes() {
		return communicationRepository.getAllIndexList().collect(Collectors.toList());
	}

	@Transactional
	@Override
	public List<IndexHistoryResponseDto> getPeriodBetweenForIndex(IndexRequestDto indexRequestDto) {
		List<IndexHistoryResponseDto> response = new ArrayList<>();
		Map<String, List<IndexHistoryResponseDto>> responseMap = new HashMap<>();

		List<String> indexNames = indexRequestDto.getIndexs();
		String type = indexRequestDto.getType();
		int quantity = indexRequestDto.getQuantity();
		LocalDate from = indexRequestDto.getFrom();
		LocalDate to = indexRequestDto.getTo();

		// Iterate over the time range
		while (!from.isAfter(to)) {
			LocalDate periodStart = from;
			LocalDate periodEnd = IndexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Calculate statistics for each index within the current period
			for (String indexName : indexNames) {

				// Retrieve indexes within the current period
				List<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd)
						.collect(Collectors.toList());

				// Calculate statistics for the current period
				IndexHistoryResponseDto periodStatistics = IndexStatisticsCalculator
						.calculateStatisticsForPeriod(indexes, indexName, periodStart, periodEnd, type, quantity);

				// Add statistics to the response map
				responseMap.computeIfAbsent(indexName, k -> new ArrayList<>()).add(periodStatistics);
			}
			// Move to the next period
			from = periodEnd.plusDays(1);
		}

		// Aggregate statistics for each index and add to the response list
		for (Map.Entry<String, List<IndexHistoryResponseDto>> entry : responseMap.entrySet()) {
			response.add(IndexStatisticsCalculator.aggregateStatistics(entry.getKey(), entry.getValue()));
		}

		return response;
	}

	@Override
	public Iterable<IndexCloseValueDto> getAllValueCloseBetween(IndexRequestDto indexRequestDto) {
		List<IndexCloseValueDto> response = new ArrayList<>();

		// Extract request parameters
		List<String> indexNames = indexRequestDto.getIndexs();
		String type = indexRequestDto.getType();
		int quantity = indexRequestDto.getQuantity();
		LocalDate from = indexRequestDto.getFrom();
		LocalDate to = indexRequestDto.getTo();

		// Iterate over the time range
		while (!from.isAfter(to)) {
			LocalDate periodStart = from;
			LocalDate periodEnd = IndexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Calculate statistics for each index within the current period
			for (String indexName : indexNames) {
				// Retrieve indexes within the current period
				List<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd)
						.collect(Collectors.toList());
				// Calculate statistics for the current period
				IndexCloseValueDto subPeriodQuotes = IndexStatisticsCalculator.calculateSubPeriodQuotes(indexes,
						indexName, periodStart, periodEnd, type, quantity);
				// Add sub-period quotes to the response list
				response.add(subPeriodQuotes);
			}
			// Move to the next period
			from = periodEnd.plusDays(1);
		}

		return response;
	}

	@Override
	public IndexHistoryResponseDto calcSumPackage(IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexIncomeApyResponseDto calcIncomeWithApy(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<IndexIncomeApyAllDateDto> calcIncomeWithApyAllDate(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<IndexIncomeIrrResponseDto> calcIncomeWithIrr(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String calcCorrelation(IndexCorrelationRequestDto indexCorrelationRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteAllHistoryForCompany() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double prediction() {
		// TODO Auto-generated method stub
		return 0;
	}

//	private IndexHistoryResponseDto calculateStatisticsForPeriod(String indexName, LocalDate periodStart,
//			LocalDate periodEnd) {
//
//		Stream<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd);
//
//		double max = indexes.mapToDouble(Index::getHigh).max().orElse(0);
//		double mean = indexes.mapToDouble(Index::getClose).average().orElse(0);
//		double median = calculateMedian(indexes.map(Index::getClose).sorted().collect(Collectors.toList()));
//		double min = indexes.mapToDouble(Index::getLow).min().orElse(0);
//		double std = calculateStandardDeviation(
//				indexes.mapToDouble(Index::getClose).boxed().collect(Collectors.toList()), mean);
//
//		return new IndexHistoryResponseDto(periodStart, periodEnd, indexName, "days", max, mean, median, min, std);
//	}
//
//	private double calculateMedian(List<Double> sortedClosePrices) {
//		int size = sortedClosePrices.size();
//		if (size % 2 == 0) {
//			return (sortedClosePrices.get(size / 2 - 1) + sortedClosePrices.get(size / 2)) / 2;
//		} else {
//			return sortedClosePrices.get(size / 2);
//		}
//	}
//
//	private double calculateStandardDeviation(List<Double> dataList, double mean) {
//		double sumOfSquaredDifferences = dataList.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum();
//		int count = dataList.size();
//		return Math.sqrt(sumOfSquaredDifferences / count);
//	}
//
//	private LocalDate calculatePeriodEnd(LocalDate periodStart, String type, int quantity, LocalDate endDate) {
//		switch (type) {
//		case "days":
//			return periodStart.plusDays(quantity).isBefore(endDate) ? periodStart.plusDays(quantity) : endDate;
//		case "weeks":
//			return periodStart.plusWeeks(quantity).isBefore(endDate) ? periodStart.plusWeeks(quantity) : endDate;
//		case "months":
//			return periodStart.plusMonths(quantity).isBefore(endDate) ? periodStart.plusMonths(quantity) : endDate;
//		case "years":
//			return periodStart.plusYears(quantity).isBefore(endDate) ? periodStart.plusYears(quantity) : endDate;
//		// TODO check if needs more
//		default:
//			throw new IllegalArgumentException("Unsupported period type: " + type);
//		}
//	}

}
