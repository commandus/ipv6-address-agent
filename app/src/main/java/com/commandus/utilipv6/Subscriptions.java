package com.commandus.utilipv6;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Subscriptions {
    private static final String TAG = Subscriptions.class.getSimpleName();
    public ArrayList<Subscription> subscriptions;
    public Subscriptions(JSONArray values) {
        reset();
        parse(values);
    }

    public Subscriptions() {
        reset();
    }

    public Subscriptions(String value) {
        reset();
        try {
            JSONArray v = new JSONArray(value);
            parse(v);
        } catch (JSONException e) {
        }
    }

    private void reset() {
        subscriptions = new ArrayList<>();
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (subscriptions.size() > 0) {
            b.append(subscriptions.get(0).toString());
            for (int i = 1; i < subscriptions.size(); i++) {
                b.append(", ").append(subscriptions.get(i).toString());
            }
        }
        return b.toString();
    }

    public void parse(JSONArray value) {
        for (int i = 0; i < value.length(); i++) {
            try {
                JSONObject js = value.getJSONObject(i);
                Subscription s = new Subscription(js);
                subscriptions.add(s);
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    public Subscription[] toArray() {
        return (Subscription[]) subscriptions.toArray();
    }
}
