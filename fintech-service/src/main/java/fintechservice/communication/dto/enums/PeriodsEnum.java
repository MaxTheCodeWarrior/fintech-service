package fintechservice.communication.dto.enums;
/* @formatter:off */

//  "type": type of analytical period (may contain quantity: for example "1d")
public enum PeriodsEnum {
	
	
//  only for methods without "quantity" field (length of analytical period) 
	ONEDAY("1d"),
	ONEWEEK("1wk"),
	ONEMONTH("1mp"),
	
//	only for methods with "quantity" field (length of analytical period) 
	DAY("day"),
	DAYS("days"),
	WEEKS("weeks"),
	MOUTHS("months"),
	YEARS("years"),
	DEACDES("decades"),
	CENTURIES("centuries");

	private final String value;

	PeriodsEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
