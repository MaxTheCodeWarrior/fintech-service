package telran.fintechservice.accounting.service;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.fintechservice.accounting.dao.AccountingRepository;
import telran.fintechservice.accounting.dto.UserCreateDto;
import telran.fintechservice.accounting.dto.UserDto;
import telran.fintechservice.accounting.dto.UserRoleEnum;
import telran.fintechservice.accounting.dto.UserRolesDto;
import telran.fintechservice.accounting.dto.UserUpdateDto;
import telran.fintechservice.accounting.model.User;
import telran.fintechservice.exceptions.UserExistsException;
import telran.fintechservice.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService, CommandLineRunner {
	final AccountingRepository accountRepository;
	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;

	@Override
	public UserDto registerUser(UserCreateDto userCreateDto) {
		if (accountRepository.existsById(userCreateDto.getLogin())) {
			throw new UserExistsException();
		}
		User user = modelMapper.map(userCreateDto, User.class);
		String password = passwordEncoder.encode(userCreateDto.getPassword());
		user.setPassword(password);
		user.getRoles().add(UserRoleEnum.USER);
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
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
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return passwordEncoder.matches(xPassword, user.getPassword());

	}

	@Override
	public UserDto deleteUser(String login) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		accountRepository.delete(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(String login, UserUpdateDto userUppdateDto) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.setFirstName(userUppdateDto.getFirstName());
		user.setLastName(userUppdateDto.getLastName());
		accountRepository.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserRolesDto addUserRole(String login, String role) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().add(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public UserRolesDto deleteUserRole(String login, String role) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		user.getRoles().remove(UserRoleEnum.valueOf(role.toUpperCase()));
		accountRepository.save(user);
		return modelMapper.map(user, UserRolesDto.class);
	}

	@Override
	public void changeUserPassword(String login, String newPassword) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		String password = passwordEncoder.encode(newPassword);
		user.setPassword(password);
		accountRepository.save(user);
	}

	@Override
	public UserDto getUser(String login) {
		User user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(user, UserDto.class);
	}

	/* @formatter:off */
	
	//CommandLineRunner for creating admin instance
	@Override
	public void run(String... args) throws Exception {
		if(!accountRepository.existsById("admin")) {
			User user = new User();
				user.setLogin("admin");
					user.setFirstName("");
						user.setLastName("");
							user.setPassword(passwordEncoder.encode("admin"));
								user.getRoles().add(UserRoleEnum.MODERATOR);
									user.getRoles().add(UserRoleEnum.ADMINISTRATOR);
										accountRepository.save(user);
		}
		
	}




}
