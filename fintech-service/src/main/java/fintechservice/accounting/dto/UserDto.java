package fintechservice.accounting.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

	String login;
	String firstName;
	String lastName;
	Set<String> roles = new HashSet<>();
}
