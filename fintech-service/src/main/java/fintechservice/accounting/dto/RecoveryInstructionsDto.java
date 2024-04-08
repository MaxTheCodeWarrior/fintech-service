package fintechservice.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RecoveryInstructionsDto {

	public RecoveryInstructionsDto(String userName) {
		this.userName = userName;
	}

	private String userName;

	String text = "Dear, " + userName + " for password recovery please folow to your `Account section`";

}
