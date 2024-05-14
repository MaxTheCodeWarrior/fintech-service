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
import fintechservice.communication.dto.CorrelationCoefficientDto;
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
	final IndexStatisticsCalculator indexStatisticsCalculator;

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
			LocalDate periodEnd = indexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Calculate statistics for each index within the current period
			for (String indexName : indexNames) {

				// Retrieve indexes within the current period
				List<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd)
						.collect(Collectors.toList());

				// Calculate statistics for the current period
				IndexHistoryResponseDto periodStatistics = indexStatisticsCalculator
						.calculateStatisticsForPeriod(indexes, indexName, periodStart, periodEnd, type, quantity);

				// Add statistics to the response map
				responseMap.computeIfAbsent(indexName, k -> new ArrayList<>()).add(periodStatistics);
			}
			// Move to the next period
			from = periodEnd.plusDays(1);
		}

		// Aggregate statistics for each index and add to the response list
		for (Map.Entry<String, List<IndexHistoryResponseDto>> entry : responseMap.entrySet()) {
			response.add(indexStatisticsCalculator.aggregateStatistics(entry.getKey(), entry.getValue()));
		}

		return response;
	}

	@Transactional
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
			LocalDate periodEnd = indexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Calculate statistics for each index within the current period
			for (String indexName : indexNames) {
				// Retrieve indexes within the current period
				List<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd)
						.collect(Collectors.toList());
				// Calculate statistics for the current period

				IndexCloseValueDto subPeriodQuotes = indexStatisticsCalculator.calculateSubPeriodQuotes(indexes,
						indexName, periodStart, periodEnd, type, quantity);

				// Add sub-period quotes to the response list
				response.add(subPeriodQuotes);
			}
			// Move to the next period
			from = periodEnd.plusDays(1); // Move to the day after the period end to include it
		}

		return response;
	}

	@Transactional
	@Override
	public IndexHistoryResponseDto calcSumPackage(IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto) {
		List<IndexHistoryResponseDto> periodStatisticsList = new ArrayList<>();

		List<String> indexNames = indexRequestWithAmountFieldDto.getIndexs();
		String type = indexRequestWithAmountFieldDto.getType();
		int quantity = indexRequestWithAmountFieldDto.getQuantity();
		LocalDate from = indexRequestWithAmountFieldDto.getFrom();
		LocalDate to = indexRequestWithAmountFieldDto.getTo();
		List<Double> amount = indexRequestWithAmountFieldDto.getAmount();

		// Iterate over the time range
		while (!from.isAfter(to)) {
			LocalDate periodStart = from;
			LocalDate periodEnd = indexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Calculate statistics for each index within the current period
			for (int i = 0; i < indexNames.size(); i++) {

				// Retrieve indexes within the current period
				List<Index> indexes = communicationRepository
						.findByIndexBetween(indexNames.get(i), periodStart, periodEnd).collect(Collectors.toList());

				// Calculate statistics for the current period
				IndexHistoryResponseDto periodStatistics = indexStatisticsCalculator
						.calculateStatisticsForPeriodWithAmount(indexes, indexNames.get(i), periodStart, periodEnd,
								type, quantity, amount.get(i));

				// Add statistics to list for aggregation
				periodStatisticsList.add(periodStatistics);
			}
			// Move to the next period
			from = periodEnd.plusDays(1);
		}

		indexNames = indexNames.stream().distinct().collect(Collectors.toList());

		return indexStatisticsCalculator.aggregateStatistics("Package for: " + indexNames.toString(),
				periodStatisticsList);
	}

	@Transactional
	@Override
	public List<IndexHistoryResponseDto> calcSumPackageWithoutAggreagtion(
			IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto) {
		
		List<IndexHistoryResponseDto> aggregatedStatisticsList = new ArrayList<>();

		List<String> indexNames = indexRequestWithAmountFieldDto.getIndexs();
		String type = indexRequestWithAmountFieldDto.getType();
		int quantity = indexRequestWithAmountFieldDto.getQuantity();
		LocalDate from = indexRequestWithAmountFieldDto.getFrom();
		LocalDate to = indexRequestWithAmountFieldDto.getTo();
		List<Double> amounts = indexRequestWithAmountFieldDto.getAmount();
		
		Map<String, Double> purshasedPricesForIndexes = new HashMap<>();
		
		// Iterate over the time range
		while (!from.isAfter(to)) {

			LocalDate periodStart = from;
			LocalDate periodEnd = indexStatisticsCalculator.calculatePeriodEnd(from, type, quantity, to);

			// Iterate over each indexName to calculate statistics for each
			List<IndexHistoryResponseDto> periodStatisticsList = new ArrayList<>();
			

			for (int i = 0; i < indexNames.size(); i++) {
				String indexName = indexNames.get(i);

				// Retrieve indexes for the current indexName within the current period
				List<Index> indexes = communicationRepository.findByIndexBetween(indexName, periodStart, periodEnd)
						.collect(Collectors.toList());
				
				// For getting the amount of indexes that was purchased in package
				purshasedPricesForIndexes.putIfAbsent(indexName, amounts.get(i) / indexes.get(0).getClose());

				// Calculate statistics for the current indexName and add to the list
				IndexHistoryResponseDto periodStatistics = indexStatisticsCalculator
						.calculateStatisticsForPeriodWithAmount(indexes, indexName, periodStart, periodEnd, type,
								quantity, purshasedPricesForIndexes.get(indexName));

				periodStatisticsList.add(periodStatistics);
			}

			// Aggregate statistics for the current period and add to the aggregated list
			IndexHistoryResponseDto aggregatedStatistics = indexStatisticsCalculator
					.aggregateStatistics("Package for: " + String.join(", ", indexNames), periodStatisticsList);
			aggregatedStatisticsList.add(aggregatedStatistics);

			// Move to the next period
			from = periodEnd.plusDays(1);
		}

		return aggregatedStatisticsList;
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

	@Transactional
	@Override
	public CorrelationCoefficientDto calcCorrelation(IndexCorrelationRequestDto indexCorrelationRequestDto) {
		List<String> indexNames = indexCorrelationRequestDto.getIndexs();
		LocalDate periodStart = indexCorrelationRequestDto.getFrom();
		LocalDate periodEnd = indexCorrelationRequestDto.getTo();

		List<Index> firstIndex = communicationRepository.findByIndexBetween(indexNames.get(0), periodStart, periodEnd)
				.collect(Collectors.toList());

		List<Index> secondIndex = communicationRepository.findByIndexBetween(indexNames.get(1), periodStart, periodEnd)
				.collect(Collectors.toList());

		// Calculate correlation coefficient
		double correlationCoefficient = indexStatisticsCalculator.calculateCorrelationCoefficient(firstIndex,
				secondIndex);

		// Determine correlation description based on correlation coefficient
		String correlationDescription = indexStatisticsCalculator
				.determineCorrelationDescription(correlationCoefficient);

		// Calculate profit and annual yield for each stock
		List<Double> profits = indexStatisticsCalculator.calculateProfits(firstIndex, secondIndex);
		List<Double> annualYields = indexStatisticsCalculator.calculateAnnualYields(firstIndex, secondIndex);

		// Calculate minimum and maximum yields
		double minProfit = profits.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
		double maxProfit = profits.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
		double minAnnualYield = annualYields.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
		double maxAnnualYield = annualYields.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

		// Display the results
		CorrelationCoefficientDto correlationDto = new CorrelationCoefficientDto(correlationCoefficient,
				correlationDescription, minProfit, maxProfit, minAnnualYield, maxAnnualYield);
		return correlationDto;
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
