package fintechservice.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import fintechservice.accounting.dao.AccountingRepository;
import fintechservice.accounting.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(10)
public class AuthenticationFilter implements Filter {

	final AccountingRepository accountingRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		 // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "register");
        response.setHeader("Access-Control-Max-Age", "3600");

        // Handle OPTIONS request
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
	        
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			try {
				String[] credentials = getCredentials(request.getHeader("Authorization"));
				User user = accountingRepository.findById(credentials[0])
						.orElseThrow(RuntimeException::new);
				if (!BCrypt.checkpw(credentials[1], user.getPassword())) {
					throw new RuntimeException();
				}
				request = new WrappedRequest(request, user.getLogin());
			} catch (Exception e) {
				response.sendError(401);
				return;
			}
		}
		
		chain.doFilter(request, response);

	}
	
	
	private boolean checkEndPoint(String method, String path) {
	   return !(
			   (HttpMethod.POST.matches(method) &&
	            path.equals("/account/register")) 
	            		);
	}

	
	private String[] getCredentials(String header) {
		String token = header.split(" ")[1];
		String decode = new String(Base64.getDecoder().decode(token));
		return decode.split(":");
	}

	private class WrappedRequest extends HttpServletRequestWrapper {
		private String login;

		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}

		@Override
		public Principal getUserPrincipal() {
			return () -> login;
		}
	}

}
