package fintechservice.communication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CorrelationCoefficientDto {

	private double correlationCoefficient;
	private String correlationDescription;
	private double minProfit;
	private double maxProfit;
	private double minAnnualYield;
	private double maxAnnualYield;

}
