package fintechservice.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexRequestWithAmountFieldDto {

	// stock indexes
	private List<String> indexs;

	private List<Integer> amount;
	
	// start date of history
	private LocalDate from;

	// end date of history
	private LocalDate to;

	// type of analytical period
	private String type;

	// length of analytical period
	private int quantity;

}
