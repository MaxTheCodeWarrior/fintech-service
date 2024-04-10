package fintechservice.accounting.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fintechservice.accounting.dto.UserRoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 5132458793756807103L;

	@Id
	String login;

	private String password;

	String firstName;

	String lastName;

	Set<UserRoleEnum> roles = new HashSet<>();
}
