package com.sagatechs.javaeeApp.dao.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.sagatechs.javaeeApp.model.base.BaseEntity;

public abstract class GenericDaoJpa<T extends BaseEntity, PK extends Serializable> {

	@PersistenceContext(unitName = "main-persistence-unit")
	protected EntityManager entityManager;

	protected Class<T> entityClass;
	protected Class<PK> entityPKClass;

	@SuppressWarnings("unchecked")
	public GenericDaoJpa() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
		this.entityPKClass = (Class<PK>) genericSuperclass.getActualTypeArguments()[1];
	}

	@SuppressWarnings("unused")
	private Class<PK> getPrimaryKeyClass() {

		return this.entityPKClass;
	}

	private Class<T> getEntityClass() {
		return entityClass;
	}
	/*
	 * public GenericDaoJpa(Class<T> entityClass, Class<PK> entityPKClass) {
	 * this.entityClass = entityClass; this.entityPKClass = entityPKClass; }
	 */

	protected EntityManager getEntityManager() {
		return this.entityManager;
	}

	public T persist(T t) {

		this.getEntityManager().persist(t);
		return t;
	}

	public T find(PK id) {
		return this.entityManager.find(entityClass, id);
	}

	public T update(T t) {
		return this.entityManager.merge(t);
	}

	public void delete(T t) {
		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
	}

	@SuppressWarnings("unchecked")
	public Set<T> findAll() {
		CriteriaQuery<T> criteria = getEntityManager().getCriteriaBuilder().createQuery(getEntityClass());
		criteria = criteria.select(criteria.from(getEntityClass()));
		return (Set<T>) getEntityManager().createQuery(criteria).getResultList();
	}

	public long countAll() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(getEntityClass())));
		return getEntityManager().createQuery(cq).getSingleResult().intValue();
	}

	public void removeAll() {
		String jpql = String.format("delete from %s", getEntityClass());
		Query query = getEntityManager().createQuery(jpql);
		query.executeUpdate();
	}
}
