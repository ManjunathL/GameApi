package com.mygubbi.route;

import com.mygubbi.db.QueryData;
import com.mygubbi.security.SecurityService;
import io.vertx.core.json.JsonObject;

/**
 * Created by nitinpuri on 25-04-2016.
 */
public class UserHandlerUtil {

    public static final String HASH_KEY = "hash";
    public static final String SALT_KEY = "salt";
    public static final String USER_ID_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String OLD_PASSWORD_KEY = "old_password";
    public static final String NEW_PASSWORD_KEY = "new_password";

    public static void secureCredentials(JsonObject paramsObject) {
        secureCredentials(paramsObject, PASSWORD_KEY);
    }

    public static boolean authenticate(JsonObject paramsObject, QueryData selectData) {
        return authenticate(paramsObject, selectData, PASSWORD_KEY);
    }

    public static boolean authenticate(JsonObject paramsObject, QueryData selectData, String passwordKey) {
        return SecurityService.getInstance().authenticate(
                selectData.rows.get(0).getString(USER_ID_KEY),
                paramsObject.getString(passwordKey),
                selectData.rows.get(0).getString(SALT_KEY),
                selectData.rows.get(0).getString(HASH_KEY));
    }

    public static void secureCredentials(JsonObject paramsObject, String passwordKey) {
        SecurityService.HashedCredentials hashedCredentials = SecurityService.getInstance().getHashedCredentials((String) paramsObject.remove(passwordKey));
        paramsObject.put(HASH_KEY, hashedCredentials.getHashedPassword());
        paramsObject.put(SALT_KEY, hashedCredentials.getSalt());

    }
}
