package com.ipeirotis.readability.service;

import java.lang.reflect.ParameterizedType;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.ipeirotis.readability.model.BaseEntity;

public class CompositeEntityService<K extends BaseEntity, P extends BaseEntity>
		extends BaseEntityService<K> {
	final Class<P> parentClazz;

	@SuppressWarnings("unchecked")
	public CompositeEntityService() {
		this.parentClazz = (Class<P>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@SuppressWarnings("unchecked")
	public Iterable<K> findAllByPrimaryKeys(String parentKey, String entityKey) {
		Key<P> parentKeyRef = getParentKeyRef(parentKey);
		Object result = ObjectifyService.ofy().load().ancestor(parentKeyRef)
				.filterKey(entityKey).iterable();

		return (Iterable<K>) result;
	}

	public K findFirstByPrimaryKeys(String parentKey, String entityKey) {
		Key<P> parentKeyRef = getParentKeyRef(parentKey);

		return (K) ObjectifyService.ofy().load().type(clazz)
				.filterKey(Key.create(parentKeyRef, parentClazz, entityKey))
				.first().get();
	}

	@SuppressWarnings("unchecked")
	public Iterable<K> findAllByParentKey(String parentKey) {
		Object obj = ObjectifyService.ofy().load().type(clazz).filter("owner =", parentKey).list();

		return (Iterable<K>) obj;
	}

	protected Key<P> getParentKeyRef(String parentKey) {
		Key<P> parentKeyRef = Key.create(parentClazz, parentKey);

		return parentKeyRef;
	}

}
