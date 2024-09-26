/**
 * 
 */
package com.acma.properties.services;

import java.util.List;

import com.acma.properties.beans.UsersBean;

/**
 * 
 */
public interface UsersService {

	public UsersBean createUser(UsersBean usersBean, String accessToken);
	public List<UsersBean> getAllUser(String accessToken);
	public List<UsersBean> getAllUsersOfAGroup(String groupId,String accessToken);
	
}
