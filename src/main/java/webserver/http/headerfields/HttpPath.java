package webserver.http.headerfields;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.InvalidHttpPathException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class HttpPath {
    private static final Logger logger = LoggerFactory.getLogger(HttpPath.class);
    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private final String path;

    public static HttpPath of(String path) {
        try {
            path = URLDecoder.decode(path.split("\\?")[0], UTF_8);
            return Optional.of(new HttpPath(path)).orElseThrow(InvalidHttpPathException::new);
        } catch (UnsupportedEncodingException e) {
            logger.debug(e.getMessage());
            throw new InvalidHttpPathException();
        }
    }

    private HttpPath(String path) {
        this.path = path;
    }

    public String extension() {
        return (this.path.contains(".")) ? this.path.substring(this.path.lastIndexOf(".") + 1) : "";
    }

    @Override
    public String toString() {
        return this.path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpPath)) {
            return false;
        }
        final HttpPath rhs = (HttpPath) o;
        return this.path.equals(rhs.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path);
    }
}