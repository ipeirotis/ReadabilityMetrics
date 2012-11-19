package com.ipeirotis.readability.service;

import java.lang.reflect.ParameterizedType;

import com.googlecode.objectify.ObjectifyService;
import com.ipeirotis.readability.model.BaseEntity;

public class BaseEntityService<K extends BaseEntity> {
	final Class<K> clazz;
	
	@SuppressWarnings("unchecked")
	protected BaseEntityService() {
		this.clazz = (Class<K>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public Iterable<K> findAll() {
		return ObjectifyService.ofy().load().type(clazz).iterable();
	}
	
	public void save(Iterable<K> objects) {
		ObjectifyService.ofy().save().entities(objects).now();
	}
	
	public void save(K... objects) {
		ObjectifyService.ofy().save().entities((K[]) objects).now();
	}
	
	public void delete(K... objects) {
		ObjectifyService.ofy().delete().entities((Object[]) objects).now();
	}
	
	public K findFirstByPrimaryKey(String id) {
		return ObjectifyService.ofy().load().type(clazz).id(id).get();
	}
}
