package scripts.api.data_tracker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.tribot.api.General;
import scripts.api.util.functions.Logging;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataTracker {

    public static final int UPDATE_DELAY = 5000;

    private static final String HMAC_SHA256 = "HmacSHA256";

    private String apiSecret;
    private HttpUrl apiUrl;

    private boolean running;
    private String sessionId = null;

    private OkHttpClient httpClient;
    private Gson gson;
    private JsonParser jsonParser;
    public JsonObject data;
    private ExecutorService executorService;

    public DataTracker(String apiUrl, String apiSecret, String scriptName) {
        this.apiUrl = HttpUrl.parse(apiUrl).newBuilder()
                .addPathSegment("scripts/api")
                .addPathSegment("scripts")
                .addPathSegment(scriptName)
                .build();
        this.apiSecret = apiSecret;

        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
        this.jsonParser = new JsonParser();
        this.data = new JsonObject();
        this.executorService = Executors.newSingleThreadExecutor();

        this.data.addProperty("username", General.getTRiBotUsername());
    }

    public void trackNumber(String name, Number value) {
        this.data.addProperty(name, value);
    }

    public void trackString(String name, String value) {
        this.data.addProperty(name, value);
    }

    public void trackBool(String name, boolean value) {
        this.data.addProperty(name, value);
    }

    private void trackData() {
        if (sessionId == null && !createSession())
            return;

        var jsonBody = data.deepCopy();

        jsonBody.addProperty("hmac", hmac());

        var request = new Request.Builder()
                .url(apiUrl.newBuilder().addPathSegment(this.sessionId).build())
                .put(bodyFromJson(jsonBody))
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (response.code() != 200)
                Logging.debug("Tracking data failed");

            Logging.debug("Tracked Script data");
        } catch (IOException e) {
            e.printStackTrace();

            this.stop();
        }
    }

    private boolean createSession() {
        var jsonBody = data.deepCopy();

        jsonBody.addProperty("hmac", hmac());

        var request = new Request.Builder()
                .url(apiUrl)
                .post(bodyFromJson(jsonBody))
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (response.code() != 200)
                this.stop();

            try (var body = response.body()) {
                var json = body != null ? jsonParser.parse(body.string()).getAsJsonObject() : null;

                if (json != null && json.has("id")) {
                    this.sessionId = json.get("id").getAsString();

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            this.stop();
        }

        return false;
    }


    private RequestBody bodyFromJson(JsonObject body) {
        return RequestBody.create(body.toString(), MediaType.parse("application/json"));
    }

    public String hmac() {
        try {
            var mac = Mac.getInstance(HMAC_SHA256);
            var secretKeySpec = new SecretKeySpec(this.apiSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);

            mac.init(secretKeySpec);

            var hmac = mac.doFinal(this.data.toString().getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();

            this.stop();
        }
        return null;
    }

    public void start() {
        this.running = true;

        executorService.submit(() -> {
            try {
                while (running) {
                    this.trackData();

                    Thread.sleep(UPDATE_DELAY);
                }
            } catch (InterruptedException e) {

            }
        });
    }

    public void stop() {
        this.running = false;

        executorService.shutdown();
    }
}

