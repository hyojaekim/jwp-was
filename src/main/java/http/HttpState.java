package http;

import java.util.Arrays;
import java.util.List;

public class HttpState {
    private static final List<String> METHOD_TYPE = Arrays.asList("GET", "POST", "PUT", "DELETE");

    private String method;
    private String path;
    private String version;

    public HttpState(String method, String path, String version) {
        this.method = checkMethodType(method);
        this.path = path;
        this.version = version;
    }

    private String checkMethodType(String method) {
        if (METHOD_TYPE.contains(method)) {
            return method;
        }
        throw new IllegalArgumentException();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }
}
