package net.cloudburo.kyber.tutorial;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KyberAPI {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String KYBER_ROPSTEN = "https://ropsten-api.kyber.network/";

    private OkHttpClient client = new OkHttpClient();
    private String kyberNetwork;

    public KyberAPI(String kyberNetwork ) {
        this.kyberNetwork = kyberNetwork;
    }

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String getCurrencies() throws IOException {
        String url = this.kyberNetwork+"currencies";
        return this.run(url);
    }
}
