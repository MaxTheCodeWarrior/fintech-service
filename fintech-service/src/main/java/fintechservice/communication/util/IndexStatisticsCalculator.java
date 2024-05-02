package fintechservice.communication.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fintechservice.communication.dto.CorrelationDescriptionEnum;
import fintechservice.communication.dto.IndexCloseValueDto;
import fintechservice.communication.dto.IndexHistoryResponseDto;
import fintechservice.communication.model.Index;
import fintechservice.exceptions.DateOutOfRangeException;
/* @formatter:off */
@Component
public class IndexStatisticsCalculator {

	public LocalDate calculatePeriodEnd(LocalDate periodStart, String type, int quantity, LocalDate endDate) {
		
		String fixedType = type.equals("day") ? "days" : type;
		
		switch (fixedType) {
		case "days":
			return periodStart.plusDays(quantity).isBefore(endDate) ? periodStart.plusDays(quantity) : endDate;
		case "weeks":
			return periodStart.plusWeeks(quantity).isBefore(endDate) ? periodStart.plusWeeks(quantity) : endDate;
		case "months":
			return periodStart.plusMonths(quantity).isBefore(endDate) ? periodStart.plusMonths(quantity) : endDate;
		case "years":
			return periodStart.plusYears(quantity).isBefore(endDate) ? periodStart.plusYears(quantity) : endDate;
		default:
			throw new IllegalArgumentException("Unsupported period type: " + type);
		}
	}

	public IndexHistoryResponseDto calculateStatisticsForPeriod(List<Index> indexes, String indexName,
			LocalDate periodStart, LocalDate periodEnd, String type, int quantity) {
		
		double max = indexes.stream()
				.mapToDouble(Index::getHigh)
				.max()
				.orElse(0);
		
		double mean = indexes.stream()
				.mapToDouble(Index::getClose)
				.average()
				.orElse(0);
		
		double median = calculateMedian(indexes.stream()
								.mapToDouble(Index::getClose)
								.boxed()
								.collect(Collectors.toList())
								);
		
		double min = indexes.stream()
				.mapToDouble(Index::getLow)
				.min()
				.orElse(0);
		
		double std = calculateStandardDeviation(indexes.stream()
										.mapToDouble(Index::getClose)
										.boxed()
										.collect(Collectors.toList())
										, mean);

		return new IndexHistoryResponseDto(periodStart, periodEnd, indexName, quantity + " " + type, max, mean, median,
				min, std);
	}

	public IndexHistoryResponseDto calculateStatisticsForPeriodWithAmount(List<Index> indexes, String indexName,
			LocalDate periodStart, LocalDate periodEnd, String type, int quantity, double amount) {

		// Calculate the sum of quotes for the current index, include the amount 
		double sum = indexes.stream()
			    .mapToDouble(index -> index.getClose() * amount)
			    .reduce(0.0, Double::sum);

		// Calculate statistical indicators based on the sums
		double max = sum;
		double min = sum;

		// Calculate the actual maximum and minimum values based on the sum of quotes
		for (int i = 0; i < indexes.size(); i++) {
			double currentSum = indexes.get(i).getClose() * amount;
			
			max = currentSum > max ? currentSum : max;
		
			min = currentSum < min ? currentSum : min;
			
		}

		double mean = sum / indexes.size();
		
		double median = calculateMedian(indexes.stream()
								.mapToDouble(index -> index.getClose() * amount)
								.boxed()
								.collect(Collectors.toList())
								);
		
		double std = calculateStandardDeviation(indexes.stream()
										.mapToDouble(index -> index.getClose() * amount)
										.boxed()
										.collect(Collectors.toList())
										, mean);

		return new IndexHistoryResponseDto(periodStart, periodEnd, indexName, quantity + " " + type, max, mean, median,
				min, std);
	}

	public double calculateMedian(List<Double> sortedClosePrices) {
		int size = sortedClosePrices.size();
		if (size == 0) {
			throw new DateOutOfRangeException();
		}
		if (size % 2 == 0) {
			return (sortedClosePrices.get(size / 2 - 1) + sortedClosePrices.get(size / 2)) / 2;
		} else {
			return sortedClosePrices.get(size / 2);
		}

	}

	public double calculateStandardDeviation(List<Double> dataList, double mean) {
		
		double sumOfSquaredDifferences = dataList.stream()
										.mapToDouble(value -> Math.pow(value - mean, 2))
										.sum();
		
		int count = dataList.size();
		
		return Math.sqrt(sumOfSquaredDifferences / count);
	}

	public IndexHistoryResponseDto aggregateStatistics(String indexName, List<IndexHistoryResponseDto> periodStatisticsList) {
		
		double max = periodStatisticsList.stream()
				.mapToDouble(IndexHistoryResponseDto::getMax)
				.max()
				.orElse(0);
		
		double mean = periodStatisticsList.stream()
				.mapToDouble(IndexHistoryResponseDto::getMean)
				.average()
				.orElse(0);
		
		double median = calculateMedian(periodStatisticsList.stream()
									.mapToDouble(IndexHistoryResponseDto::getMedian)
									.boxed()
									.collect(Collectors.toList()));
		
		double min = periodStatisticsList.stream()
				.mapToDouble(IndexHistoryResponseDto::getMin)
				.min()
				.orElse(0);
		
		double std = calculateStandardDeviation(periodStatisticsList.stream()
											.mapToDouble(IndexHistoryResponseDto::getStd)
											.boxed()
											.collect(Collectors.toList())
											, mean);
		
		return new IndexHistoryResponseDto(periodStatisticsList.get(0).getFrom(), periodStatisticsList.get(0).getTo(),
				indexName, periodStatisticsList.get(0).getType(), max, mean, median, min, std);
	}

	public IndexCloseValueDto calculateSubPeriodQuotes(List<Index> indexes, String indexName, LocalDate periodStart,
			LocalDate periodEnd, String type, int quantity) {

		double startClose = 0;
		double endClose = 0;
		List<Double> listClose = new ArrayList<>();

		// Iterate over the indexes within the current period
		for (Index index : indexes) {

			// Formating double to 0.00
			BigDecimal bd = new BigDecimal(index.getClose()).setScale(2, RoundingMode.HALF_UP);

			double closeValue = bd.doubleValue();

			// Update startClose and endClose values
			if (startClose == 0) {
				startClose = closeValue;
			}
			endClose = closeValue;

			// Add close value to the list of close values
			listClose.add(closeValue);

		}
		// Reverse the list
		Collections.reverse(listClose);

		return new IndexCloseValueDto(periodStart, periodEnd, indexName, quantity + " " + type, periodStart, periodEnd,
				startClose, endClose, endClose - startClose, listClose);

	}

	public List<Double> calculateProfits(List<Index> firstIndex, List<Index> secondIndex) {
		List<Double> profits = new ArrayList<>();

		if (firstIndex.size() != secondIndex.size()) { // Assuming both lists have the same size
			throw new IllegalArgumentException();
		}

		int n = firstIndex.size();

		for (int i = 0; i < n; i++) {
			double profit = secondIndex.get(i).getClose() - secondIndex.get(i).getOpen()
					- (firstIndex.get(i).getClose() - firstIndex.get(i).getOpen());
			profits.add(profit);
		}

		return profits;
	}

	public List<Double> calculateAnnualYields(List<Index> firstIndex, List<Index> secondIndex) {
		List<Double> annualYields = new ArrayList<>();

		if (firstIndex.size() != secondIndex.size()) { // Assuming both lists have the same size
			throw new IllegalArgumentException();
		}

		int n = firstIndex.size();

		for (int i = 0; i < n; i++) {
			
			double yield = (
					
					(secondIndex.get(i).getClose() - secondIndex.get(i).getOpen())
					
					- (firstIndex.get(i).getClose() - firstIndex.get(i).getOpen()))
					
					/ firstIndex.get(i).getOpen()
					
					* 100.0;
			
			annualYields.add(yield);
		}

		return annualYields;

	}

	public double calculateCorrelationCoefficient(List<Index> firstIndex, List<Index> secondIndex) {
		if (firstIndex.size() != secondIndex.size()) { // Assuming both lists have the same size
			throw new DateOutOfRangeException();
		}
		int n = firstIndex.size();
		double sumXY = 0.0;
		double sumX = 0.0;
		double sumY = 0.0;
		double sumXSquare = 0.0;
		double sumYSquare = 0.0;

		for (int i = 0; i < n; i++) {
			double x = firstIndex.get(i).getClose() - firstIndex.get(i).getOpen();
			double y = secondIndex.get(i).getClose() - secondIndex.get(i).getOpen();

			sumXY += x * y;
			sumX += x;
			sumY += y;
			sumXSquare += x * x;
			sumYSquare += y * y;
		}

		double correlationCoefficient = (n * sumXY - sumX * sumY)
				/ Math.sqrt((n * sumXSquare - sumX * sumX) * (n * sumYSquare - sumY * sumY));

		return correlationCoefficient;
	}

	public String determineCorrelationDescription(double correlationCoefficient) {
		if (correlationCoefficient == 1.0) {
			return CorrelationDescriptionEnum.PERFECT_POSITIVE.getDescription();
		} else if (correlationCoefficient > 0.9) {
			return CorrelationDescriptionEnum.VERY_STRONG_POSITIVE.getDescription();
		} else if (correlationCoefficient > 0.7) {
			return CorrelationDescriptionEnum.STRONG_POSITIVE.getDescription();
		} else if (correlationCoefficient > 0.5) {
			return CorrelationDescriptionEnum.MODERATE_POSITIVE.getDescription();
		} else if (correlationCoefficient > 0.3) {
			return CorrelationDescriptionEnum.WEAK_POSITIVE.getDescription();
		} else if (correlationCoefficient > 0) {
			return CorrelationDescriptionEnum.NEGLIGIBLE_POSITIVE.getDescription();
		} else if (correlationCoefficient == -1.0) {
			return CorrelationDescriptionEnum.PERFECT_NEGATIVE.getDescription();
		} else if (correlationCoefficient < -0.9) {
			return CorrelationDescriptionEnum.VERY_STRONG_NEGATIVE.getDescription();
		} else if (correlationCoefficient < -0.7) {
			return CorrelationDescriptionEnum.STRONG_NEGATIVE.getDescription();
		} else if (correlationCoefficient < -0.5) {
			return CorrelationDescriptionEnum.MODERATE_NEGATIVE.getDescription();
		} else if (correlationCoefficient < -0.3) {
			return CorrelationDescriptionEnum.WEAK_NEGATIVE.getDescription();
		} else if (correlationCoefficient < 0) {
			return CorrelationDescriptionEnum.NEGLIGIBLE_NEGATIVE.getDescription();
		} else {
			return CorrelationDescriptionEnum.NO_CORRELATION.getDescription();
		}
	}

}