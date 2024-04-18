package fintechservice.communication.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import fintechservice.communication.dao.CommunicationRepository;
import fintechservice.communication.dto.IndexCloseValueDto;
import fintechservice.communication.dto.IndexCorrelationRequestDto;
import fintechservice.communication.dto.IndexDto;
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
		if (Files.exists(Paths.get(path)) && Files.isRegularFile(Paths.get(path))) {
			try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

	@Override
	public List<IndexDto> getAllIndexes() {
		return communicationRepository.findAll().stream().map(i -> modelMapper.map(i, IndexDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public List<IndexHistoryResponseDto> getPeriodBetweenForIndex(IndexRequestDto indexRequestDto) {

		// TODO JSON parse "yyyy-M-dd" or "yyyy-MM-dd" !

		String indexName = indexRequestDto.getIndexs().get(0);
		LocalDate from = indexRequestDto.getFrom();
		LocalDate to = indexRequestDto.getTo();
		List<Index> indexes = communicationRepository.findByIndexBetween(indexName, from, to)
				.collect(Collectors.toList());

		double max = indexes.stream().mapToDouble(Index::getHigh).max().orElse(0);
		double mean = indexes.stream().mapToDouble(Index::getClose).average().orElse(0);
		double median = calculateMedian(indexes);
		double min = indexes.stream().mapToDouble(Index::getLow).min().orElse(0);
		double std = calculateStandardDeviation(indexes, mean);

		List<IndexHistoryResponseDto> response = new ArrayList<>();
		response.add(new IndexHistoryResponseDto(from, to, indexName, indexRequestDto.getType(), max, mean, median, min,
				std));
		return response;
	}

	@Override
	public Iterable<IndexCloseValueDto> getAllValueCloseBetween(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
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

	private double calculateMedian(List<Index> dataList) {
		List<Double> sortedClosePrices = dataList.stream().map(Index::getClose).sorted().collect(Collectors.toList());
		int size = sortedClosePrices.size();
		if (size % 2 == 0) {
			return (sortedClosePrices.get(size / 2 - 1) + sortedClosePrices.get(size / 2)) / 2;
		} else {
			return sortedClosePrices.get(size / 2);
		}
	}

	private double calculateStandardDeviation(List<Index> dataList, double mean) {
		double sumOfSquaredDifferences = dataList.stream().mapToDouble(row -> Math.pow(row.getClose() - mean, 2)).sum();
		return Math.sqrt(sumOfSquaredDifferences / dataList.size());
	}

}
