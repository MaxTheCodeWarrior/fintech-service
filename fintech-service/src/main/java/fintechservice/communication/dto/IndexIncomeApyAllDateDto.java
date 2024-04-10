package fintechservice.communication.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexIncomeApyAllDateDto {

	// The company index
    private String source;
    
    // Start date of the historical data
    private LocalDate historyFrom;
    
    // End date of the historical data
    private LocalDate historyTo;
    
    // The type and length of the entire historical period
    private String type;
    
    // Start date of the sub-period
    private LocalDate from;
    
    // End date of the sub-period
    private LocalDate to;
    
    // Purchase price of the stock
    private double purchaseAmount;
    
    // Sale price of the stock
    private double saleAmount;
    
    // Profit from selling the stock
    private double income;
    
    // Annual yield (APY) in this sub-period
    private double apy;
    
}
