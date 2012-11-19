package com.ipeirotis.readability.web.support;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ipeirotis.readability.model.User;
import com.ipeirotis.readability.service.UserService;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Component
@Provider
public class UserProvider extends AbstractHttpContextInjectable<User> implements InjectableProvider<Context, Type> {
	@Autowired
	UserService userService;
	
	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public Injectable<?> getInjectable(ComponentContext ic, Context a, Type c) {
		if ((c instanceof Class<?>) && User.class.isAssignableFrom((Class<?>) c))
			return this;
		
		return null;
	}

	@Override
	public User getValue(HttpContext c) {
		MultivaluedMap<String, String> headers = c.getRequest().getRequestHeaders();
		
		String userId = null;
		
		if (headers.containsKey("x-mashape-user-publickey")) {
			userId = headers.getFirst("x-mashape-user-publickey");
			
			User user = userService.findFirstByPrimaryKey(userId);
			
			if (null == user) {
				user = new User();
				user.setId(userId);
				
				userService.save(user);
			}
			
			return user;
		}
		
		return null;
	}
}
