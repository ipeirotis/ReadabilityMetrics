package com.ipeirotis.readability.model;

import org.apache.commons.codec.digest.DigestUtils;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity
public class Text extends BaseEntity {
	@Id
	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Unindex
	String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Text() {
	}

	@Index
	String owner;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Text(User owner, String content) {
		this.id = String.format("%s:%s", owner.getId(),
				DigestUtils.sha256Hex(content));
		this.content = content;
		this.owner = owner.getId();
	}
}
