package telran.fintechservice.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.fintechservice.accounting.dto.UserRoleEnum;

@Getter
@NoArgsConstructor
@Setter
public class User {

	@Id
	String login;

	private String password;

	String firstName;

	String lastName;

	Set<UserRoleEnum> roles = new HashSet<>();
}
