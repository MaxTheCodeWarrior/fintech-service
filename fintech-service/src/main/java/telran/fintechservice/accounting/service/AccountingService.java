package telran.fintechservice.accounting.service;

import telran.fintechservice.accounting.dto.UserCreateDto;
import telran.fintechservice.accounting.dto.UserDto;
import telran.fintechservice.accounting.dto.UserRolesDto;
import telran.fintechservice.accounting.dto.UserUpdateDto;

public interface AccountingService {

	UserDto registerUser(UserCreateDto userCreateDto);

	UserDto getUser(String login);

	void recoveryPasswordByLink(String url);

	boolean recoveryPassword(String xPassword, String login);

	UserDto deleteUser(String login);

	UserDto updateUser(String login, UserUpdateDto userUppdateDto);

	UserRolesDto addUserRole(String login, String role);

	UserRolesDto deleteUserRole(String login, String role);

	void changeUserPassword(String login, String newPassword);

}
