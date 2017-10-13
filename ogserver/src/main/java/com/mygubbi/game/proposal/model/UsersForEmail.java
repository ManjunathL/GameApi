package com.mygubbi.game.proposal.model;

import io.vertx.core.json.JsonObject;

/**
 * Created by shilpa on 12/10/17.
 */
public class UsersForEmail extends JsonObject {
    private static final String NAME =  "name";
    private static final String EMAIL ="email";
    private static final String ROLE_FOR_EMAIL ="roleForEmail";
    private static final String REGION ="region";

    public UsersForEmail(){

    }

    public UsersForEmail(JsonObject json)
    {
        super(json.getMap());
    }

    public String getName() {
        return this.getString(NAME);
    }

    public  String getEmail() {
        return this.getString(EMAIL);
    }

    public  String getRoleForEmail() {
        return this.getString(ROLE_FOR_EMAIL);
    }

    public  String getRegion() {
        return this.getString(REGION);
    }

    public UsersForEmail setName(String name){
        put(NAME,name);
        return this;
    }
    public UsersForEmail setEmail(String email){
        put(EMAIL,email);
        return this;
    }
    public UsersForEmail setRoleForEmail(String role){
        put(ROLE_FOR_EMAIL,role);
        return this;
    }
    public UsersForEmail setRegion(String region){
        put(REGION,region);
        return this;
    }
}


