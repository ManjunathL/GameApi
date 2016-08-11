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

        String username = "somename";
        String password = "somepassword";
        SecurityService.HashedCredentials hashedCredentials = SecurityService.getInstance().getHashedCredentials(password);
        boolean valid = SecurityService.getInstance().authenticate(username, password, hashedCredentials.getSalt(), hashedCredentials.getHashedPassword());

        Assert.assertTrue(valid);

    }
}
