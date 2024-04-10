package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SourceRequestDto {

	Iterable<String> source;
	LocalDate fromData;
	LocalDate toData;
	/**
	 * value from @PeriodsEnum only
	 */
	String type;

}
