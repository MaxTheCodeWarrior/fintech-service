package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SourceResponseDto {

	private UploadInfoIdDto uploadInfoId;
	private double close;
	private int volume;
	private double open;
	private double high;
	private double low;
	
	@Getter
	@Setter
	@NoArgsConstructor
	public class UploadInfoIdDto {

		private LocalDate date;
		private String source;

	}


}
