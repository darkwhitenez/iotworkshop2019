package worskhop.iot.symdroid.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import worskhop.iot.symdroid.models.CoreResponse;
import worskhop.iot.symdroid.models.Observation;
import worskhop.iot.symdroid.models.Search;
import worskhop.iot.symdroid.models.SymbioteResource;
import worskhop.iot.symdroid.settings.Settings;
import worskhop.iot.symdroid.utils.ApiException;


class NetworkUtil {

    private static NetworkUtil INSTANCE;
    private String securityRequest;


    static NetworkUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkUtil();
        }
        return INSTANCE;
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.1-Security#311-getting-security-headers-for-guest-users
    private static String getGuestToken(Context context) throws ApiException {
        try {
            URL url = new URL(Settings.getCoreAAm(context) + "aam/get_guest_token");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.connect();

            return con.getHeaderField("x-auth-token");
        } catch (java.net.SocketTimeoutException e) {
            throw new ApiException("Read Timeout", e);
        } catch (Exception e) {
            throw new ApiException("Error while fetching the guest token from API", e);
        }
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.1-Security#3112-create-security-request
    private static String createSecurityRequest(String guestToken) {
        return "{"
                + "\"token\":\"" + guestToken + "\","
                + "\"authenticationChallenge\":\"\","
                + "\"clientCertificate\":\"\","
                + "\"clientCertificateSigningAAMCertificate\":\"\","
                + "\"foreignTokenIssuingAAMCertificate\":\"\""
                + "}";
    }

    private static void setSecurityHeaders(String securityRequest, HttpURLConnection con) {
        con.setRequestProperty("x-auth-timestamp", Long.toString(System.currentTimeMillis()));
        con.setRequestProperty("x-auth-1", securityRequest);
        con.setRequestProperty("x-auth-size", "1");
    }

    private static Reader readAndLog(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String temp;
        StringBuilder sb = new StringBuilder();
        while ((temp = in.readLine()) != null) {
            sb.append(temp).append(" ");
        }
        String result = sb.toString();

        return new StringReader(result);
    }

    void createSecurityRequest(Context context) throws ApiException {
        String guestToken = getGuestToken(context);
        this.securityRequest = createSecurityRequest(guestToken);
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.4-Accessing-the-resource-and-actuating-and-invoking-service-for-default-(dummy)-resources#341-reading-current-value
    void sendCommandToActuator(Context context, String resourceURL, String command) throws ApiException {
        try {

            URL url = new URL(resourceURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            setSecurityHeaders(securityRequest, con);
            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            out.write(command);
            out.flush();
            out.close();
        } catch (java.net.SocketTimeoutException e) {
            throw new ApiException("Read Timeout", e);
        } catch (Exception e) {
            throw new ApiException("Can't send command to actuator at the moment", e);
        }
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.2-Search-for-resources
    List<SymbioteResource> findResources(Context context, Search query) throws ApiException {
        try {
            URL url = new URL(Settings.getCoreAAm(context) + "query?" + query.getQueryString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(Settings.getMaxTimeout(context));
            setSecurityHeaders(securityRequest, con);

            con.connect();

            Gson gson = new Gson();
            Reader reader = readAndLog(con.getInputStream());
            CoreResponse<List<SymbioteResource>> response = gson.fromJson(reader,
                    new TypeToken<CoreResponse<List<SymbioteResource>>>() {
                    }.getType());
            return response.getBody();
        } catch (java.net.SocketTimeoutException e) {
            throw new ApiException("Read Timeout", e);
        } catch (Exception e) {
            throw new ApiException("Error while fetching data from API", e);
        }
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.3-Obtaining-resource-access-URL
    String getResourceUrl(Context context, String id) throws ApiException {
        try {
            URL url = new URL(Settings.getCoreAAm(context) + "resourceUrls?id=" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            setSecurityHeaders(securityRequest, con);
            con.connect();

            Gson gson = new Gson();
            Reader reader = readAndLog(con.getInputStream());
            CoreResponse<Map<String, String>> response = gson.fromJson(reader, new TypeToken<CoreResponse<Map<String, String>>>() {
            }.getType());

            return response.getBody().get(id);
        } catch (java.net.SocketTimeoutException e) {
            throw new ApiException("Read Timeout", e);
        } catch (Exception e) {
            throw new ApiException("Error while fetching the resource URL", e);
        }
    }

    // https://github.com/symbiote-h2020/SymbioteCloud/wiki/3.4-Accessing-the-resource-and-actuating-and-invoking-service-for-default-(dummy)-resources#341-reading-current-value
    List<Observation> getReadCurrentValue(String resourceURL, Context context) throws ApiException {
        try {
            int top = Settings.getMaxDataItemsPerReq(context);
            URL url = new URL(resourceURL + "/Observations?$top=" + top);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(Settings.getMaxTimeout(context));
            setSecurityHeaders(securityRequest, con);
            con.setRequestProperty("Accept", "application/json");

            con.connect();

            Gson gson = new Gson();
            Reader reader = readAndLog(con.getInputStream());

            return gson.fromJson(reader, new TypeToken<List<Observation>>() {
            }.getType());
        } catch (java.net.SocketTimeoutException e) {
            throw new ApiException("Read Timeout", e);
        } catch (Exception e) {
            throw new ApiException("Resource data not available", e);
        }
    }
}
