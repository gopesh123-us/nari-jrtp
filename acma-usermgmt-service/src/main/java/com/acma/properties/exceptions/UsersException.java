/**
 * 
 */
package com.acma.properties.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author narsi
 * @Date: Oct-04-2024 
 */

@Getter
//@Builder
@AllArgsConstructor
public class UsersException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7126355645303158479L;
	
	private String errorMessage;
	private int errorCode;

}
