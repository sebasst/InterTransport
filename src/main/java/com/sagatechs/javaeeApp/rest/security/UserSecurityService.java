package com.sagatechs.javaeeApp.rest.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jboss.logging.Logger;

import com.sagatechs.javaeeApp.exceptions.UnauthorizedException;
import com.sagatechs.javaeeApp.model.base.Status;
import com.sagatechs.javaeeApp.rest.security.model.RoleSecurity;
import com.sagatechs.javaeeApp.rest.security.model.UserSecurity;
import com.sagatechs.javaeeApp.rest.security.webModel.ChangePasswordRequest;
import com.sagatechs.javaeeApp.rest.security.webModel.Tokens;
import com.sagatechs.javaeeApp.services.general.EmailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Singleton
public class UserSecurityService {

	private static final Logger LOGGER = Logger.getLogger(UserSecurityService.class);
	public static final int EXPIRATION_TIME_SECONDS = 86400;// 6400;
	public static final int EXPIRATION_TIME_SECONDS_REFRESH = 86400 * 7;// 6400;

	public static final String SECRET_KEY = "xaE5cHuY4NCQm0v_BnsE93x3aa6tcRNUDJBKnHKUqhagrMIeTALKwkYHYPr77dBbPddJ5o207mWaF1ibL3zdDkDBv5MywlcPfu3_Awy2zDbCTDp6pZm-h245ZuC-ieVsDvBi3c1X15YEvmiqsE4BTKKQiHraIzT9kPwO2cqNJFfQPFMu_TWXeSpU14fLG5uFip2MltirPJLAeYS2kB4x--PLacTNo9Tb9zW3d0Il768xLOgPpdBqNkwUwLKrPtfXOl5mgXbv2l6G2k3z-JIysZJlRnDCTKp4R8Vvucp3i8p4e5UadenCT2Bl6qPMyYpXfS2j8jv08unn5xQiwkusiQ";

	SecretKey key = null;;

	private Map<String, UserSecurity> userCache = new HashMap<>();

	private Map<String, String> codeValidationCache = new HashMap<>();

	@Inject
	UserSecurityDao userSecurityDao;

	@Inject
	RoleSecurityDao roleSecurityDao;

	@Inject
	EmailService emailService;

	@Inject
	com.sagatechs.javaeeApp.utils.StringUtils stringUtils;

	@PostConstruct
	public void init() {

	}

	public UserSecurity createUser(UserSecurity user) {
		return this.userSecurityDao.persist(user);
	}
	
	public UserSecurity updateUser(UserSecurity user) {
		return this.userSecurityDao.update(user);
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
		 * Serializer serializer = new Serializer() {
		 * 
		 * @Override public byte[] serialize(Object t) throws SerializationException {
		 * Jsonb jsonb = JsonbBuilder.create(); String jsondata = jsonb.toJson(t);
		 * System.out.println(jsondata); return jsondata.getBytes(); } };
		 */
		Date now = new Date();
		String jws = Jwts.builder()
				// .serializeToJsonWith(serializer)// (1)
				.setSubject(username) // (2)
				.setIssuedAt(now).setExpiration(getExpirationDate(now, refreshToken)).claim("roles", roles)
				.claim("refreshToken", refreshToken).signWith(getSecretKey()).compact();// (4)
		return jws;
	}

	public String validateToken(String token) {
		if (StringUtils.isBlank(token)) {

			return null;
		}

		// obtengo el usuario
		Jws<Claims> jws = Jwts.parser() // (1)
				// .deserializeJsonWith(deserializer)
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

	public boolean authenticateUsernamePassword(String username, String password) {

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
		return user != null ? user.getPassword() : null;
	}

	private UserSecurity getTokensByUsernameFromRepository(String username) {
		return this.userSecurityDao.getByUsernameAndStatus(username, Status.ACTIVE);
	}

	public List<String> getRolesByUsernameFromRepository(String username) {
		List<RoleSecurity> resultado = this.roleSecurityDao.getByUsername(username, Status.ACTIVE);
		List<String> roles = new ArrayList<>();
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

	public Tokens refreshToken(String refreshToken) throws UnauthorizedException {
		if (StringUtils.isBlank(refreshToken)) {

			throw new UnauthorizedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
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

				throw new UnauthorizedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
			}

			Tokens generatedTokens = generateTokens(username, getRolesByUsernameFromRepository(username));

			renewTokens(username, generatedTokens);
			return generatedTokens;
		} catch (Exception e) {
			LOGGER.error("Error en validación de token");
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			throw new UnauthorizedException("Permiso denegado. Por favor vuelga a ingresar al sistema");
		}
	}

	protected String generateRandomCode() {

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = ThreadLocalRandom.current().nextInt(0, 1000);
		String pattern = "0000";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);

		return decimalFormat.format(randomNum);
	}

	public boolean verifyEmailRegistration(String email) {
		UserSecurity user = this.userSecurityDao.getByEmail(email);
		// si existe el usuario, devuelvo true
		if (user != null) {
			return true;
		} else {

			String code = this.generateRandomCode();
			this.codeValidationCache.put(email, code);
			this.sendVerificationCodeToEmail(email, code);
			return false;
		}
	}

	public void sendVerificationCodeToEmail(String email, String code) {
		String htmlMessage = "<p>Bienvenido a Saga:</p><p>&nbsp;</p><p>Tu c&oacute;digo de verificaci&oacute;n es: <b>"
				+ code + "</b></p>";
		this.emailService.sendEmailMessage(email, "Código de verificación", htmlMessage);
	}

	public boolean codeEmailVerification(String email, String code) {
		// busco en cache
		String savedCode = this.codeValidationCache.get(email);
		if (savedCode == null) {
			return false;
		} else {
			if (savedCode.equals(code)) {
				// quito del cache
				this.codeValidationCache.remove(email);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean changePasswordFromWsRequest(ChangePasswordRequest changePasswordRequest) {
		// verifico el codigo
		boolean codeVerification = this.codeEmailVerification(changePasswordRequest.getEmail(),
				changePasswordRequest.getCode());
		// si fue correcto, actualizo el password
		if (codeVerification) {
			// recupero por email
			UserSecurity user = this.userSecurityDao.getByEmail(changePasswordRequest.getEmail());

			// seteo nuevo password
			byte[] passwordEncp = this.stringUtils.encryptPassword(changePasswordRequest.getNewPassword());
			user.setPassword(passwordEncp);
			// persisto
			this.userSecurityDao.update(user);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Envio un correo sy ya est registrado
	 * 
	 * @param email
	 * @return
	 */
	public boolean changePasswordSendCode(String email) {
		UserSecurity user = this.userSecurityDao.getByEmail(email);
		// si existe el usuario, devuelvo true
		if (user == null) {
			return false;
		} else {

			String code = this.generateRandomCode();
			this.codeValidationCache.put(email, code);
			this.sendVerificationCodeToEmail(email, code);
			return true;
		}
	}
}
