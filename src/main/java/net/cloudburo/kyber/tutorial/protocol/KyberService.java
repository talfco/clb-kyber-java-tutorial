package net.cloudburo.kyber.tutorial.protocol;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Service;
import org.web3j.protocol.exceptions.ClientConnectionException;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KyberService extends Service {

    public static final String KYBER_ROPSTEN = "https://ropsten-api.kyber.network/";

    public static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    private static final Logger log = LoggerFactory.getLogger(KyberService.class);

    private OkHttpClient httpClient;

    private final String url;

    private final boolean includeRawResponse;

    private HashMap<String, String> headers = new HashMap<>();
    public static final String DEFAULT_URL = "http://localhost:8545/";

    public KyberService(String url, OkHttpClient httpClient, boolean includeRawResponses) {
        super(includeRawResponses);
        this.url = url;
        this.httpClient = httpClient;
        this.includeRawResponse = includeRawResponses;
    }

    public KyberService(OkHttpClient httpClient, boolean includeRawResponses) {
        this(DEFAULT_URL, httpClient, includeRawResponses);
    }

    public KyberService(String url, OkHttpClient httpClient) {
        this(url, httpClient, false);
    }

    public KyberService(String url) {
        this(url, createOkHttpClient());
    }

    public KyberService(String url, boolean includeRawResponse) {
        this(url, createOkHttpClient(), includeRawResponse);
    }

    public KyberService(OkHttpClient httpClient) {
        this(DEFAULT_URL, httpClient);
    }

    public KyberService(boolean includeRawResponse) {
        this(DEFAULT_URL, includeRawResponse);
    }

    public KyberService() {
        this(DEFAULT_URL);
    }

    private static OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //        .connectionSpecs(CONNECTION_SPEC_LIST);
        configureLogging(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    @Override
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {
        String payload = objectMapper.writeValueAsString(request);

        try (InputStream result = performIO(request)) {
            if (result != null) {
                return objectMapper.readValue(result, responseType);
            } else {
                return null;
            }
        }
    }

    @Override
    protected InputStream performIO(String request) throws IOException {return null;}

    protected InputStream performIO(org.web3j.protocol.core.Request request) throws IOException {

        //RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
        Headers headers = buildHeaders();

        //okhttp3.Request httpRequest = new okhttp3.Request.Builder()
        //        .url(url)
        //        .headers(headers)
        //        .post(requestBody)
        //        .build();

        List<Param> params =  request.getParams();
        String op = params.get(0).getValue();
        String qry = "";
        if (params.size()>1) {
            qry+="?";
            for (Param param : params) {
                if (!param.getValue().equals(op))
                    qry += param.toString() + "&";
            }
            qry = qry.substring(0,qry.length()-1);
        }

        okhttp3.Request httpRequest;

        if (op.equals(Param.OPS_GET)) {

            httpRequest = new okhttp3.Request.Builder()
                    .url(url + request.getMethod()+qry)
                    .headers(headers)
                    .get()
                    .build();
        } else {
            throw new IOException("Not supported");
        }

        okhttp3.Response response = httpClient.newCall(httpRequest).execute();
        processHeaders(response.headers());
        ResponseBody responseBody = response.body();
        if (response.isSuccessful()) {
            if (responseBody != null) {
                return buildInputStream(responseBody);
            } else {
                return null;
            }
        } else {
            int code = response.code();
            String text = responseBody == null ? "N/A" : responseBody.string();

            throw new ClientConnectionException("Invalid response received: " + code + "; " + text);
        }
    }

    protected void processHeaders(Headers headers) {
        // Default implementation is empty
    }

    private InputStream buildInputStream(ResponseBody responseBody) throws IOException {
        InputStream inputStream = responseBody.byteStream();

        if (includeRawResponse) {
            // we have to buffer the entire input payload, so that after processing
            // it can be re-read and used to populate the rawResponse field.

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body
            Buffer buffer = source.buffer();

            long size = buffer.size();
            if (size > Integer.MAX_VALUE) {
                throw new UnsupportedOperationException(
                        "Non-integer input buffer size specified: " + size);
            }

            int bufferSize = (int) size;
            BufferedInputStream bufferedinputStream =
                    new BufferedInputStream(inputStream, bufferSize);

            bufferedinputStream.mark(inputStream.available());
            return bufferedinputStream;

        } else {
            return inputStream;
        }
    }

    private Headers buildHeaders() {
        return Headers.of(headers);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addHeaders(Map<String, String> headersToAdd) {
        headers.putAll(headersToAdd);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void close() throws IOException {

    }
}
