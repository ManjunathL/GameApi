package com.mygubbi;

import com.mygubbi.security.SecurityService;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nitinpuri on 29-11-2015.
 */
public class SecurityServiceTest {

    @Test
    public void testAuthentication() {

        JsonObject registrationParams = new JsonObject();
        registrationParams.put("user_id", "somename");
        registrationParams.put("pswd", "somepassword");

        SecurityService.getInstance().secureCredentials(registrationParams);

        Assert.assertFalse(registrationParams.containsKey(SecurityService.PASSWORD_KEY));
        Assert.assertTrue(registrationParams.containsKey(SecurityService.HASH_KEY));
        Assert.assertTrue(registrationParams.containsKey(SecurityService.SALT_KEY));
        Assert.assertNotEquals("somepassword", registrationParams.getString(SecurityService.HASH_KEY));

        JsonObject userObject = new JsonObject();
        userObject.put("user_id", "somename");
        userObject.put("hash", registrationParams.getString("hash"));
        userObject.put("salt", registrationParams.getString("salt"));

        JsonObject loginParams = new JsonObject();
        loginParams.put("user_id", "somename");
        loginParams.put("pswd", "somepassword");

        boolean valid = SecurityService.getInstance().authenticate(loginParams, userObject);

        Assert.assertTrue(valid);

    }
}
