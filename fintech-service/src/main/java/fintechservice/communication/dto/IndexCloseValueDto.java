package fintechservice.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexCloseValueDto {

	// start date of history
	private LocalDate from;

	// end date of history
	private LocalDate to;

	// the company index
	private String source;

	// the type and length of analytical period
	private String type;

	// the start date of the sub-period
	private LocalDate minDate;

	// the end date of the sub-period
	private LocalDate maxDate;

	// purchase price of the quote
	private double startClose;

	// selling price of the quote
	private double endClose;

	// profit after selling the quote
	private double valueClose;

	// quote prices for the entire sub-period of this stock
	private List<Double> listClose;
}
