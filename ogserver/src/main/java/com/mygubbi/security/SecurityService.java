package com.mygubbi.security;

import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by nitinpuri on 29-11-2015.
 */
public class SecurityService {

    private static final SecurityService instance = new SecurityService();
    private final HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
    private final int HASH_ITERATIONS = 1024;

    private SecurityService() {
        matcher.setHashIterations(HASH_ITERATIONS);
        matcher.setStoredCredentialsHexEncoded(false);
    }

    public static SecurityService getInstance(){
        return instance;
    }

    public HashedCredentials getHashedCredentials(String password) {
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String hashedPassword = new Sha256Hash(password, salt, HASH_ITERATIONS).toBase64();
        return new HashedCredentials(salt, hashedPassword);
    }

    public boolean authenticate(String userId, String password, String salt, String hash) {
        SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(userId, hash, ByteSource.Util.bytes(salt), "myRealm");
        AuthenticationToken token = new UsernamePasswordToken(userId, password);
        return matcher.doCredentialsMatch(token, account);
    }

    public static class HashedCredentials {
        private String salt;
        private String hashedPassword;

        public HashedCredentials(String salt, String hashedPassword) {
            this.salt = salt;
            this.hashedPassword = hashedPassword;
        }

        public String getSalt() {
            return salt;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }
    }
}
