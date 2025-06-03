package com.safetynet.alerts.exception;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EmptyRequestParamFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		Map<String, String[]> params = req.getParameterMap();

		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String paramName = entry.getKey();
			String[] values = entry.getValue();
			for (String value : values) {
				if (value == null || value.trim().isEmpty()) {
					((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Le paramètre '" + paramName + "' ne peut pas être vide.");
					return;
				}
			}
		}

		chain.doFilter(request, response);
	}
}