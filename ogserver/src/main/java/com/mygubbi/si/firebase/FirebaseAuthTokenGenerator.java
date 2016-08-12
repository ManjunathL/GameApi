package com.mygubbi.si.firebase;

import com.firebase.security.token.TokenGenerator;
import com.firebase.security.token.TokenOptions;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nitinpuri on 12-08-2016.
 */
public class FirebaseAuthTokenGenerator {

    public static void main(String[] args) throws IOException {
        long years = 500;
        long timeAfterXYears = System.currentTimeMillis() + years * 31536000000L; //(365 * 24 * 60 * 60 * 1000)
        Date expires = new Date(timeAfterXYears);
        System.out.println("date expires - " + expires);

        // Put uid and firebase secret token.
        // e.g. create a user for crm and pass its uid here
        // the token can then be used for REST interaction with Firebase directly
        // e.g. GET of https://mygubbi.firebaseio.com/test.json?auth=<token> will get the test node
        // On the other hand, in firebase rules, test node can be guarded to be accessible only by uid specified here
        String token = new FirebaseAuthTokenGenerator().generateToken(expires, "<uid>", "<secret>");
        System.out.println("token - " + token);
    }

    private String generateToken(Date expires, String uid, String secret) {
        TokenOptions tokenOptions = new TokenOptions();
        tokenOptions.setExpires(expires);

        Map<String, Object> authPayload = new HashMap<>();
        authPayload.put("uid", uid);

        TokenGenerator tokenGenerator = new TokenGenerator(secret);
        return tokenGenerator.createToken(authPayload, tokenOptions);
    }

}
