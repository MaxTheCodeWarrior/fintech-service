package fintechservice.accounting.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fintechservice.accounting.dao.AccountingRepository;
import fintechservice.accounting.dto.UserCreateDto;
import fintechservice.accounting.dto.UserDto;
import fintechservice.accounting.dto.UserLoginDto;
import fintechservice.accounting.dto.UserRoleEnum;
import fintechservice.accounting.dto.UserRolesDto;
import fintechservice.accounting.dto.UserUpdateDto;
import fintechservice.accounting.model.User;
import fintechservice.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService, CommandLineRunner {
	final AccountingRepository accountingRepository;
	final PasswordEncoder passwordEncoder;

	final ModelMapper modelMapper;

	@Override
	public UserDto registerUser(UserCreateDto userCreateDto) {
		if (accountingRepository.existsById(userCreateDto.getLogin())) {
			return null;
		}
		User user = modelMapper.map(userCreateDto, User.class);
		String password = passwordEncoder.encode(userCreateDto.getPassword());
		user.setPassword(password);
		user.getRoles().add(UserRoleEnum.USER);
		accountingRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public boolean loginUser(UserLoginDto userLoginDto, HttpServletRequest request) {
		String login = userLoginDto.getLogin();
		String password = userLoginDto.getPassword();
		// Authenticate the user
		Optional<User> optionalUser = accountingRepository.findById(login)
				.filter(user -> passwordEncoder.matches(password, user.getPassword()));
		if (optionalUser.isPresent()) {
//			// If authentication is successful, store the user in the session
//			User authenticatedUser = optionalUser.get();
//			customWebSecurity.loginUser(request, authenticatedUser);
			return true;
		}
		return false;

	}

	/* @formatter:off */
	/*	The purpose of this method is to send a recovery password link to the email 
	 * address provided in the URL.
	 * The link is used to update the password associated with the account.
	 * The example provided shows how to send the request using the cURL command
	 * line tool. Note that the "--data ''" parameter is an empty data field,
	 * which is required by cURL but not by the server. Additionally,
	 * the "{{baseUrl}}" placeholder in the URL should be replaced with the
	 * actual URL of the application.
	 */
	/* @formatter:on */

	// TODO Login method!

	@Override
	public void recoveryPasswordByLink(String email) {

		// TODO for this method needs authenticated email session

	}

	/* @formatter:off */
	/*	This method is used to send a request to recover a password.
	 * The request must include an X-Password header
	 * with a value of the user's password.
	 * The application will return a response
	 * with instructions for resetting the password.
	 */
	/* @formatter:on */

	@Override
	public boolean recoveryPassword(String xPassword, String login) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return passwordEncoder.matches(xPassword, user.getPassword());

	}

	@Override
	public UserDto deleteUser(String login) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		accountingRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUppdateDto) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.setFirstName(userUppdateDto.getFirstName());
		user.setLastName(userUppdateDto.getLastName());
		accountingRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserRolesDto addUserRole(String login, String role) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(UserRoleEnum.valueOf(role.toUpperCase()));
		accountingRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public UserRolesDto deleteUserRole(String login, String role) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().remove(UserRoleEnum.valueOf(role.toUpperCase()));
		accountingRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public void changeUserPassword(String login, String newPassword) {
		User user = accountingRepository.findById(login).orElseThrow(UserNotFoundException::new);
		String password = passwordEncoder.encode(newPassword);
		user.setPassword(password);
		accountingRepository.save(user);
	}

	/* @formatter:off */
	
	//CommandLineRunner for creating admin instance
	@Override
	public void run(String... args) throws Exception {
		if(!accountingRepository.existsById("admin")) {
			User user = new User();
				user.setLogin("admin");
					user.setFirstName("");
						user.setLastName("");
							user.setPassword(passwordEncoder.encode("admin"));
								user.getRoles().add(UserRoleEnum.MODERATOR);
									user.getRoles().add(UserRoleEnum.ADMINISTRATOR);
										accountingRepository.save(user);
		}
		
	}

	



}
