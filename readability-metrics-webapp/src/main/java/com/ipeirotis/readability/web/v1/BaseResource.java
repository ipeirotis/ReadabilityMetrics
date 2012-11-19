package com.ipeirotis.readability.web.v1;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.googlecode.objectify.Key;
import com.ipeirotis.readability.model.User;
import com.ipeirotis.readability.service.UserService;

@Component
@Scope("request")
@Produces(MediaType.APPLICATION_JSON_VALUE)
abstract class BaseResource {
	@Context
	HttpHeaders requestHeaders;
	
	@Context
	User user;
	
	@Autowired
	UserService userService;
	
	Key<User> getOwner() {
		if (null == user)
			return null;
		
		return Key.create(User.class, user.getId());
	}
}
