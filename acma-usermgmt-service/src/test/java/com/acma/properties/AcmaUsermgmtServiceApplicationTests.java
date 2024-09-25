package com.acma.properties;

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import com.acma.properties.models.Users;
import com.acma.properties.outbound.AcmaUsersOutboundApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AcmaUsermgmtServiceApplicationTests {

	static Users newUser = null;
	static String accessToken = null;
	static String propertyOwnersGroupId =null;
	static String acmaBrokersGroupId= null;
	static String acmaAgentsGroupId= null;
	
	@Test
	void contextLoads() {
		accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ5cHFuM2lZR0xDV1pKcl8yUlVpZmlqcEVzZWF3MUJBeHd5SnpqSk5neHFzIn0.eyJleHAiOjE3MjcwNjA3NDcsImlhdCI6MTcyNzA1OTI0NywianRpIjoiZTZjN2MzYjYtNmE3MS00ZWQxLWI1YWUtMDQ3YzMxOTBhY2E1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9hY21hIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImZlZjQxMzA5LTk4MWMtNDY1MS1hMDRjLTIxN2VlNjI5N2ZmZCIsInR5cCI6IkJlYXJlciIsImF6cCI6InN0YXRlZm9ybS1zdmMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWFjbWEiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjE3Mi4xOC4wLjEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtc3RhdGVmb3JtLXN2YyIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTguMC4xIiwiY2xpZW50X2lkIjoic3RhdGVmb3JtLXN2YyJ9.TLDzvkW5YO3zWl3lhC3bZymmPdg0N-gvHbds5OymgPFdCjov4rTOmH1u5gwbuKZDL5P7BEIYXPdcF23jMmovCmv8voxGZmLMpvikejxNbZxJLGS57qmFIzVuT2JkCxkd86R5Dx1rt3cxWBxgmSQ3o8CVmjetmXFtG5f3cSIhvcsxqBqfo6JhGU5jHbivvEwU6oxv-ihUNGwUHbtIcfK7e4PC6T2lKh14AB6t2xpDYR2n8SM80EfVNPiMEVsGdAnQz86xP81ZbRUkcIB6C5D6czFxSMMztWaZihJl_DI1EaZbXqfNco4dY90LTyihZ1uS-RtK0pk7_wgqdinENnFi1g";
	}
	
	@BeforeAll
	public static void init() {
		newUser = Users.builder()
				.firstName("Narsi092401")
				.lastName("test")
				.email("Narsi092401@yopmail.com")
				.username("Narsi092401")
				.build();
		accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ5cHFuM2lZR0xDV1pKcl8yUlVpZmlqcEVzZWF3MUJBeHd5SnpqSk5neHFzIn0.eyJleHAiOjE3MjcyMzM1NjYsImlhdCI6MTcyNzIzMjA2NiwianRpIjoiMzFjY2MwNTQtMTUyOS00OTkwLWJmNzAtNTRhNmRlZmM5ZWI2IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9hY21hIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImZlZjQxMzA5LTk4MWMtNDY1MS1hMDRjLTIxN2VlNjI5N2ZmZCIsInR5cCI6IkJlYXJlciIsImF6cCI6InN0YXRlZm9ybS1zdmMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLWFjbWEiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjE3Mi4xOC4wLjEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtc3RhdGVmb3JtLXN2YyIsImNsaWVudEFkZHJlc3MiOiIxNzIuMTguMC4xIiwiY2xpZW50X2lkIjoic3RhdGVmb3JtLXN2YyJ9.lO7PgfKLFDlyTYnkvMRwKMM4z-IK6E11ZyWcllA7FrKmRzlJz90mHj_mvMVeIBTznzEQZHzxlKe9ym2tV35bEYXZ8owIGNF91TIjbfdkS6TQ-m8y_iOAocjNyKq3rJLwbsiklTUIZCWouwVWBdWdC48Htk_hxQpvDGQlmVwj6ZJFQ5SnBeS8Q2f3HGEOHUUFaOAhb6gW11E6lOR1mdJSnIb6iKEfFL7uNVYJOt9yNmpxiJjaVhE8h4Y7y-ajBR3kZUR7Zsexx4m21igiVIJEkPz-_ifJNZ8XJcLick-o8cjWfYIQNRboMo0OkwbqvqoaNWlLO5b5i4vFJPgPbHOdEQ";
		propertyOwnersGroupId= "f955f066-2a7b-4058-bf7f-9b48b43b38b5";
		acmaBrokersGroupId = "c307af52-8088-4686-a00a-5f4849dbbbf9";
		acmaAgentsGroupId = "f3ae4ba0-b8a8-4d70-8f0f-a8cdde4939cf";
		
	}

	@Autowired
	private AcmaUsersOutboundApi usersAPI;
	
	//@Test
	
	public void testGetAllUsers() throws RestClientException, URISyntaxException, JsonMappingException, JsonProcessingException {
		usersAPI.getAllUser(accessToken);
	}
	
	@Test
	@Order(1)
	public void testPropertyOwner() {
		String userName = newUser.getUsername();
		String email = newUser.getEmail();
		userName = userName+DateUtil.now().getTime();
		email = email+DateUtil.now().getTime();
		newUser.setUsername(userName);
		newUser.setEmail(email);
		newUser = usersAPI.createUser(newUser, accessToken);
		
		assertThat(newUser);
		assertThat(newUser.getUserId());
		assertThat(newUser.getGroupId());
		
	}
	
	@Test
	@Order(2)
	public void testCreateBrokerUsers() {
		String userName = newUser.getUsername();
		String email = newUser.getEmail();
		userName = userName+DateUtil.now().getTime();
		email = email+DateUtil.now().getTime();
		newUser.setUsername(userName);
		newUser.setEmail(email);
		newUser.setGroupId(acmaBrokersGroupId);
		newUser = usersAPI.createUser(newUser, accessToken);
		
		assertThat(newUser);
		assertThat(newUser.getUserId());
		assertThat(newUser.getGroupId());
		
	}
	
	@Test
	@Order(3)
	public void testCreateAgentUsers() {
		
		String userName = newUser.getUsername();
		String email = newUser.getEmail();
		userName = userName+DateUtil.now().getTime();
		email = email+DateUtil.now().getTime();
		newUser.setUsername(userName);
		newUser.setEmail(email);
		newUser.setGroupId(acmaAgentsGroupId);
		newUser = usersAPI.createUser(newUser, accessToken);
		
		assertThat(newUser);
		assertThat(newUser.getUserId());
		assertThat(newUser.getGroupId());
		
	}
	
	//@Test
	//@Order(2)
	public void testDeprovUser() {
		boolean isUserDep = usersAPI.DeProvisionUserUnderAGroup(newUser.getUserId(), newUser.getGroupId(), accessToken);
		assertThat(isUserDep);
		
	}
	
	@Test
	@Order(4)
	public void testGetAllPropertyOwners() throws JsonMappingException, RestClientException, JsonProcessingException, URISyntaxException {
		List<Users> propOwnersList =  usersAPI.getAllUsersOfAGroup(propertyOwnersGroupId,accessToken);
		assertThat(propOwnersList);
		Assertions.assertTrue(propOwnersList.size() >0);		
		
	}
	
	@Test
	@Order(5)
	public void testGetAllAgents() throws JsonMappingException, RestClientException, JsonProcessingException, URISyntaxException {
		List<Users> agentsList =  usersAPI.getAllUsersOfAGroup(acmaAgentsGroupId,accessToken);
		assertThat(agentsList);
		Assertions.assertTrue(agentsList.size() >0);		
		
	}
	
	@Test
	@Order(6)
	public void testGetAllBrokerUsers() throws JsonMappingException, RestClientException, JsonProcessingException, URISyntaxException {
		List<Users> brokersList =  usersAPI.getAllUsersOfAGroup(acmaBrokersGroupId,accessToken);
		assertThat(brokersList);
		Assertions.assertTrue(brokersList.size() >0);		
		
	}
}
