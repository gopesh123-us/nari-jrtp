/**
 * 
 */
package com.acma.properties.outbound;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.acma.properties.models.Users;
import com.acma.properties.models.UsersResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author narsi
 * @since Sept-17,2024 
 */

@Component
@Slf4j
public class AcmaUsersOutboundApi {

	private RestTemplate restTemplate;
	
	@Value("${acma.iam.usersApi}")
	private String acma_users_api_url;
	
	
	public AcmaUsersOutboundApi(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public List<Users> getAllPropertyOwners(String accessToken) throws RestClientException, URISyntaxException{
		log.info("IAM Users API {}",acma_users_api_url);
		
		List<Users> usersList = new ArrayList<>();
		
		
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
		headersMap.add("Authorization", "bearer "+accessToken);
		
		HttpEntity<String> entity = new HttpEntity<>(headersMap);
		
		ResponseEntity<UsersResponse> responseEntity = restTemplate.exchange(acma_users_api_url, HttpMethod.GET, entity, UsersResponse.class);
		
	   //ResponseEntity<List> responseEntity =	restTemplate.getForEntity(acma_users_api_url, List.class,headersMap);
	   log.info("API Response Code is {}",responseEntity.getStatusCode().value());
	 if(HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
		 UsersResponse usersRespBody =  responseEntity.getBody();
		 log.info("Count of the Users {}",usersRespBody.getUsersList().size());
		 
	 }
		return null;
	}
	
	public List<Users> getAllAgents(){
		return null;
	}
	
	public List<Users> getAllBrokers(){
		return null;
	}
	
	public Users createUser(Users user) {
		
		return null;
	}
	
}
