package fintechservice.security;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Service;

import fintechservice.accounting.dao.AccountingRepository;
import fintechservice.accounting.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomWebSecurity {

	final AccountingRepository accountingRepository;
	final PasswordEncoder passwordEncoder;

	public AuthorizationDecision checkLogin(Supplier<Authentication> a, RequestAuthorizationContext o) {
		String login = o.getVariables().get("login");
		String principalName = a.get().getName();
		return new AuthorizationDecision(login.equals(principalName));

	}

	public Optional<User> authenticateUser(String login, String password) {
		return accountingRepository.findById(login)
				.filter(user -> passwordEncoder.matches(password, user.getPassword()));
	}

	public void loginUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession(true);
		session.setAttribute("user", user);
	}

	public void logoutUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	public AuthorizationDecision customCheckBiFunction(Supplier<Authentication> a, RequestAuthorizationContext o,
			BiFunction<Supplier<Authentication>, RequestAuthorizationContext, AuthorizationDecision> foo) {
		return foo.apply(a, o);
	}

	/*
	 * write your custom check methods below or implement
	 * AuthorizationManager<RequestAuthorizationContext> with default method check
	 */

}
