/**
 * 
 */
package com.acma.properties.models;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author narsi
 * @since Sept-16,2024
 */
@Data
@Builder
public class Users implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9211350344279214899L;
	
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	
	@Builder.Default
	private boolean enabled=false;
	
	@Builder.Default
	private boolean emailVerified = false;
	
	@Builder.Default
	private boolean totp = false;
	
	private List<String> realmRoles;
	private UserAccess access;
	

}
