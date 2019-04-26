package com.sagatechs.javaeeApp.rest.security.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sagatechs.javaeeApp.model.base.BaseEntity;
import com.sagatechs.javaeeApp.model.base.Status;

@Entity
@Table(name = "role", schema = "security")
@NamedQuery(name = RoleSecurity.QUERY_FIND_BY_USERNAME_AND_STATUS, query = "SELECT DISTINCT o FROM RoleSecurity o INNER JOIN o.users u WHERE u.username = :"
		+ RoleSecurity.QUERY_PARAM_USERNAME + " AND o.status = :" + RoleSecurity.QUERY_PARAM_STATUS)
@NamedQuery(name = RoleSecurity.QUERY_FIND_BY_NAME, query = "SELECT o FROM RoleSecurity o WHERE o.name= :"
		+ RoleSecurity.QUERY_PARAM_NAME )
public class RoleSecurity extends BaseEntity<Long> {

	public static final String QUERY_FIND_BY_USERNAME_AND_STATUS = "RoleSecurity.FindByUsernameAndStatus";
	public static final String QUERY_FIND_BY_NAME= "RoleSecurity.FindByName";

	
	public static final String QUERY_PARAM_USERNAME = "username";
	public static final String QUERY_PARAM_STATUS = "status";
	public static final String QUERY_PARAM_NAME = "name";
	
	

	public RoleSecurity() {
		super();
	}
	
	

	public RoleSecurity(@NotNull(message = "El estado es obligatorio.") Status status,
			@NotEmpty(message = "El nombre es obligatorio.") Role name, String description) {
		super();
		this.status = status;
		this.name = name;
		this.description = description;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "El estado es obligatorio.")
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@NotNull(message = "El nombre es obligatorio.")
	@Column(name = "name", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private Role name;

	@Column(name = "description", nullable = true)
	private String description;

	@ManyToMany(mappedBy = "roles")
	private Set<UserSecurity> users = new HashSet<UserSecurity>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Role getName() {
		return name;
	}

	public void setName(Role name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<UserSecurity> getUsers() {
		return users;
	}

	public void setUsers(Set<UserSecurity> users) {
		this.users = users;
	}

}
