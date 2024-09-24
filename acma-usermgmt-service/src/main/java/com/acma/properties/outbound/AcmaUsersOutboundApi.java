/**
 * 
 */
package com.acma.properties.outbound;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.acma.properties.models.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author narsi
 * @since Sept-17,2024
 */

@Component
@Slf4j
public class AcmaUsersOutboundApi {

	private RestTemplate restTemplate;

	private ObjectMapper objectMapper;

	@Value("${acma.iam.usersApi}")
	private String acma_users_api_url;
	
	@Value("${acma.iam.groups.property-owners}")
	private String propertyOwnersGroupId;

	public AcmaUsersOutboundApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * Returns all the property owners
 	 * @param accessToken
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public List<Users> getAllPropertyOwners(String accessToken)
			throws RestClientException, URISyntaxException, JsonMappingException, JsonProcessingException {
		log.info("IAM Users API {}", acma_users_api_url);

		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
		headersMap.add("Authorization", "bearer " + accessToken);

		HttpEntity<String> entity = new HttpEntity<>(headersMap);
		try {
			long start = System.currentTimeMillis();
			
//			ResponseEntity<List<Users>> responseEntity = restTemplate.exchange(acma_users_api_url, HttpMethod.GET,
//					entity, new ParameterizedTypeReference<List<Users>>() {
//					});
//			log.info("API Response Code is {}", responseEntity.getStatusCode().value());
//			if (HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
//				List<Users> usersList = responseEntity.getBody();
//				log.info("Count of the Users {}", usersList.size());
//				long end = System.currentTimeMillis();
//				log.info("Total Time taken in millis is "+(end-start));
//				return Optional.ofNullable(usersList).get();
//
//			} else {
//				throw new RuntimeException("HttpStatusCode " + responseEntity.getStatusCode().value());
//			}
			 
			
			ResponseEntity<?> responseEntity = restTemplate.exchange(acma_users_api_url, HttpMethod.GET, entity, Object.class);
			log.info("API Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
				List<Users> usersListResp = (List<Users>) responseEntity.getBody();
				log.info("Count of the Users {}", usersListResp.size());
				long end = System.currentTimeMillis();
				log.info("Total Time taken in millis is "+(end-start));
				return usersListResp;
			} else {
				throw new RuntimeException("HttpStatusCode " + responseEntity.getStatusCode().value());
			}
			
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<Users> getAllAgents() {
		return null;
	}

	public List<Users> getAllBrokers() {
		return null;
	}

	public Users createUser(Users user, String accessToken) {
		
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
	    headersMap.add("Authorization", "Bearer "+accessToken);
	    headersMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		
	    HttpEntity requestBody = new HttpEntity<>(user,headersMap);
	    
	    try {
	    	ResponseEntity<?> responseEntity = restTemplate.postForEntity(acma_users_api_url, requestBody, null);
		    log.info("API Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.CREATED.value() == responseEntity.getStatusCode().value()) {
				HttpHeaders responseHeaders = responseEntity.getHeaders();
				if(responseHeaders.containsKey("Location")){
					String locHeaderVal = responseHeaders.getFirst("Location");
					log.info("location header value is "+locHeaderVal);
					String userId =  locHeaderVal.replace(acma_users_api_url+"/", "");
					log.info("User Id From location header is "+userId);
					String groupId = null;
					if(!StringUtils.hasLength(user.getGroupId())) {
						log.info("Property Owners Group Id is {}", groupId);
						groupId = propertyOwnersGroupId;
					}else {
						log.info("Others Group Id is {}", groupId);
						groupId = user.getGroupId();
					}
					boolean isGroupProvisioned = provisionUserUnderAGroup(userId,groupId,accessToken);
					if(isGroupProvisioned) {
						user.setUserId(userId);
						user.setGroupId(groupId);
						
					}else {
						//delete the user
					}
				}
			}else {
				throw new RuntimeException("something went wrong while user is creating");
			}
	    }catch (Exception e) {
			throw new RuntimeException(e);
		}
		return user;
	}


	private boolean provisionUserUnderAGroup(String userId, String groupId, String accessToken) {
		boolean groupProvisioned = false;
		String provisioningApi = acma_users_api_url+"/"+userId+"/groups/"+groupId;
		log.info("User Provisioning:: API is {}", provisioningApi);
		
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
		headersMap.add("Authorization", "Bearer "+accessToken);
		
		HttpEntity request = new HttpEntity<>(headersMap);
		 Map<String, String> param = new HashMap<String, String>();
		 
		 ResponseEntity responseEntity = restTemplate.exchange(provisioningApi, HttpMethod.PUT, request, ResponseEntity.class,param);
		 log.info("User Provisioning:: Response Code is {}", responseEntity.getStatusCode().value());
		 if(HttpStatus.NO_CONTENT.value() == responseEntity.getStatusCode().value()) {
			 groupProvisioned = true; 
		 }
		return groupProvisioned;
		
	}
}
