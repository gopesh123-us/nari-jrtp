/**
 * 
 */
package com.acma.properties.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acma.properties.beans.UsersBean;
import com.acma.properties.services.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
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
	public ResponseEntity<List<UsersBean>> getAllUsers(){
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
	public ResponseEntity<List<UsersBean>> getAllPropertyOwners(){
		return null;
	}
	
	@GetMapping(value = "/users/agents")
	public ResponseEntity<List<UsersBean>> getAllAgents(){
		return null;
	}
	
	@GetMapping(value = "/users/brokers")
	public ResponseEntity<List<UsersBean>> getAllBrokers(){
		return null;
	}
	
	@PostMapping(value = "/users")
	public ResponseEntity<UsersBean> createUser(){
		
		return null;
	}
	
	@GetMapping(value = "/users/{userId}")
	public ResponseEntity<UsersBean> getUserById(@PathVariable("userId") String uid){
		
		return null;
	}
	
	@DeleteMapping(value = "/users/{userId}")
	public ResponseEntity<UsersBean> deleteUserById(@PathVariable("userId") String uid){
		
		return null;
	}
	
}
