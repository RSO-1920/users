package si.fri.rso.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javassist.NotFoundException;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.security.SecureRandom;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KeycloakBean {

    @Inject
    @DiscoverService(value = "rso1920-auth")
    private Optional<String> fileMetadataUrl;

    private static SecureRandom random = new SecureRandom();private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

    private String getMasterAccesToken() throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=admin&password=password&grant_type=password&client_id=admin-cli");
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/realms/master/protocol/openid-connect/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject Jobject = new JSONObject(response.body().string());
        return Jobject.get("access_token").toString();
    }

    public boolean deleteUser(String userId) throws IOException, NotFoundException {
        System.out.println("URL: " +ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users/"+userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users/"+userId)
                .delete()
                .addHeader("authorization", "Bearer " + getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();

        return response.isSuccessful();
    }

    public JSONArray getAllUsers() throws IOException {
        System.out.println("URL: " + ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users")
                .get()
                .addHeader("authorization", "Bearer " + getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();
        JSONArray Jarray = new JSONArray(response.body().string());
        return Jarray;
    }

    public JSONObject getUser(String username) throws IOException, NotFoundException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users?username="+username)
                .get()
                .addHeader("authorization", "Bearer " + getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();
        String responseBodyString = response.body().string();
        responseBodyString = responseBodyString.substring(1, responseBodyString.length() - 1);
        if(responseBodyString.length() == 0){
            throw new NotFoundException("Specified user NOT FOUND");
        }
        JSONObject Jobject = new JSONObject(responseBodyString);

        return Jobject;

        // return Jobject.get("id").toString();
    }

    public String createUser(String username, String firstname, String lastname, String password, String email) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t \"username\": \""+ username +"\",\r\n\t \"emailVerified\": false,\r\n\t \"firstName\": \""+ firstname +"\",\r\n\t \"lastName\": \""+ lastname +"\",\r\n\t \"credentials\": [\r\n\t     {\r\n\t         \"type\": \"password\",\r\n\t         \"value\": \""+ password +"\",\r\n\t         \"temporary\": false\r\n\t     }\r\n\t ],\r\n\t \"email\": \""+ email +"\",\r\n\t \"enabled\": true\r\n}");
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/admin/realms/customers-realm/users")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("authorization", "Bearer "+ getMasterAccesToken())
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string().toString();
    }

    public String getUserAuthenticationToken(String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username="+username+"&password="+password+"&grant_type=password&client_id=customers-app&scope=openid");
        Request request = new Request.Builder()
                .url(ConfigurationUtil.getInstance().get("kumuluzee.config.keycloak").get() + "/auth/realms/customers-realm/protocol/openid-connect/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        String accessToken = "Bearer " + (new JSONObject(response.body().string()).get("access_token")).toString();
        System.out.println(accessToken);
        return accessToken;
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

    public void sendSuccessfulLoginToAuth(JSONObject Jobject) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"id\": \""+ Jobject.get("id").toString() +"\",\r\n    \"authToken\": \""+ Jobject.get("authToken").toString() +"\"\r\n}");
        Request request = new Request.Builder()
                .url(fileMetadataUrl.get() + "/v1/auth")
                .method("POST", body)
                .addHeader("authorization", Jobject.get("authToken").toString())
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        //System.out.println(fileMetadataUrl.get());
    }

}
