/**
 * 
 */
package com.acma.properties.services;

import org.springframework.stereotype.Service;

import com.acma.properties.outbound.AcmaUsersOutboundApi;

/**
 * 
 */
@Service
public class GroupsServiceImpl implements GroupsService {
	
	private AcmaUsersOutboundApi acmaUsersOutboundApi;
	
	public GroupsServiceImpl(AcmaUsersOutboundApi acmaUsersOutboundApi) {
		this.acmaUsersOutboundApi = acmaUsersOutboundApi;
	}

	@Override
	public boolean provisionUser(String userId, String groupId, String accessToken) {
		return acmaUsersOutboundApi.provisionUserUnderAGroup(userId, groupId, accessToken);
	}

	@Override
	public boolean deProvisionUser(String userId, String groupId, String accessToken) {
		return acmaUsersOutboundApi.DeProvisionUserUnderAGroup(userId, groupId, accessToken);
	}

}
