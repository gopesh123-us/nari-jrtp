package com.acma.properties.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsersResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6465221571377234927L;
	
	@JsonProperty
	private List<Users> usersList;
}
