/**
 * 
 */
package com.acma.properties.advices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.acma.properties.beans.UsersExceptionsDisplayBean;
import com.acma.properties.exceptions.UsersException;
import com.acma.properties.exceptions.UsersRestException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RestControllerAdvice
public class UserMgmtExceptionsController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(UsersException.class)
	public ResponseEntity<UsersExceptionsDisplayBean> handleBusinessExceptions(UsersException usersException){
		log.info("Users Exception is "+usersException.getErrorMessage());
		
		log.info("GET Class "+usersException.getClass());
		
		for(StackTraceElement ste :  usersException.fillInStackTrace().getStackTrace()) {
			log.info("class name "+ste.getClassName());
			log.info("Method Name: "+ste.getMethodName());
			log.info("Line Number "+ste.getLineNumber());
			
			
		}
		
		usersException.printStackTrace();
		
		UsersExceptionsDisplayBean uedb = UsersExceptionsDisplayBean.builder()
											.message(usersException.getErrorMessage())
											.code(usersException.getErrorCode())
											.build();
		return ResponseEntity.ok(uedb);
	}
	
	@ExceptionHandler(UsersRestException.class)
	public ResponseEntity<UsersRestException> handleControllerExceptions(UsersRestException usersException){
		log.info("handleControllerExceptions "+usersException);
//		UsersExceptionsDisplayBean uedb = UsersExceptionsDisplayBean.builder()
//											.message(usersException.getErrorMessage())
//											.code(usersException.getErrorCode())
//											.build();
		return ResponseEntity.ok(usersException);
	}
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.info("handleMethodArgumentNotValid ");
		List<UsersExceptionsDisplayBean> validationErrorsList = new ArrayList<>();
		ex.getBindingResult().getAllErrors().stream().forEach(validationError -> {
			UsersExceptionsDisplayBean uedb = UsersExceptionsDisplayBean.builder()
					.message(validationError.getDefaultMessage())
					.code(HttpStatus.BAD_REQUEST.value())
					.build();
			validationErrorsList.add(uedb);
		});
		
		log.info("validation errors size is "+validationErrorsList.size());
		return ResponseEntity.badRequest().body(validationErrorsList);
	}
	
	

}
