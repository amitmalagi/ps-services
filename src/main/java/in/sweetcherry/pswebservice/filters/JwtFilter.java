package in.sweetcherry.pswebservice.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import in.sweetcherry.pswebservice.services.JwtService;
import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends GenericFilterBean {

	@Autowired
	private JwtService jwtService;

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;

		if(jwtService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            jwtService = webApplicationContext.getBean(JwtService.class);
        }
		
		if (!"OPTIONS".equals(request.getMethod())) {
			final String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new ServletException("Missing or invalid Authorization header.");
			}

			final String token = authHeader.substring(7); // The part after
															// "Bearer "

			try {
				final Claims claims = jwtService.verifyToken(token);
				request.setAttribute("claims", claims);
			} catch (final Exception ex) {
				logger.error("ERROR", ex);
				throw new ServletException("Invalid token. " + ex.getMessage());
			}
		}
		chain.doFilter(req, res);
	}

	/*@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}*/
}
