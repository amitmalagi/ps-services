package in.sweetcherry.pswebservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.sweetcherry.pswebservice.PSWebServiceProperties;
import in.sweetcherry.pswebservice.models.Employee;
import in.sweetcherry.pswebservice.models.EmployeeProfile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Component
public class JwtService {
    private static final String ISSUER = "in.sweetcherry.jwt";
    
    private PSWebServiceProperties wsProperties;

	public String generateToken(Employee employee) throws IOException, URISyntaxException {
		String secretKey = wsProperties.getWs_cookie_key();

		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = new EmployeeProfile(employee.getEmployeeId(), employee.getEmail(),
				employee.getFirstname(), employee.getLastname(), employee.getProfileId(), employee.getProfile());
		//Date expiration = Date.from(LocalDateTime.now(UTC).plusDays(1).toInstant(UTC));
		//Set expiration - .setExpiration(expiration)
		return Jwts.builder()
				.setSubject(employee.getEmail())
				.claim("profile", mapper.writeValueAsString(employeeProfile))
				.setIssuer(ISSUER)
				.setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

    public Claims verifyToken(String token) throws SignatureException, IOException, URISyntaxException {
        String secretKey = wsProperties.getWs_cookie_key();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claims.getBody();
    }
    
    @Autowired
	public void setWsProperties(PSWebServiceProperties wsProperties) {
		this.wsProperties = wsProperties;
	}
}