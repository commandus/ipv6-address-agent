package com.commandus.utilipv6;

import org.json.JSONException;
import org.json.JSONObject;

public class Subscription {
    public String address;
    public String name;
    public String endpoint;
    public String publicKey;
    public String authSecret;

    public Subscription(JSONObject value) {
        parse(value);
    }

    public Subscription(String value) {
        try {
            JSONObject v = new JSONObject(value);
            parse(v);
        } catch (JSONException e) {
            reset();
        }
    }

    public Subscription() {
        reset();
    }

    public Subscription(String address, String name, String endpoint, String publicKey, String authSecret) {
        this.address = address;
        this.name = name;
        this.endpoint = endpoint;
        this.publicKey = publicKey;
        this.authSecret = authSecret;
    }

    public Subscription(String subscriptionToken, String authSecret) {
        this.address = "";
        this.name = "";
        this.publicKey = subscriptionToken; //?!!
        this.endpoint = "";
        this.authSecret = authSecret;
    }

    private void reset() {
        address = "0";
        name = "";
        endpoint = "";
        publicKey = "";
        authSecret = "";
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return "{"
                + "\"address\": " + address
                + ", \"name\": \"" + name
                + "\", \"endpoint\": \"" + endpoint
                + "\", \"publicKey\": \"" + publicKey
                + "\", \"authSecret\": \"" + authSecret
                + "\"}";
    }

    public void parse(JSONObject value) {
        if (value.has("address")) {
            try {
                address = value.getString("address");
            } catch (JSONException e) {
                address = "";
            }
        } else {
            address = "";
        }
        if (value.has("name")) {
            try {
                name = value.getString("name");
            } catch (JSONException e) {
                name = "";
            }
        } else {
            name = "";
        }

        if (value.has("endpoint")) {
            try {
                endpoint = value.getString("endpoint");
            } catch (JSONException e) {
                endpoint = "";
            }
        } else {
            endpoint = "";
        }

        if (value.has("publicKey")) {
            try {
                publicKey = value.getString("publicKey");
            } catch (JSONException e) {
                publicKey = "";
            }
        } else {
            publicKey = "";
        }
        if (value.has("authSecret")) {
            try {
                authSecret = value.getString("authSecret");
            } catch (JSONException e) {
                authSecret = "";
            }
        } else {
            authSecret = "";
        }
    }

    /**
     * @brief Endpoint contains public key (VAPID subscription token) of th subscription.
     * @return VAPID subscription token
     */
    public String getToken() {
        int p = endpoint.lastIndexOf('/');
        if (p > 0)
            return endpoint.substring(p);
        else
            return endpoint;
    }
}
