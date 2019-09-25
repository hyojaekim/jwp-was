package webserver.http;

import utils.io.FileIoUtils;
import webserver.http.headerfields.HttpConnection;
import webserver.http.headerfields.HttpContentType;
import webserver.http.headerfields.HttpStatusCode;
import webserver.http.headerfields.HttpVersion;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final String TEXT_PLAIN = "text/plain";

    public static final HttpResponse BAD_REQUEST =
            HttpResponse.builder(HttpContentType.getHttpContentType(TEXT_PLAIN))
                        .statusCode(HttpStatusCode.BAD_REQUEST)
                        .build();

    public static final HttpResponse NOT_FOUND =
            HttpResponse.builder(HttpContentType.getHttpContentType(TEXT_PLAIN))
                        .statusCode(HttpStatusCode.NOT_FOUND)
                        .build();

    public static final HttpResponse INTERNAL_SERVER_ERROR =
            HttpResponse.builder(HttpContentType.getHttpContentType(TEXT_PLAIN))
                        .statusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                        .build();

    private final HttpVersion version;
    private final HttpStatusCode statusCode;
    private final HttpContentType contentType;
    private final Map<String, String> optionFields;
    private final String body;

    public static class HttpResponseBuilder {
        private HttpVersion version = HttpVersion.HTTP_1_1;
        private HttpStatusCode statusCode = HttpStatusCode.OK;
        private HttpContentType contentType;
        private Map<String, String> optionFields = new HashMap<>();
        private String body = "";

        public HttpResponseBuilder(HttpContentType contentType) {
            this.contentType = contentType;
        }

        public HttpResponseBuilder version(HttpVersion httpVersion) {
            this.version = httpVersion;
            return this;
        }

        public HttpResponseBuilder statusCode(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public HttpResponseBuilder connection(HttpConnection connection) {
            optionFields.put("Connection", connection.toString());
            return this;
        }

        public HttpResponseBuilder location(String location) {
            optionFields.put("Location", location);
            return this;
        }

        public HttpResponseBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public static HttpResponseBuilder builder(HttpContentType contentType) {
        return new HttpResponseBuilder(contentType);
    }

    private HttpResponse(HttpResponseBuilder builder) {
        this.version = builder.version;
        this.statusCode = builder.statusCode;
        this.contentType = builder.contentType;
        this.optionFields = builder.optionFields;
        this.body = builder.body;
    }

    public static HttpResponse success(HttpRequest request, String contentType, String body) {
        return HttpResponse.builder(HttpContentType.getHttpContentType(contentType))
                .version(request.version())
                .connection(request.connection())
                .body(body)
                .build();
    }

    public static HttpResponse redirection(HttpRequest request, String contentType, String location) {
        return HttpResponse.builder(HttpContentType.getHttpContentType(contentType))
                .version(request.version())
                .statusCode(HttpStatusCode.FOUND)
                .connection(request.connection())
                .location(location)
                .build();
    }

    public static HttpResponse staticFiles(HttpRequest request) {
        return FileIoUtils.loadFileFromClasspath("./static" + request.path()).map(body ->
                HttpResponse.builder(HttpContentType.extensionToContentType(request.path().extension()))
                        .version(request.version())
                        .connection(request.connection())
                        .body(body)
                        .build()
        ).orElse(HttpResponse.NOT_FOUND);
    }

    public String serializeHeader() {
        final StringBuilder header = new StringBuilder(serializeMandatory());

        optionFields.keySet().forEach(key -> header.append(headerParse(key, optionFields.get(key))));

        return header.toString();
    }

    private String headerParse(String fieldName, String fieldValue) {
        return fieldName + ": " + fieldValue + "\r\n";
    }

    private String serializeMandatory() {
        return String.format(
                "%s %d %s\r\n" +
                "Content-Type: %s\r\n" +
                "Content-Length: %d\r\n",
                this.version,
                this.statusCode.number(),
                this.statusCode.text(),
                this.contentType,
                this.body.length()
        );
    }

    public String serialize() {
        return serializeHeader() + "\r\n" + this.body;
    }

    @Override
    public String toString() {
        return serialize();
    }
}