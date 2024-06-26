package fintechservice.accounting.service;

import fintechservice.accounting.dto.UserCreateDto;
import fintechservice.accounting.dto.UserDto;
import fintechservice.accounting.dto.UserRolesDto;
import fintechservice.accounting.dto.UserUpdateDto;

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
