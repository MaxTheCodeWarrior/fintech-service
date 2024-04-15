package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexDto {

	private String index;
	private LocalDate date;
	private double open;
	private double high;
	private double low;
	private double close;
	private double adjClose;
	private int volume;

}
