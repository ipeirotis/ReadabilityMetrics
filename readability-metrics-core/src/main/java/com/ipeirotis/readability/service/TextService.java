package com.ipeirotis.readability.service;

import org.springframework.stereotype.Component;

import com.googlecode.objectify.ObjectifyService;
import com.ipeirotis.readability.model.Text;
import com.ipeirotis.readability.model.User;

@Component
public class TextService extends CompositeEntityService<Text, User> {
	@Override
	public Iterable<Text> findAllByParentKey(String parentKey) {
		return ObjectifyService.ofy().load().type(Text.class).filter("owner =", parentKey).list();
	}
}
