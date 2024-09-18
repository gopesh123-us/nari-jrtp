package com.acma.properties.models;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize
public class UsersResponse {

	private List<Users> usersList;
}
