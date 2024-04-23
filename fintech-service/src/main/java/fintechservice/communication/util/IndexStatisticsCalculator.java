package fintechservice.communication.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fintechservice.communication.dto.IndexCloseValueDto;
import fintechservice.communication.dto.IndexHistoryResponseDto;
import fintechservice.communication.model.Index;
import fintechservice.exceptions.DateOutOfRangeException;

public class IndexStatisticsCalculator {

	public static LocalDate calculatePeriodEnd(LocalDate periodStart, String type, int quantity, LocalDate endDate) {
		switch (type) {
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

	public static IndexHistoryResponseDto calculateStatisticsForPeriod(List<Index> indexes, String indexName,
			LocalDate periodStart, LocalDate periodEnd, String type, int quantity) {
		double max = indexes.stream().mapToDouble(Index::getHigh).max().orElse(0);
		double mean = indexes.stream().mapToDouble(Index::getClose).average().orElse(0);
		double median = calculateMedian(
				indexes.stream().mapToDouble(Index::getClose).boxed().collect(Collectors.toList()));
		double min = indexes.stream().mapToDouble(Index::getLow).min().orElse(0);
		double std = calculateStandardDeviation(
				indexes.stream().mapToDouble(Index::getClose).boxed().collect(Collectors.toList()), mean);

		return new IndexHistoryResponseDto(periodStart, periodEnd, indexName, quantity + " " + type, max, mean, median,
				min, std);
	}

	public static double calculateMedian(List<Double> sortedClosePrices) {
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

	public static double calculateStandardDeviation(List<Double> dataList, double mean) {
		double sumOfSquaredDifferences = dataList.stream().mapToDouble(value -> Math.pow(value - mean, 2)).sum();
		int count = dataList.size();
		return Math.sqrt(sumOfSquaredDifferences / count);
	}

	public static IndexHistoryResponseDto aggregateStatistics(String indexName,
			List<IndexHistoryResponseDto> periodStatisticsList) {
		double max = periodStatisticsList.stream().mapToDouble(IndexHistoryResponseDto::getMax).max().orElse(0);
		double mean = periodStatisticsList.stream().mapToDouble(IndexHistoryResponseDto::getMean).average().orElse(0);
		double median = calculateMedian(periodStatisticsList.stream().mapToDouble(IndexHistoryResponseDto::getMedian)
				.boxed().collect(Collectors.toList()));
		double min = periodStatisticsList.stream().mapToDouble(IndexHistoryResponseDto::getMin).min().orElse(0);
		double std = calculateStandardDeviation(periodStatisticsList.stream()
				.mapToDouble(IndexHistoryResponseDto::getStd).boxed().collect(Collectors.toList()), mean);
		return new IndexHistoryResponseDto(periodStatisticsList.get(0).getFrom(), periodStatisticsList.get(0).getTo(),
				indexName, periodStatisticsList.get(0).getType(), max, mean, median, min, std);
	}

	public static IndexCloseValueDto calculateSubPeriodQuotes(List<Index> indexes, String indexName,
			LocalDate periodStart, LocalDate periodEnd, String type, int quantity) {
		// Initialize variables to store sub-period quotes
		double startClose = Double.MAX_VALUE;
		double endClose = Double.MIN_VALUE;
		List<Double> listClose = new ArrayList<>();

		// Iterate over the indexes within the current period
		for (Index index : indexes) {

			double closeValue = index.getClose();

			// Update startClose and endClose values
			startClose = Math.min(startClose, closeValue);
			endClose = Math.max(endClose, closeValue);

			// Add close value to the list of close values
			listClose.add(closeValue);
		}

		return new IndexCloseValueDto(periodStart, periodEnd, indexName, quantity + " " + type, periodStart, periodEnd,
				startClose, endClose, endClose - startClose, listClose);

	}

}