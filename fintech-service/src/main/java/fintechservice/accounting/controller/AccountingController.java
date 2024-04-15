package fintechservice.accounting.controller;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import fintechservice.accounting.dto.RecoveryInstructionsDto;
import fintechservice.accounting.dto.UserCreateDto;
import fintechservice.accounting.dto.UserDto;
import fintechservice.accounting.dto.UserLoginDto;
import fintechservice.accounting.dto.UserRolesDto;
import fintechservice.accounting.dto.UserUpdateDto;
import fintechservice.accounting.service.AccountingService;
import fintechservice.security.CustomWebSecurity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountingController {

	final AccountingService accountingService;
	final ModelMapper modelMapper;
	final CustomWebSecurity customWebSecurity;

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserCreateDto userCreateDto) {
		UserDto userDto = accountingService.registerUser(userCreateDto);
		return userDto != null ? ResponseEntity.status(HttpStatus.OK).body(userDto)
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> loginUser(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
		// TODO check if already logged in
		return accountingService.loginUser(userLoginDto, request) ? ResponseEntity.status(HttpStatus.OK).build()
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@DeleteMapping("/user/{user}")
	public ResponseEntity<UserDto> deleteUser(@PathVariable String user) {
		UserDto userDto = accountingService.deleteUser(user);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}

	@PutMapping("/user/{user}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String user, @RequestBody UserUpdateDto userUpdateDto) {
		UserDto userDto = accountingService.updateUser(user, userUpdateDto);
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
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches() ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
				: ResponseEntity.status(HttpStatus.CONFLICT).build();
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
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new RecoveryInstructionsDto(principal.getName()));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@DeleteMapping("/user/{user}/role/{role}")
	public ResponseEntity<UserRolesDto> deleteUserRole(@PathVariable String user, @PathVariable String role) {
		UserRolesDto userRolesDto = accountingService.deleteUserRole(user, role);
		return ResponseEntity.status(HttpStatus.OK).body(userRolesDto);
	}

	// TODO check this filter
	@PutMapping("/user/password") // X-Password variable from header not the body!
	public ResponseEntity<Void> changeUserPassword(Principal principal,
			@RequestHeader("X-Password") String newPassword) {
		accountingService.changeUserPassword(principal.getName(), newPassword);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

}
