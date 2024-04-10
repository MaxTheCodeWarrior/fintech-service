package fintechservice.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndexIncomeApyResponseDto {
    // Start date of the historical data
    private LocalDate from;
    
    // End date of the historical data
    private LocalDate to;
    
    // The company index for which the data is provided
    private List<String> source;
    
    // The type and length of the analytical period (e.g., "5 days", "monthly", etc.)
    private String type;
    
    // Sub-period designation with the minimum annual yield
    private SubPeriodDto minIncome;
    
    // Sub-period designation with the maximum annual yield
    private SubPeriodDto maxIncome;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SubPeriodDto {
        // The date of purchase of the quote
        private LocalDate dateOfPurchase;
        
        // The purchase price of the quote
        private double purchaseAmount;
        
        // The date of sale of the quote
        private LocalDate dateOfSale;
        
        // The sale price of the quote
        private double saleAmount;
        
        // The profit from selling the quote
        private double income;
        
        // The annual yield in this sub-period
        private double apy;
    }
}
