/**
 * 
 */
package com.acma.properties.beans;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
@Data
@NoArgsConstructor
public class UsersBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -53038970142094035L;
	
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	
	//@Builder.Default
	private boolean enabled=false;
	
	//@Builder.Default
	private boolean emailVerified = false;
	
	//@Builder.Default
	private boolean totp = false;
	
	private String groupId;
	
	private String id;
	
	

}
