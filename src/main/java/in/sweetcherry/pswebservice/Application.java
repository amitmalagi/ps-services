package in.sweetcherry.pswebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import in.sweetcherry.pswebservice.filters.JwtFilter;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement 
public class Application {

	@Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/orders/*");
        registrationBean.addUrlPatterns("/employees/*");

        return registrationBean;
    }
	
	/*@Bean
	public FilterRegistrationBean corsFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }*/
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }
	
	@Bean
	public FilterRegistrationBean loggingFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		
		CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	    loggingFilter.setIncludeClientInfo(true);
	    loggingFilter.setIncludeQueryString(true);
	    loggingFilter.setIncludePayload(true);
	    loggingFilter.setIncludeHeaders(true);
	    
	    registrationBean.setFilter(loggingFilter);
	    registrationBean.addUrlPatterns("/*");
	    
	    return registrationBean;
	}
	
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
