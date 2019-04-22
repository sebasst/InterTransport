package com.sagatechs.javaeeApp.rest.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;
import com.sagatechs.javaeeApp.exceptions.UnauthorizedRefreshException;
import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.model.RoleSecurity;
import com.sagatechs.javaeeApp.rest.model.Tokens;
import com.sagatechs.javaeeApp.rest.model.UserSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Singleton
public class UserSecurityService {

	public static final int EXPIRATION_TIME_SECONDS = 86400;// 6400;
	public static final int EXPIRATION_TIME_SECONDS_REFRESH = 86400*7;// 6400;

	public static final String SECRET_KEY = "xaE5cHuY4NCQm0v_BnsE93x3aa6tcRNUDJBKnHKUqhagrMIeTALKwkYHYPr77dBbPddJ5o207mWaF1ibL3zdDkDBv5MywlcPfu3_Awy2zDbCTDp6pZm-h245ZuC-ieVsDvBi3c1X15YEvmiqsE4BTKKQiHraIzT9kPwO2cqNJFfQPFMu_TWXeSpU14fLG5uFip2MltirPJLAeYS2kB4x--PLacTNo9Tb9zW3d0Il768xLOgPpdBqNkwUwLKrPtfXOl5mgXbv2l6G2k3z-JIysZJlRnDCTKp4R8Vvucp3i8p4e5UadenCT2Bl6qPMyYpXfS2j8jv08unn5xQiwkusiQ";

	SecretKey key = null;;

	private Map<String, UserSecurity> userCache = new HashMap<>();

	@Inject
	UserSecurityDao userSecurityDao;

	@Inject
	RoleSecurityDao roleSecurityDao;
	
	
	@PostConstruct
	public void init() {

	}

	public SecretKey getSecretKey() {

		try {
			if (key == null) {
				key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes("UTF-8"));
			}
			return key;
		} catch (WeakKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Date getExpirationDate(Date date, boolean refreshToken) {
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(date); // sets calendar time/date
		if (refreshToken) {
			// cal.add(Calendar.MINUTE, 3);
			cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS_REFRESH);
			// return null;
		} else {
			cal.add(Calendar.SECOND, EXPIRATION_TIME_SECONDS); // adds one 24
		}

		return cal.getTime(); // returns new date object, one hour in the future
	}

	public String issueToken(String username, List<String> roles, boolean refreshToken) {
		System.out.println("dddd");
		/*
		Serializer serializer = new Serializer() {

			@Override
			public byte[] serialize(Object t) throws SerializationException {
				Jsonb jsonb = JsonbBuilder.create();
				String jsondata = jsonb.toJson(t);
				System.out.println(jsondata);
				return jsondata.getBytes();
			}
		};
		*/
		Date now = new Date();
		String jws = Jwts.builder()
				//.serializeToJsonWith(serializer)// (1)
				.setSubject(username) // (2)
				.setIssuedAt(now).setExpiration(getExpirationDate(now, refreshToken)).claim("roles", roles)
				.claim("refreshToken", refreshToken).signWith(getSecretKey()).compact();// (4)
		return jws;
	}

	public String validateToken(String token)  {
		if (StringUtils.isBlank(token)) {

			return null;
		}
		

		// obtengo el usuario
		Jws<Claims> jws = Jwts.parser() // (1)
				//.deserializeJsonWith(deserializer)
				.setSigningKey(getSecretKey()) // (2)
				.parseClaimsJws(token); // (3)

		// hasta ahi se valida el token
		String username = jws.getBody().getSubject();
		// veo si es de refresh
		Boolean isRefresh = (Boolean) jws.getBody().get("refreshToken");
		if (StringUtils.isBlank(username) || isRefresh == null) {

			return null;
		}

		// obtengo nuestro repartidor
		UserSecurity userSecurity = this.getTokensByUsername(username);
		String storedAccessToken = userSecurity != null ? userSecurity.getAccessToken() : null;

		if (isRefresh) {
			return null;

		} else {
			if (StringUtils.isEmpty(storedAccessToken) || !token.equals(storedAccessToken)) {
				return null;
			}
		}

		return username;

	}

	public Tokens authenticateRest(String username, String password) throws UnauthorizedException {

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new UnauthorizedException(
					"Acceso denegado. Por favor ingrese correctamente el nombre de usuario o contraseña. Si el problema persiste comuníquese con el administrador del sistema.");
		}

		// comparo passwords
		// obtengo pass en sha

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] pass = md.digest(password.getBytes(StandardCharsets.UTF_8));
			// genero el string para comparar
			/*
			 * StringBuilder sb = new StringBuilder(); for (int i = 0; i < bytes.length;
			 * i++) { sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,
			 * 16).substring(1)); }
			 */
			byte[] passwordEncrypted = this.getEncryptedPasswordFromRepository(username);
			if (Arrays.equals(pass, passwordEncrypted)) {

				List<String> roles = this.getRolesByUsernameFromRepository(username);
				Tokens generatedTokens = generateTokens(username, roles);

				renewTokens(username, generatedTokens);

				return generatedTokens;
			} else {
				throw new UnauthorizedException(
						"Acceso denegado. Por favor ingrese correctamente el nombre de usuario y contraseÃ±a.");
			}
		} catch (NoSuchAlgorithmException e) {
			throw new UnauthorizedException(
					"Acceso denegado. Por favor ingrese correctamente el nombre de usuario oy contraseÃ±a. Si el problema persiste comunÃ­quese con el administrador del sistema.");
		}

	}
	
	public boolean authenticateUsernamePassword(String username, String password)  {

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return false;
		}

		// comparo passwords
		// obtengo pass en sha

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] pass = md.digest(password.getBytes(StandardCharsets.UTF_8));
			// genero el string para comparar
			/*
			 * StringBuilder sb = new StringBuilder(); for (int i = 0; i < bytes.length;
			 * i++) { sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,
			 * 16).substring(1)); }
			 */
			byte[] passwordEncrypted = this.getEncryptedPasswordFromRepository(username);
			if (Arrays.equals(pass, passwordEncrypted)) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			return false;

		}

	}

	private Tokens generateTokens(String username, List<String> roles) {
		Tokens generatedTokens = new Tokens();
		generatedTokens.setAccess_token(issueToken(username, roles, false));
		generatedTokens.setRefresh_token(issueToken(username, roles, true));
		generatedTokens.setExpires_in(EXPIRATION_TIME_SECONDS);
		generatedTokens.setUsername(username);

		return generatedTokens;
	}

	private UserSecurity getTokensByUsername(String username) {
		UserSecurity userSecurity = this.userCache.get(username);
		if (userSecurity == null) {
			userSecurity = this.getTokensByUsernameFromRepository(username);
		}
		return userSecurity;
	}

	private void renewTokens(String username, Tokens generatedTokens) {
		UserSecurity userSecurity = this.getTokensByUsername(username);
		if (userSecurity == null) {
			throw new NotFoundException("No se encontró el usuario con nombre de usuario: " + username);
		}

		userSecurity.setAccessToken(generatedTokens.getAccess_token());
		userSecurity.setRefreshToken(generatedTokens.getRefresh_token());

		this.userCache.put(username, userSecurity);

		this.persistTokensInRepository(userSecurity);
	}

	private byte[] getEncryptedPasswordFromRepository(String username) {
		UserSecurity user = this.userSecurityDao.getByUsernameAndStatus(username, Status.ACTIVE);
		return user!=null?user.getPassword():null;
	}

	private UserSecurity getTokensByUsernameFromRepository(String username) {
		return this.userSecurityDao.getByUsernameAndStatus(username, Status.ACTIVE);
	}

	public List<String> getRolesByUsernameFromRepository(String username) {
		List<RoleSecurity> resultado = this.roleSecurityDao.getByUsername(username, Status.ACTIVE);
		List<String> roles= new ArrayList<>();
		for (RoleSecurity roleSecurity : resultado) {
			roles.add(roleSecurity.getName().name());
		}
		
		return roles;
	}

	private void persistTokensInRepository(UserSecurity userSecurity) {
		if (userSecurity.getId() == null) {
			this.userSecurityDao.persist(userSecurity);
		} else {
			this.userSecurityDao.update(userSecurity);
		}
	}
	public void logout(String username) {
		Tokens tokens = new Tokens();
		renewTokens(username, tokens);
	}
	
	public Tokens refreshToken(String refreshToken) throws UnauthorizedException, UnauthorizedRefreshException {
		if (StringUtils.isBlank(refreshToken)) {

			throw new UnauthorizedRefreshException("Permiso denegado. Por favor vuelga a ingresar al sistema");
		}
		// obtengo el usuario

		Jws<Claims> jws;
		try {
			jws = Jwts.parser() // (1)
					.setSigningKey(getSecretKey()) // (2)
					.parseClaimsJws(refreshToken);

			// hasta ahi se valida el token
			String username = jws.getBody().getSubject();
			// veo si es de refresh
			Boolean isRefresh = (Boolean) jws.getBody().get("refreshToken");
			if (StringUtils.isBlank(username) || isRefresh == null || !isRefresh) {

				throw new UnauthorizedRefreshException("Permiso denegado. Por favor vuelga a ingresar al sistema");
			}

			Tokens generatedTokens = generateTokens(username, getRolesByUsernameFromRepository(username));

			renewTokens(username, generatedTokens);
			return generatedTokens;
		} catch (Exception e) {
			throw new UnauthorizedRefreshException("Permiso denegado. Por favor vuelga a ingresar al sistema");
		}
	}
}
