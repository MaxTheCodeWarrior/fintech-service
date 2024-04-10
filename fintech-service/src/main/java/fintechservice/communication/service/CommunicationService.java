package fintechservice.communication.service;

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

public interface CommunicationService {

	boolean addHistoryWithFile(String fileName);

	Iterable<SourceResponseDto> parserForYahooFinance(SourceRequestDto sourceRequestDto);

	Iterable<SourceLinkDto> addNewIndexes(SourceLinksRequestDto sourceLinksRequestDto);
	
	

	SourceHistoryDto getTimeHistoryForIndex(String index);

	Iterable<String> getAllIndexes();

	
	Iterable<IndexHistoryResponseDto> getPeriodBetweenForIndex(IndexRequestDto indexRequestDto);

	Iterable<IndexCloseValueDto> getAllValueCloseBetween(IndexRequestDto indexRequestDto);

	
	
	IndexHistoryResponseDto calcSumPackage(IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto); 

	IndexIncomeApyResponseDto calcIncomeWithApy(IndexRequestDto indexRequestDto);
	
	Iterable<IndexIncomeApyAllDateDto> calcIncomeWithApyAllDate(IndexRequestDto indexRequestDto);
	
	Iterable<IndexIncomeIrrResponseDto> calcIncomeWithIrr(IndexRequestDto indexRequestDto);
	
	String calcCorrelation (IndexCorrelationRequestDto indexCorrelationRequestDto);
	
	
	
	boolean deleteAllHistoryForCompany();
	
	double prediction();
	
	
}
