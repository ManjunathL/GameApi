package com.mygubbi.security;

import io.vertx.core.json.JsonObject;
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
    public static final String PASSWORD_KEY = "pswd";
    public static final String HASH_KEY = "hash";
    public static final String SALT_KEY = "salt";
    public static final String USER_ID_KEY = "user_id";

    private SecurityService() {
        matcher.setHashIterations(HASH_ITERATIONS);
        matcher.setStoredCredentialsHexEncoded(false);
    }

    public static SecurityService getInstance(){
        return instance;
    }

    public void secureCredentials(JsonObject userRegister) {
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String hashedPassword = new Sha256Hash(userRegister.getString(PASSWORD_KEY), salt, HASH_ITERATIONS).toBase64();
        userRegister.remove(PASSWORD_KEY);
        userRegister.put(HASH_KEY, hashedPassword);
        userRegister.put(SALT_KEY, salt);
    }

    public boolean authenticate(JsonObject paramsObject, JsonObject userObject) {
        String salt = userObject.getString(SALT_KEY);
        String hash = userObject.getString(HASH_KEY);
        String userId = userObject.getString(USER_ID_KEY);
        SimpleAuthenticationInfo account = new SimpleAuthenticationInfo(userId, hash, ByteSource.Util.bytes(salt), "myRealm");
        AuthenticationToken token = new UsernamePasswordToken(userId, paramsObject.getString(PASSWORD_KEY));
        return matcher.doCredentialsMatch(token, account);
    }
}
