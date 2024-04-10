package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexHistoryResponseDto {

	// Start date of the historical data
	private LocalDate from;

	// End date of the historical data
	private LocalDate to;

	// The company index for which the data is provided
	private String source;

	// The type and length of the analytical period (e.g., "5 days", "monthly", etc.)
	private String type;

	// The maximum profit during the specified period
	private double max;

	// The average profit during the specified period
	private double mean;

	// The median profit during the specified period
	private double median;

	// The minimum profit during the specified period
	private double min;

	// The standard deviation of profit during the specified period
	private double std;
}
