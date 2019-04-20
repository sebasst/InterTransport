package com.sagatechs.javaeeApp.model.base;

import java.io.Serializable;

public abstract class BaseEntity<PK extends Serializable> {

	/**
	 * This method should return the primary key.
	 * 
	 * @return
	 */
	public abstract PK getId();


}