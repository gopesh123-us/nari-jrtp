/**
 * 
 */
package com.acma.properties.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acma.properties.beans.UsersBean;
import com.acma.properties.exceptions.UsersException;
import com.acma.properties.services.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@RestController
@Slf4j
public class UsersResource {

	private UsersService usersService;
	HttpServletRequest request;
	
	private String bearerPrefix = "Bearer";
	
	
	public UsersResource(UsersService usersService,HttpServletRequest request) {
		this.usersService = usersService;
		this.request = request;
	}
	
	@GetMapping(value = "/users")
	@Operation(description = "getAllUsers",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<List<UsersBean>> getAllUsers() throws UsersException{
		log.info("UsersResource::getAllUsers");
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		List<UsersBean> usersList =  usersService.getAllUsers(bearerToken);
		return new ResponseEntity<List<UsersBean>>(usersList, HttpStatus.OK);	
		
		
	}
	
	@GetMapping(value = "/users/owners")
	@Operation(description = "getAllPropertyOwners",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<List<UsersBean>> getAllPropertyOwners(String ownerGroupId){
		log.info("UsersResource::getAllPropertyOwners  "+ownerGroupId);
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		List<UsersBean> acmaAgentsList =  usersService.getAllUsersOfAGroup(ownerGroupId, bearerToken);
		return ResponseEntity.ok(acmaAgentsList);
	}
	
	@GetMapping(value = "/users/agents")
	@Operation(description = "getAllAgents",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<List<UsersBean>> getAllAgents(String agentGroupId){
		log.info("UsersResource::getAllAgents  "+agentGroupId);
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		List<UsersBean> acmaAgentsList =  usersService.getAllUsersOfAGroup(agentGroupId, bearerToken);
		return ResponseEntity.ok(acmaAgentsList);
	}
	
	@GetMapping(value = "/users/brokers")
	@Operation(description = "getAllBrokers",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<List<UsersBean>> getAllBrokers(String brokerGroupId){
		log.info("UsersResource::getAllBrokers  "+brokerGroupId);
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		List<UsersBean> acmaAgentsList =  usersService.getAllUsersOfAGroup(brokerGroupId, bearerToken);
		return ResponseEntity.ok(acmaAgentsList);
	}
	
	@PostMapping(value = "/users")
	@Operation(description = "createUser",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<UsersBean> createUser(@Valid @RequestBody UsersBean usersBean){
		log.info("UsersResource::createUser"+usersBean.toString());
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		//usersBean = usersService.createUser(usersBean, bearerToken);
		return new ResponseEntity<UsersBean>(usersBean, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/users/{userId}")
	@Operation(description = "getUserById",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<UsersBean> getUserById(@PathVariable("userId") String uid){
		log.info("getUserById::user id , {}", uid);
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		UsersBean userBean = usersService.getUserById(uid, bearerToken);
		return ResponseEntity.ok(userBean);
	}
	
	@DeleteMapping(value = "/users/{userId}")
	@Operation(description = "deleteUserById",security = @SecurityRequirement(name="bearerAuth"))
	public ResponseEntity<Object> deleteUserById(@PathVariable("userId") String uid){
		log.info("getUserById::user id , {}", uid);
		String bearerToken = request.getHeader("Authorization");
		log.info("bearer token is {}",bearerToken);
		if(StringUtils.hasText(bearerToken) && (StringUtils.hasText(bearerPrefix))){
			bearerToken =  StringUtils.replace(bearerToken, bearerPrefix, "");		
		}
		log.info("token is {}",bearerToken);
		boolean isUserDeleted = usersService.deleteUser(uid, bearerToken);
		Map<String,String> messagMap = new HashMap<>();
		messagMap.put("message", "User "+uid+" deleted "+isUserDeleted);
		return ResponseEntity.ok(messagMap);
	}
	
}
