/**
 * 
 */
package com.acma.properties.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import com.acma.properties.beans.UsersBean;
import com.acma.properties.models.Users;
import com.acma.properties.outbound.AcmaUsersOutboundApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class UserServiceImpl implements UsersService {

	private AcmaUsersOutboundApi acmaUsersOutboundApi;

	private ModelMapper modelMapper;

	public UserServiceImpl(AcmaUsersOutboundApi acmaUsersOutboundApi, ModelMapper modelMapper) {
		this.acmaUsersOutboundApi = acmaUsersOutboundApi;
		this.modelMapper = modelMapper;
	}

	@Override
	public UsersBean createUser(UsersBean usersBean, String accessToken) {
		log.info("Request Data is ,{}",usersBean.toString());
		Users users = modelMapper.map(usersBean, Users.class);
		log.info("Users are: " + users.toString());

		users = acmaUsersOutboundApi.createUser(users, accessToken);
		usersBean = modelMapper.map(users, UsersBean.class);
		return usersBean;
	}

	@Override
	public List<UsersBean> getAllUsers(String accessToken) {
		try {
			List<Users> usersList = acmaUsersOutboundApi.getAllUser(accessToken);
			if (!CollectionUtils.isEmpty(usersList)) {
				log.info("Collection is not empty");
				List<UsersBean> uiUsersList = new ArrayList<>();
				usersList.stream().forEach(user -> {
					uiUsersList.add(modelMapper.map(user, UsersBean.class));
				});
				return uiUsersList;
			}else {
				throw new RuntimeException("No Users Found in the ACMA AD");
			}
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UsersBean> getAllUsersOfAGroup(String groupId, String accessToken) {
		try {
			List<Users> usersList = acmaUsersOutboundApi.getAllUsersOfAGroup(groupId, accessToken);
			if (!CollectionUtils.isEmpty(usersList)) {
				List<UsersBean> uiUsersList = new ArrayList<>();
				usersList.stream().forEach(user -> {
					UsersBean usersBean = modelMapper.map(user, UsersBean.class);
					log.info("Users Bean is {}",usersBean.toString());
					uiUsersList.add(modelMapper.map(user, UsersBean.class));
				});
				return uiUsersList;
			}else {
				throw new RuntimeException("No Users Found in the ACMA AD");
			}
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UsersBean getUserById(String userId, String accessToken) {
		try {
			return modelMapper.map(acmaUsersOutboundApi.getUserByUserId(userId, accessToken),UsersBean.class);
		}catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public boolean deleteUser(String userId, String accessToken) {
		try {
			return acmaUsersOutboundApi.deleteUserByUserId(userId, accessToken);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
