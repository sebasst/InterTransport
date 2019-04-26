package com.sagatechs.javaeeApp.model.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sagatechs.javaeeApp.model.base.BaseEntity;
import com.sagatechs.javaeeApp.rest.security.model.UserSecurity;

@Entity
@Table(name = "passenger", schema = "app")
@NamedQuery(name =Passenger.QUERY_FIND_BY_EMAIL, query = "SELECT o FROM Passenger o INNER JOIN FETCH o.user u WHERE u.email = :" +Passenger.QUERY_PARAM_EMAIL )
@NamedQuery(name =Passenger.QUERY_FIND_BY_USERNAME, query = "SELECT o FROM Passenger o INNER JOIN FETCH o.user u WHERE u.username = :" +Passenger.QUERY_PARAM_USERNAME )
public class Passenger extends BaseEntity<Long>{
	
	public static final String QUERY_FIND_BY_USERNAME = "Passenger.FindByUsername";
	public static final String QUERY_FIND_BY_EMAIL = "Passenger.FindByEmail";
	
	public static final String QUERY_PARAM_USERNAME = "username";
	public static final String QUERY_PARAM_STATUS = "status";
	public static final String QUERY_PARAM_EMAIL = "email";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "id_document", nullable = true, length = 50, unique = false)
	private String idDocument;
	
	@Column(name = "first_name", nullable = true, length = 50, unique = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = true, length = 50, unique = false)
	private String lastName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_security_id", foreignKey =@ForeignKey(name="fk_passenger_user_security"),nullable = false, unique = true)
	UserSecurity user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lasttName) {
		this.lastName = lasttName;
	}

	public UserSecurity getUser() {
		return user;
	}

	public void setUser(UserSecurity user) {
		this.user = user;
	}

	
	
}
