package com.programdoo.transport.data.models.requests.accounts;

import com.google.gson.annotations.SerializedName;

public class TokenRequestModel {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public TokenRequestModel(
            String username,
            String password) {
        this.username = username;
        this.password = password;
    }
}
