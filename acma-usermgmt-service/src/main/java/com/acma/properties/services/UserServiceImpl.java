/**
 * 
 */
package com.acma.properties.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acma.properties.beans.UsersBean;

/**
 * 
 */
@Service
public class UserServiceImpl implements UsersService {

	@Override
	public UsersBean createUser(UsersBean usersBean, String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsersBean> getAllUser(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsersBean> getAllUsersOfAGroup(String groupId, String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
