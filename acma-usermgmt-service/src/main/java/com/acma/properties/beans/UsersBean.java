/**
 * 
 */
package com.acma.properties.beans;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
	
	@NotEmpty(message = "{username.notEmpty.message}")
	@Size(min = 3,max = 255,message = "{username.size.message}")
	private String username;
	
	@NotEmpty(message = "{firstName.notEmpty.message}")
	@Size(max = 255,message = "firstName.size.message")
	private String firstName;
	
	@NotEmpty(message = "{lastName.notEmpty.message}")
	@Size(max = 255,message = "{lastName.size.message}")
	private String lastName;
	
	@NotEmpty(message = "{email.notEmpty.message}")
	@Size(max = 255,message = "{email.size.message}")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",message = "{email.pattern.message}")
	private String email;
	
	@NotEmpty(message = "{mobile.notEmpty.message}")
	@Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",message = "{mobile.pattern.message}")
	private String mobile;
	
	//@Builder.Default
	private boolean enabled=false;
	
	//@Builder.Default
	private boolean emailVerified = false;
	
	//@Builder.Default
	private boolean totp = false;
	
	private String groupId;
	
	private String id;
	
	

}
