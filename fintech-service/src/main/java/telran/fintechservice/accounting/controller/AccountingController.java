package telran.fintechservice.accounting.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import telran.fintechservice.accounting.dto.RecoveryInstructionsDto;
import telran.fintechservice.accounting.dto.UserCreateDto;
import telran.fintechservice.accounting.dto.UserDto;
import telran.fintechservice.accounting.dto.UserRolesDto;
import telran.fintechservice.accounting.dto.UserUpdateDto;
import telran.fintechservice.accounting.service.AccountingService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountingController {

	final AccountingService accountingService;
	final ModelMapper modelMapper;

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserCreateDto userCreateDto) {
		UserDto userDto = accountingService.registerUser(userCreateDto);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> loginUser(Principal principal) {
		return ResponseEntity.status(HttpStatus.OK).body(accountingService.getUser(principal.getName()));
	}

	@DeleteMapping("/user/{user}")
	public ResponseEntity<UserDto> deleteUser(@PathVariable String user) {
		UserDto userDto = accountingService.deleteUser(user);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}

	@PutMapping("/user/{user}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String user, @RequestBody UserUpdateDto userUppdateDto) {
		UserDto userDto = accountingService.updateUser(user, userUppdateDto);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}

	@PutMapping("/user/{user}/role/{role}")
	public ResponseEntity<UserRolesDto> addUserRole(@PathVariable String user, @PathVariable String role) {
		UserRolesDto userRolesDto = accountingService.addUserRole(user, role);
		return ResponseEntity.status(HttpStatus.OK).body(userRolesDto);

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

	@GetMapping("/recovery/{email}")
	public ResponseEntity<Void> recoveryPasswordByLink(@PathVariable String email) {

		// TODO for this method needs authenticated email session

		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/* @formatter:off */
	/*	This method is used to send a request to recover a password.
	 * The request must include an X-Password header
	 * with a value of the user's password.
	 * The application will return a response
	 * with instructions for resetting the password.
	 */
	/* @formatter:on */

	@PostMapping("/recovery/-")
	public ResponseEntity<RecoveryInstructionsDto> recoveryPassword(@RequestHeader("X-Password") String password,
			Principal principal) {
		if (accountingService.recoveryPassword(password, principal.getName())) {
			return ResponseEntity.status(HttpStatus.OK).body(new RecoveryInstructionsDto(principal.getName()));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@DeleteMapping("/user/{user}/role/{role}")
	public ResponseEntity<UserRolesDto> deleteUserRole(@PathVariable String user, @PathVariable String role) {
		UserRolesDto userRolesDto = accountingService.deleteUserRole(user, role);
		return ResponseEntity.status(HttpStatus.OK).body(userRolesDto);
	}

	@PutMapping("/password")
	public ResponseEntity<Void> changeUserPassword(Principal principal,
			@RequestHeader("X-Password") String newPassword) {
		accountingService.changeUserPassword(principal.getName(), newPassword);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	@GetMapping("/user/{user}")
	public ResponseEntity<UserDto> getUser(@PathVariable String user) {
		UserDto userDto = modelMapper.map(accountingService.getUser(user), UserDto.class);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);

	}

}
