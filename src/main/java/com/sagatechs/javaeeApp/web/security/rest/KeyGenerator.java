package com.sagatechs.javaeeApp.web.security.rest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.sagatechs.adminfaces.starter.exception.SystemException;

public class KeyGenerator {

    private static final String SECRET_KEY = "secret";
    private static final String SHA_256 = "SHA-256";

    public String generateKey() {
        return getHashText(SECRET_KEY);
    }

    protected String getHashText(final String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
            byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(hash);

            return encoded;
        } catch (NoSuchAlgorithmException ex) {
            throw new SystemException();
        }
    }
}