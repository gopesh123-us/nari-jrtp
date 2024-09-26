/**
 * 
 */
package com.acma.properties.services;

/**
 * 
 */
public interface GroupsService {

	boolean provisionUser(String userId, String groupId, String accessToken);
	boolean deProvisionUser(String userId, String groupId, String accessToken);
}
