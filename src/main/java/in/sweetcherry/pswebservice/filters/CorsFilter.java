package in.sweetcherry.pswebservice.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

public class CorsFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		final HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Access-Token, Authorization");
		
		chain.doFilter(req, res);

	}

}

/*public class CorsFilter extends OncePerRequestFilter {
	//private static final Log LOG = LogFactory.getLog(CORSFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
			//LOG.trace("Sending Header....");
			// CORS "pre-flight" request
			response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
			response.addHeader("Access-Control-Allow-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
			response.addHeader("Access-Control-Max-Age", "1");
		}
		
		filterChain.doFilter(request, response);
	}

}*/