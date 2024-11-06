/**
 * 
 */
package com.acma.properties.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Configuration
@Slf4j
public class RouteConfig {

//	@Bean
//	public RouteLocator routeDefinitions(RouteLocatorBuilder builder) {
//		log.info("****Route Definitions Locator****");
//		return builder.routes()
//				
//		.route("01-usermgmt-api-routes", (route->route.path("/users/**")
//												  .uri("lb://ACMA-USERMGMT-SERVICE")
//													    
//											      ))
//		.route("01a-usermgmt-document-routes", (route->route.path("/swagger-ui.html")//predicate
//													        .uri("lb://ACMA-USERMGMT-SERVICE")))
//		
//		.route("01b-usermgmt-document-routes", (route->route.path("/swagger-ui/**")
//															.uri("lb://ACMA-USERMGMT-SERVICE")))
//		
//		.route("01c-usermgmt-document-routes", (route->route.path("/v3/**")
//															.uri("lb://ACMA-USERMGMT-SERVICE")))
//		
//		.route("02-authz-token-routes", (route->route.path("/token")
//				                                      .uri("lb://ACMA-AUTHN-AUTHZ-SERVICE")))
//		
//		.build();
//		
//	}
}
