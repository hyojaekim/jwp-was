package utils.parser;

import java.util.Map;

public interface KeyValueParser {
    Map<String, String> toMap(String x);
    //T toType(String x);
}