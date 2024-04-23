package fintechservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fintechservice.accounting.dto.UserRoleEnum;
import lombok.RequiredArgsConstructor;

//@formatter:off

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {

	final CustomWebSecurity customWebSecurity;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable()); //Enables CSRF protection
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); //Enable COOKIES
		http.authorizeHttpRequests(authoraize -> authoraize

	
				/**					
				 * User Management
				 */
	
				// POST {{baseUrl}}/register - for user registration. access to H2.
				.requestMatchers("/account/register", "/h2-console/**", "/account/user/password")
				.permitAll()

				// TODO getRecoveryLink

				// TODO recovery password

				// DELETE {{baseUrl}}/user/{{user}} - for user delete.
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access((a, o) -> {
					return new AuthorizationDecision(
							o.getRequest().isUserInRole(UserRoleEnum.ADMINISTRATOR.name()
									)
							|| customWebSecurity.checkLogin(a, o).isGranted());
				})

				// PUT/DELETE {{baseUrl}}/user/{{user}}/role/{{role}} - for adding a deleting role to user.
				.requestMatchers("/account/user/{login}/role/{role}")
				.hasRole(UserRoleEnum.ADMINISTRATOR.name())

				// PUT {{baseUrl}}/user/password - for changing user password.
				.requestMatchers(HttpMethod.PUT, "/account/user/password")
				.access((a, o) -> {
					return new AuthorizationDecision(a.get().isAuthenticated());
				})
				/**	
				 * Communication
				 */
				
				//  POST {{baseUrl}}/communication/parser/{{indexForHistory}}/{{csv}} - adding indexes from csv
				.requestMatchers(HttpMethod.POST, "/index/{indexName}/{path}")
				.hasRole(UserRoleEnum.ADMINISTRATOR.name())

				.anyRequest()
				.authenticated());
		
				return http.build();
	}

}
