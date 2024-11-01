/**
 * 
 */
package com.acma.properties.outbound;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.acma.properties.exceptions.UsersException;
import com.acma.properties.models.Users;
import com.acma.properties.outboundutils.AcmaOutboundUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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

	@Value("${acma.iam.groupsApi}")
	private String acma_groups_api_url;

	@Value("${acma.iam.groups.property-owners}")
	private String propertyOwnersGroupId;

	public AcmaUsersOutboundApi(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Returns all the property owners
	 * 
	 * @param accessToken
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws UsersException 
	 */
	public List<Users> getAllUser(String accessToken) throws UsersException {
		log.info("IAM Users API {}", acma_users_api_url);

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);

		try {
			long start = System.currentTimeMillis();
			ResponseEntity<List<Users>> responseEntity = restTemplate.exchange(acma_users_api_url, HttpMethod.GET,
					httpEntity, new ParameterizedTypeReference<List<Users>>() {});
			log.info("API Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
				List<Users> usersList = responseEntity.getBody();
				log.info("Count of the Users {}", usersList.size());
				long end = System.currentTimeMillis();
				log.info("Total Time taken in millis is "+(end-start));
				return Optional.ofNullable(usersList).get();

			} else {
				throw new UsersException(responseEntity.toString(),responseEntity.getStatusCode().value());
			}

		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Exception:: "+e.getLocalizedMessage());
			throw new UsersException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	/**
	 * Returns all the property owners
	 * 
	 * @param accessToken
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public List<Users> getAllUsersOfAGroup(String groupId, String accessToken)
			throws RestClientException, URISyntaxException, JsonMappingException, JsonProcessingException {
		log.info("getAllPropertyOwners {}", acma_groups_api_url);
		String acmaUsersApi = acma_groups_api_url + "/" + groupId + "/members";
		log.info("acmaUsersApi {}", acmaUsersApi);
		
		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);
		try {
			ResponseEntity<List<Users>> responseEntity = restTemplate.exchange(acmaUsersApi, HttpMethod.GET, httpEntity,
					new ParameterizedTypeReference<List<Users>>() {});
			log.info("API Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
				List<Users> modifiedUsersList = new ArrayList<>();
				List<Users> usersListResp = responseEntity.getBody();
				log.info("Count of the Users {}", usersListResp.size());
				if(!CollectionUtils.isEmpty(usersListResp)) {
					usersListResp.stream().forEach(user->{
						user.setGroupId(groupId);
						modifiedUsersList.add(user);
					});
				}
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

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(user, accessToken);

		try {
			ResponseEntity<?> responseEntity = restTemplate.postForEntity(acma_users_api_url, httpEntity, null);
			log.info("API Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.CREATED.value() == responseEntity.getStatusCode().value()) {
				HttpHeaders responseHeaders = responseEntity.getHeaders();
				if (responseHeaders.containsKey("Location")) {
					String locHeaderVal = responseHeaders.getFirst("Location");
					log.info("location header value is " + locHeaderVal);
					String userId = locHeaderVal.replace(acma_users_api_url + "/", "");
					log.info("User Id From location header is " + userId);
					String groupId = null;
					if (!StringUtils.hasLength(user.getGroupId())) {
						log.info("Property Owners Group Id is {}", groupId);
						groupId = propertyOwnersGroupId;
					} else {
						log.info("Others Group Id is {}", groupId);
						groupId = user.getGroupId();
					}
					// provisioning the user to a particular group
					boolean isGroupProvisioned = provisionUserUnderAGroup(userId, groupId, accessToken);
					if (isGroupProvisioned) {
						user.setGroupId(groupId);

					} else {
						// delete the user
					}
				}
			} else {
				throw new RuntimeException("something went wrong while user is creating");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	public boolean provisionUserUnderAGroup(String userId, String groupId, String accessToken) {
		boolean groupProvisioned = false;
		String provisioningApi = acma_users_api_url + "/" + userId + "/groups/" + groupId;
		log.info("User Provisioning:: API is {}", provisioningApi);

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);
		Map<String, String> param = new HashMap<String, String>();

		try {
			ResponseEntity responseEntity = restTemplate.exchange(provisioningApi, HttpMethod.PUT, httpEntity,
					ResponseEntity.class, param);
			log.info("User Provisioning:: Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.NO_CONTENT.value() == responseEntity.getStatusCode().value()) {
				groupProvisioned = true;
			} else {
				throw new RuntimeException("Something went wrong while provisioining the user");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return groupProvisioned;

	}

	public boolean DeProvisionUserUnderAGroup(String userId, String groupId, String accessToken) {
		boolean groupDeProvisioned = false;

		log.info("User DeProvisioning:: User Id is {}", userId);
		log.info("User DeProvisioning:: Group Id is {}", groupId);

		String provisioningApi = acma_users_api_url + "/" + userId + "/groups/" + groupId;
		log.info("User Provisioning:: API is {}", provisioningApi);

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);
		Map<String, String> param = new HashMap<String, String>();
		try {
			ResponseEntity responseEntity = restTemplate.exchange(provisioningApi, HttpMethod.DELETE, httpEntity,
					ResponseEntity.class, param);
			log.info("User Provisioning:: Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.NO_CONTENT.value() == responseEntity.getStatusCode().value()) {
				groupDeProvisioned = true;
			} else {
				throw new RuntimeException("Something went wrong wile user is deprovisioing");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return groupDeProvisioned;

	}

	public Users getUserByUserId(String userId, String accessToken) {

		log.info("getUserByUserId:: User Id is {}", userId);

		String usersApi = acma_users_api_url+"/" + userId;
		log.info("getUserByUserId:: API is {}", usersApi);

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);
		Map<String, String> param = new HashMap<String, String>();
		try {
			ResponseEntity<Users> responseEntity = restTemplate.exchange(usersApi, HttpMethod.GET, httpEntity,
					Users.class);
			log.info("getUserByUserId:: Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.OK.value() == responseEntity.getStatusCode().value()) {
				Users acmaUser = responseEntity.getBody();
				return acmaUser;
			} else {
				throw new RuntimeException("Something went wrong wile user is deprovisioing");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean deleteUserByUserId(String userId, String accessToken) {
		boolean isUserHardDelete = false;
		log.info("deleteUserByUserId:: User Id is {}", userId);

		String usersApi = acma_users_api_url+"/" + userId;
		log.info("deleteUserByUserId:: API is {}", usersApi);

		HttpEntity httpEntity = AcmaOutboundUtils.getHttpEntity(null, accessToken);
		Map<String, String> param = new HashMap<String, String>();
		try {
			ResponseEntity responseEntity = restTemplate.exchange(usersApi, HttpMethod.DELETE, httpEntity,
					ResponseEntity.class);
			log.info("deleteUserByUserId:: Response Code is {}", responseEntity.getStatusCode().value());
			if (HttpStatus.NO_CONTENT.value() == responseEntity.getStatusCode().value()) {
				isUserHardDelete = true;
			} else {
				throw new RuntimeException("Something went wrong wile user is deprovisioing");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return isUserHardDelete;
	}

}
