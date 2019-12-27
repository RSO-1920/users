package si.fri.rso.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.security.SecureRandom;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class KeycloakBean {

    private static SecureRandom random = new SecureRandom();private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

    private String getMasterAccesToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=admin&password=password&grant_type=password&client_id=admin-cli");
        Request request = new Request.Builder()
                .url("http://localhost:8082/auth/realms/master/protocol/openid-connect/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject Jobject = new JSONObject(response.body().string());
        return Jobject.get("access_token").toString();
    }

    public JSONArray getAllUsers() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8082/auth/admin/realms/customers-realm/users")
                .get()
                .addHeader("authorization", "Bearer " + getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();
        JSONArray Jarray = new JSONArray(response.body().string());
        return Jarray;
    }

    public String createUser(String username, String firstname, String lastname, String password, String email) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t \"username\": \" "+ username +"\",\r\n\t \"emailVerified\": false,\r\n\t \"firstName\": \" "+ firstname +" \",\r\n\t \"lastName\": \""+ lastname +"\",\r\n\t \"credentials\": [\r\n\t     {\r\n\t         \"type\": \"password\",\r\n\t         \"value\": \""+ password +"*\",\r\n\t         \"temporary\": false\r\n\t     }\r\n\t ],\r\n\t \"email\": \""+ email +"\"\r\n}");
        Request request = new Request.Builder()
                .url("http://localhost:8082/auth/admin/realms/customers-realm/users")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("authorization", "Bearer "+ getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(response.toString());
        return response.toString();
    }

    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

}