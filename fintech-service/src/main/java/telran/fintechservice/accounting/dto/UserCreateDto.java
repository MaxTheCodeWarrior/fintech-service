package telran.fintechservice.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserCreateDto {

	String login;
	String password;
	String firstName;
	String lastName;

}
