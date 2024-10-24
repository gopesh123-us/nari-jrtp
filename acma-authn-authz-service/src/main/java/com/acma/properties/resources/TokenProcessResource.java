/**
 * 
 */
package com.acma.properties.resources;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@RestController
@Slf4j
public class TokenProcessResource {
	
	@Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
	@Autowired
	private RedisTemplate<String, String> acmaCacheServer;
	
	@Autowired
	private HttpServletResponse response;
	
	@GetMapping("/token")
	public Map<String, String> generateToken(OAuth2AuthenticationToken authentication){
		
		String subjectId = authentication.getName();
		log.info("Logged in User is "+authentication.getName());
		
		OAuth2AuthorizedClient authorizedClient = authorizedClientService
	                .loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		
		 String accessToken = authorizedClient.getAccessToken().getTokenValue();
		 log.info("access Token "+accessToken);
		 
		 OidcUser user = (OidcUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 log.info("id Token "+user.getIdToken().getTokenValue());
		 
		 Map<String, String>  usersMap = new HashMap<>();
		 usersMap.put("access_token", accessToken);
		 usersMap.put("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
		 usersMap.put("id_token", user.getIdToken().getTokenValue());
		 usersMap.put("token_type", authorizedClient.getAccessToken().getTokenType().getValue());
		 usersMap.put("token_expiry", authorizedClient.getAccessToken().getExpiresAt().toString());
		 
		 JSONObject jsonObject = new JSONObject(usersMap);
		 log.info("json Object is "+jsonObject.toString());
		 
		 acmaCacheServer.opsForHash().put(subjectId, subjectId, accessToken);
		 
		 //response.addHeader("AcmaCk", jsonObject.toString());
		 Cookie cookie = new Cookie("AcmaCk", Base64.getEncoder().encodeToString(jsonObject.toString().getBytes()));
		 cookie.setPath("/");
		 cookie.setMaxAge(authorizedClient.getAccessToken().getExpiresAt().getNano());
		 cookie.setDomain("localhost");
		 
		 response.addCookie(cookie);
		 try {
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		
		return usersMap;
	}
}
