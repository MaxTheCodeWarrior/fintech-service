package fintechservice.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class IndexCorrelationRequestDto {
	

	// stock indexes
	private List<String> indexs;
	
	// start date of history
	private LocalDate from;
	
	// end date of history
	private LocalDate to;

}
