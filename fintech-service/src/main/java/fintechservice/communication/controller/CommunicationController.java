package fintechservice.communication.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
import fintechservice.communication.service.CommunicationService;
import lombok.RequiredArgsConstructor;

/* @formatter:off */

@Controller
@RequiredArgsConstructor
@RequestMapping("/communication")
public class CommunicationController {

	final CommunicationService communicationService;
	final ModelMapper modelMapper;

	@PostMapping("/index/{index}/{path}")
	public ResponseEntity<Boolean> addHistoryWithFile(@PathVariable String index, @PathVariable String path) {
		boolean check = communicationService.addHistoryWithFile(index, path);
		return ResponseEntity.status(HttpStatus.OK).body(check);
	}


	/**
	 * @param 
	 * This is a parser that works with the website https://finance.yahoo.com/. 
	 * To use this method, you must first add the data to the database using the "New Index For Parser" method.
	 * It works through a GET request. It works with both a single index and an array of indexes.
	 */
	
	@PostMapping("/parser")
	public ResponseEntity<Iterable<SourceResponseDto>> parserForYahooFinance(SourceRequestDto sourceRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @param  
	 * The method is necessary for downloading data from the https://finance.yahoo.com/ website using the parser.
	 * For proper operation, write the index of the company that will be used in further work.
	 * The second variable must specify the https address of the page selected by the company in the Historical Data section.
	 */
	
	@PostMapping("/parser/addindex")
	public ResponseEntity<Iterable<SourceLinkDto>> addNewIndexes(SourceLinksRequestDto sourceLinksRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}


	/** 
	 * @param 
	 * This method returns the historical boundary values for the specified index.
	 * {{index}} - The company index used in the application. 
	 * To display all indexes, use the Get All Indexes request.
	 */
	

	@GetMapping("/index/{index}")
	public ResponseEntity<SourceHistoryDto> getTimeHistoryForIndex(String index) {
		// TODO Auto-generated method stub
		return null;
	}

	// Returns all indexes that can be used in the application.
	@GetMapping("/index")
	public ResponseEntity<Iterable<String>> getAllIndexes() {
		communicationService.getAllIndexes();
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	
	/** 
	 * @param 
	 * Returns statistical data on a stock for a specified index over a specified period of time. 
	 * To do this, specify the type, possible options are: days, weeks, months, decades, years, centuries.
	 * Quantity - specify the length of the type.
	 */
	

	@PostMapping("/index")
	public ResponseEntity<Iterable<IndexHistoryResponseDto>> getPeriodBetweenForIndex(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param 
	 * Returns all quotes on all sub-periods for a specified index over a specified period of time. 
	 * To do this, specify the type, possible options are: days, weeks, months, decades, years, centuries. 
	 * Quantity - specify the number of type.
	 */
	
	@PostMapping("/data")
	public ResponseEntity<Iterable<IndexCloseValueDto>> getAllValueCloseBetween(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param 
	 * Calculates the sum of quotes in a quantity that can be specified for each stock.
	 * Outputs statistical indicators for a package of stocks. 
	 */
	
	@PostMapping("/index/sum")
	public ResponseEntity<IndexHistoryResponseDto> calcSumPackage(
			IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param 
	 * Calculates the profit and annual yield for stocks.
	 * Displays the minimum and maximum yield with a time sub-period.
	 */
	
	@PostMapping("/index/apy")
	public ResponseEntity<IndexIncomeApyResponseDto> calcIncomeWithApy(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param 
	 * Calculates the profit and annual yield for stocks.
	 * Displays all yields with a time sub-period.
	 */
	
	@PostMapping("index/apy_all")
	public ResponseEntity<Iterable<IndexIncomeApyAllDateDto>> calcIncomeWithApyAllDate(
			IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * @param 
	 * Calculates the profit and internal rate of return (IRR) for stocks.
	 * Displays the minimum and maximum IRR with a sub-period of time.
	 */
	
	@PostMapping("/indexIrr")
	public ResponseEntity<Iterable<IndexIncomeIrrResponseDto>> calcIncomeWithIrr(IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param 
	 * Returns information about the correlation between stocks.
	 * Calculates the profit and annual yield for stocks.
	 * Displays the minimum and maximum yield with a sub-period of time.
	 */
	
	@PostMapping("/index//correlation")
	public ResponseEntity<String> calcCorrelation(IndexCorrelationRequestDto indexCorrelationRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @param 
	 * Deletes the specified stock by index from the application.
	 */
	
	@DeleteMapping("/index/{index}")
	public ResponseEntity<Boolean> deleteAllHistoryForCompany(String index) {
		// TODO Auto-generated method stub
		return null;
	}

	@GetMapping("/index/prediction/{index}")
	public ResponseEntity<Double> prediction() {
		// TODO Auto-generated method stub
		return null;
	}

}
