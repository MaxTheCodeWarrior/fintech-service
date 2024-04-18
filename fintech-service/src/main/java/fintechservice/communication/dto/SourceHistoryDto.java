package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceHistoryDto {

	String source;
	LocalDate fromData;
	LocalDate toData;
}
