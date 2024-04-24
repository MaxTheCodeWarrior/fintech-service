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

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {

	final CustomWebSecurity customWebSecurity;
	final CorsCustomConfiguration corsConfigurationSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable()); // Disables CSRF protection
		http.cors(cors -> cors.configurationSource(corsConfigurationSource.corsConfigurationSource())); // CORS
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // Enables cookies
		http.authorizeHttpRequests(authorize -> authorize

				// User Management
				// POST {{baseUrl}}/register - for user registration. access to H2.
				.requestMatchers("/account/register", "/h2-console/**").permitAll()

				// DELETE {{baseUrl}}/user/{{user}} - for user delete.
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access((a,
						o) -> new AuthorizationDecision(o.getRequest().isUserInRole(UserRoleEnum.ADMINISTRATOR.name())
								|| customWebSecurity.checkLogin(a, o).isGranted()))

				
				// PUT {{baseUrl}}/account/user/{user} - for update user info.
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
				.access((a,
						o) -> new AuthorizationDecision(customWebSecurity.checkLogin(a, o).isGranted()))

				// PUT/DELETE {{baseUrl}}/user/{{user}}/role/{{role}} - for add or delete UserRole.
				.requestMatchers("/account/user/{login}/role/{role}").hasRole(UserRoleEnum.ADMINISTRATOR.name())

				// PUT {{baseUrl}}/account/password - for changing user password.
				.requestMatchers(HttpMethod.PUT, "/account/password")
				.access((a, o) -> new AuthorizationDecision(a.get().isAuthenticated()))

				// Communication
				
				// POST {{baseUrl}}/communication/parser/{{indexForHistory}}/{{csv}} - adding
				// indexes from csv
				.requestMatchers(HttpMethod.POST, "/index/{indexName}/{path}")
				.hasRole(UserRoleEnum.ADMINISTRATOR.name())

				.anyRequest().authenticated());

		return http.build();
	}
}
