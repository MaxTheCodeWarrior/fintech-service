package fintechservice.security;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import fintechservice.accounting.dao.AccountingRepository;
import fintechservice.accounting.dto.UserRoleEnum;
import fintechservice.accounting.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(20)
public class AdminManagingRolesFilter implements Filter {
	
	final AccountingRepository accountingRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			User user = accountingRepository.findById(request.getUserPrincipal().getName()).get();
			if(!user.getRoles().contains(UserRoleEnum.ADMINISTRATOR)) {
				response.sendError(403, "Permission denied");
				return;
			}
		}
	
		chain.doFilter(request, response);
		
	}

	private boolean checkEndPoint(String method, String path) {
		
		return path.matches("/account/user/\\w+/role/\\w+");
	}

}
