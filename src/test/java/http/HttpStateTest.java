package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStateTest {
    private static final String GET_METHOD = "GET";
    private static final String DEFAULT_PATH = "/test/test.html";
    private static final String HTTP_1_1 = "HTTP/1.1";

    @Test
    @DisplayName("정상적인 HttpState를 생성한다.")
    void httpState() {
        assertDoesNotThrow(() -> new HttpState(GET_METHOD, DEFAULT_PATH, HTTP_1_1));
    }

    @Test
    @DisplayName("없는 메소드 요청 시 실패한다.")
    void httpStateFail() {
        String nonexistenceMethod = "GEET";
        assertThrows(IllegalArgumentException.class, () -> new HttpState(nonexistenceMethod, DEFAULT_PATH, HTTP_1_1));
    }
}