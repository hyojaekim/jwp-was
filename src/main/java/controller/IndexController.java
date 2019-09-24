package controller;

import utils.io.FileIoUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.headerfields.HttpContentType;

public class IndexController {
public static HttpResponse index(HttpRequest request) {
        return FileIoUtils.loadFileFromClasspath("./templates/index.html").map(body ->
            HttpResponse.builder(HttpContentType.TEXT_HTML())
                        .version(request.version())
                        .connection(request.connection().orElse(null))
                        .body(body)
                        .build()
        ).orElse(HttpResponse.INTERNAL_SERVER_ERROR);
    }
}