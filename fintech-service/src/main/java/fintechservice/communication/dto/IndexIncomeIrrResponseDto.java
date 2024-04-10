package fintechservice.communication.dto;


import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexIncomeIrrResponseDto {

	// Start date of the historical data
	private LocalDate from;

	// End date of the historical data
	private LocalDate to;

	// The company index for which the data is provided
	private String source;

	// The type and length of the analytical period (e.g., "5 days", "monthly",
	// etc.)
	private String type;

	// Designation of the sub-period with the lowest annual return
	private SubPeriodDto minIncome;

	// Designation of the sub-period with the highest annual return
	private SubPeriodDto maxIncome;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class SubPeriodDto {
		// Date of stock purchase
		private LocalDate dateOfPurchase;

		// Purchase price of stock
		private double purchaseAmount;

		// Date of stock sale
		private LocalDate dateOfSale;

		// Sale price of stock
		private double saleAmount;

		// Profit from stock sale
		private double income;

		// Internal rate of return in this sub-period
		private double irr;
	}

}
