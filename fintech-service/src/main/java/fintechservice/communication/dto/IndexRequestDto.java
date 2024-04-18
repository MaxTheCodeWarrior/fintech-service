package fintechservice.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IndexRequestDto {


	// stock indexes
	private List<String> indexs;

	// type of analytical period
	private String type;

	// length of analytical period
	private int quantity;

	// start date of history
	private LocalDate from;

	// end date of history
	private LocalDate to;
}
