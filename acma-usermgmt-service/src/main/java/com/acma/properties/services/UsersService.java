/**
 * 
 */
package com.acma.properties.services;

import java.util.List;

import com.acma.properties.beans.UsersBean;
import com.acma.properties.exceptions.UsersException;

/**
 * 
 */
public interface UsersService {

	UsersBean createUser(UsersBean usersBean, String accessToken);
	UsersBean getUserById(String userId, String accessToken);
	boolean deleteUser(String userId, String accessToken);
	List<UsersBean> getAllUsers(String accessToken) throws UsersException;
	List<UsersBean> getAllUsersOfAGroup(String groupId,String accessToken);
	
}
