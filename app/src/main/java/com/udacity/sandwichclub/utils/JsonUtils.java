package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONObject name = obj.getJSONObject("name");
            return new Sandwich(
                    name.optString("mainName"),
                    GetStringList(name, "alsoKnownAs"),
                    obj.optString("placeOfOrigin"),
                    obj.optString("description"),
                    obj.optString("image"),
                    GetStringList(obj, "ingredients"));
        } catch(Throwable t) {
            return null;
        }
    }

    private static List<String> GetStringList(JSONObject obj, String name) {
        try {
            JSONArray jsonArray = obj.getJSONArray(name);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.optString(i));
            }
            return list;
        } catch(JSONException e) {
            return new ArrayList<>();
        }
    }
}
