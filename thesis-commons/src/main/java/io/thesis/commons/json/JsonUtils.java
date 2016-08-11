package io.thesis.commons.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;

/**
 * Convenient JSON read/write utilities.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private JsonUtils() {
    }

    public static <T> T readObjectFromResource(final String resourcePath, final Class<T> clazz) {
        try {
            return MAPPER.readValue(readFromResource(resourcePath), clazz);
        } catch (IOException e) {
            throw new JsonException(format("Unable to read JSON object '%s' from resource path '%s'",
                    clazz.getSimpleName(), resourcePath), e);
        }
    }

    public static <T> T readObject(final Class<T> clazz, final String input) {
        try {
            return MAPPER.readValue(input, clazz);
        } catch (IOException e) {
            throw new JsonException(format("Unable to read JSON object '%s' from input '%s'", clazz.getSimpleName(),
                    input), e);
        }
    }

    public static JsonNode readJsonNodeFromResource(final String resourcePath) {
        try {
            final InputStreamReader r = readFromResource(resourcePath);
            return MAPPER.readTree(r);
        } catch (IOException e) {
            throw new JsonException(format("Unable to read JSON node from resource path '%s'", resourcePath), e);
        }
    }

    public static JsonNode readJsonFromString(final String input) {
        try {
            return MAPPER.readTree(input);
        } catch (IOException e) {
            throw new JsonException(format("Unable to read JSON node from input '%s'", input), e);
        }
    }

    public static <T> String toJson(final T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new JsonException(format("Unable to write JSON object '%s'", object.getClass().getSimpleName()), e);
        }
    }

    public static <T> JsonNode toJsonNode(final T object) {
        final String jsonString = toJson(object);
        return readJsonFromString(jsonString);
    }

    private static InputStreamReader readFromResource(final String resourcePath) throws UnsupportedEncodingException {
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        return new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8.name());
    }
}
