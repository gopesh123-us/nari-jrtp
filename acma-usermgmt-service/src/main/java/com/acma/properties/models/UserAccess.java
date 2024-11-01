/**
 * 
 */
package com.acma.properties.models;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author narsi
 * @since Sept-16,2024
 */
@Data
//@Builder
@NoArgsConstructor
public class UserAccess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4594000568387073463L;
	
	//@Builder.Default
	boolean manageGroupMembership = true;
	
	//@Builder.Default
	boolean view = true;
	
	//@Builder.Default
	boolean mapRoles = true;
	
	//@Builder.Default
	boolean impersonate = true;
	
	//@Builder.Default
	boolean manage= true;

}
