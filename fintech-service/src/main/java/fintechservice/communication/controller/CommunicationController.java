package fintechservice.communication.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
import fintechservice.communication.service.CommunicationService;
import lombok.RequiredArgsConstructor;

/* @formatter:off */

@Controller
@RequiredArgsConstructor
@RequestMapping("/communication")  // ATTENTION : Some methods differ from the given API !
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
	public ResponseEntity<Iterable<SourceResponseDto>> parserForYahooFinance(@RequestBody SourceRequestDto sourceRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * @param  
	 * The method is necessary for downloading data from the https://finance.yahoo.com/ website using the parser.
	 * For proper operation, write the index of the company that will be used in further work.
	 * The second variable must specify the https address of the page selected by the company in the Historical Data section.
	 */
	
	@PostMapping("/parser/addindex")
	public ResponseEntity<Iterable<SourceLinkDto>> addNewIndexes(@RequestBody SourceLinksRequestDto sourceLinksRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/** 
	 * @param 
	 * This method returns the historical boundary values for the specified index.
	 * {{index}} - The company index used in the application. 
	 * To display all indexes, use the Get All Indexes request.
	 */
	
	@GetMapping("/index/{index}")
	public ResponseEntity<SourceHistoryDto> getTimeHistoryForIndex(@PathVariable String index) {
		return ResponseEntity.status(HttpStatus.OK).body(communicationService.getTimeHistoryForIndex(index));
	}

	// Returns all indexes that can be used in the application.
	@GetMapping("/index")
	public ResponseEntity<List<IndexDto>> getAllIndexes() {
		return ResponseEntity.status(HttpStatus.OK).body(communicationService.getAllIndexes());
	}

	/** 
	 * @param 
	 * Returns statistical data on a stock for a specified index over a specified period of time. 
	 * To do this, specify the type, possible options are: days, weeks, months, decades, years, centuries.
	 * Quantity - specify the length of the type.
	 */
	
	@PostMapping("/index")
	public ResponseEntity<List<IndexHistoryResponseDto>> getPeriodBetweenForIndex(@RequestBody IndexRequestDto indexRequestDto) {
		return ResponseEntity.status(HttpStatus.OK).body(communicationService.getPeriodBetweenForIndex(indexRequestDto));
	}

	/**
	 * @param 
	 * Returns all quotes on all sub-periods for a specified index over a specified period of time. 
	 * To do this, specify the type, possible options are: days, weeks, months, decades, years, centuries. 
	 * Quantity - specify the number of type.
	 */
	
	@PostMapping("/data")
	public ResponseEntity<Iterable<IndexCloseValueDto>> getAllValueCloseBetween(@RequestBody IndexRequestDto indexRequestDto) {
		return ResponseEntity.status(HttpStatus.OK).body(communicationService.getAllValueCloseBetween(indexRequestDto));
	}

	/**
	 * @param 
	 * Calculates the sum of quotes in a quantity that can be specified for each stock.
	 * Outputs statistical indicators for a package of stocks. 
	 */
	
	@PostMapping("/index/sum")
	public ResponseEntity<IndexHistoryResponseDto> calcSumPackage(@RequestBody IndexRequestWithAmountFieldDto indexRequestWithAmountFieldDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
	
	/**
	 * @param 
	 * Calculates the profit and annual yield for stocks.
	 * Displays the minimum and maximum yield with a time sub-period.
	 */
	
	@PostMapping("/index/apy")
	public ResponseEntity<IndexIncomeApyResponseDto> calcIncomeWithApy(@RequestBody IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * @param 
	 * Calculates the profit and annual yield for stocks.
	 * Displays all yields with a time sub-period.
	 */
	
	@PostMapping("index/apy_all")
	public ResponseEntity<Iterable<IndexIncomeApyAllDateDto>> calcIncomeWithApyAllDate(@RequestBody
			IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * @param 
	 * Calculates the profit and internal rate of return (IRR) for stocks.
	 * Displays the minimum and maximum IRR with a sub-period of time.
	 */
	
	@PostMapping("/indexIrr")
	public ResponseEntity<Iterable<IndexIncomeIrrResponseDto>> calcIncomeWithIrr(@RequestBody IndexRequestDto indexRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * @param 
	 * Returns information about the correlation between stocks.
	 * Calculates the profit and annual yield for stocks.
	 * Displays the minimum and maximum yield with a sub-period of time.
	 */
	
	@PostMapping("/index/correlation")
	public ResponseEntity<String> calcCorrelation(@RequestBody IndexCorrelationRequestDto indexCorrelationRequestDto) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
	
	/**
	 * @param 
	 * Deletes the specified stock by index from the application.
	 */
	
	@DeleteMapping("/index/{index}")
	public ResponseEntity<Boolean> deleteAllHistoryForCompany(@PathVariable String index) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@GetMapping("/index/prediction/{index}")
	public ResponseEntity<Double> prediction(@PathVariable String index) {
		// TODO Auto-generated method stub
		return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

}
