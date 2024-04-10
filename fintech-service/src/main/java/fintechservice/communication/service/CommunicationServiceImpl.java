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

public class CommunicationServiceImpl implements CommunicationService {

	@Override
	public boolean addHistoryWithFile(String fileName) {
		// TODO Auto-generated method stub
		return false;
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
	public SourceHistoryDto getTimeHistoryForIndex(String index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> getAllIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<IndexHistoryResponseDto> getPeriodBetweenForIndex(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
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

}
