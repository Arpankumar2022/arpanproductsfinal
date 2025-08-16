
package com.arpanbags.products.arpanbagsproducts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Value("#{'${cors.allowed-origins}'.split(',')}")
	private List<String> allowedOrigins;

	@Value("#{'${cors.allowed-methods}'.split(',')}")
	private List<String> allowedMethods;

	@Value("#{'${cors.allowed-headers}'.split(',')}")
	private List<String> allowedHeaders;

	@Value("#{'${cors.exposed-headers}'.split(',')}")
	private List<String> expectedHeaders;

	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() {
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * configuration.setAllowedOrigins(allowedOrigins);
	 * configuration.setAllowedMethods(allowedMethods);
	 * configuration.setAllowedHeaders(allowedHeaders);
	 * configuration.setExposedHeaders(expectedHeaders);
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource();
	 * source.registerCorsConfiguration("/v1/api/**", configuration); return source;
	 * }
	 */

	/**
	 *  --- What is preflight request?
	 * It is an HTTP request of the OPTIONS method, sent before the request itself,
	 * in order to determine if it is safe to send it. It is only after the server
	 * has sent a positive response that the actual HTTP request is sent.
	 * 
	 * Like as a ATM Card 
	 * 
	 * 
	 * The preflight request contains metadata with information like: Origin:
	 * indicates the origin of the request (server name);
	 * Access-Control-Request-Method: which HTTP methods will be used;
	 * Access-Control-Request-Headers: keys that will be in the headers.
	 * 
	 * 
	 */
	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		System.out.println("addCorsMappings :: ");
		              corsRegistry.addMapping("/**")
		                          .allowedOrigins(allowedOrigins.toArray(String[]::new))
				                  .allowedMethods(allowedMethods.toArray(String[]::new))
				                  .allowedHeaders(allowedHeaders.toArray(String[]::new))
				                  .allowCredentials(false)
				                  .maxAge(-1);
	}

}
