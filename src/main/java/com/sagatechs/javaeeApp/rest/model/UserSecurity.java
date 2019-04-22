package com.sagatechs.javaeeApp.rest.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import com.sagatechs.javaeeApp.model.base.BaseEntity;
import com.sagatechs.javaeeApp.model.base.Status;

@Entity
@Table(name = "user", schema = "security", indexes = { @Index(name = "user_username_index", columnList = "username") })
@NamedQuery(name = UserSecurity.QUERY_FIND_BY_USERNAME_AND_STATUS, query = "SELECT o FROM UserSecurity o WHERE o.username = :"
		+ UserSecurity.QUERY_PARAM_USERNAME + " AND o.status = :" + UserSecurity.QUERY_PARAM_STATUS)
@NamedQuery(name = UserSecurity.QUERY_FIND_BY_USERNAME, query = "SELECT o FROM UserSecurity o WHERE o.username = :"
		+ UserSecurity.QUERY_PARAM_USERNAME )
public class UserSecurity extends BaseEntity<Long> {

	public static final String QUERY_FIND_BY_USERNAME_AND_STATUS = "UserSecurity.FindByUsernameAndStatus";
	public static final String QUERY_FIND_BY_USERNAME = "UserSecurity.FindByUsername";

	public static final String QUERY_PARAM_USERNAME = "username";
	public static final String QUERY_PARAM_STATUS = "status";

	
	
	public UserSecurity() {
		super();
	}
	
	

	public UserSecurity(@NotEmpty(message = "El nombre de usuario es un dato obligatorio") String username,
			byte[] password, Status status) {
		super();
		this.username = username;
		this.password = password;
		this.status = status;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "El nombre de usuario es un dato obligatorio")
	@Column(name = "username", nullable = false, length = 50, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private byte[] password;

	@Column(name = "access_token", nullable = true)
	private String accessToken;

	@Column(name = "refresh_token", nullable = true)
	private String refreshToken;

	@Column(name = "creation_token_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTokenDate;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToMany(fetch =  FetchType.LAZY)
    @JoinTable(name = "user_role", schema = "security",
           joinColumns = { @JoinColumn(name = "fk_role") },
           inverseJoinColumns = { @JoinColumn(name = "fk_user") })
    private Set<RoleSecurity> roles = new HashSet<RoleSecurity>();
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getCreationTokenDate() {
		return creationTokenDate;
	}

	public void setCreationTokenDate(Date creationTokenDate) {
		this.creationTokenDate = creationTokenDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<RoleSecurity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleSecurity> roles) {
		this.roles = roles;
	}

	
	
}
