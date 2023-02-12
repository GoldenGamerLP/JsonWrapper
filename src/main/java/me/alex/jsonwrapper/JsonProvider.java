package me.alex.jsonwrapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public interface JsonProvider {

    //From InputStream and to OutputStream
    //To Json to file with writer
    //From Json from file with reader
    <T> void toJson(Class<T> clazz, T object, Writer writer);

    <T> String toJson(Class<T> clazz, T object);

    <T> void toJson(Class<T> clazz, T object, OutputStream outputStream);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(InputStream inputStream, Class<T> clazz);

    <T> T fromJson(Reader reader, Class<T> clazz);

    //add serializer and a deserializer
    /**
     * Add a serializer and a deserializer to the provider. <br>
     * <b>Gson:</b> {@link com.google.gson.JsonDeserializer} and {@link com.google.gson.JsonSerializer}
     * <br>
     * <b>Jackson:</b> {@link com.fasterxml.jackson.databind.JsonDeserializer} and {@link com.fasterxml.jackson.databind.JsonSerializer}
     * <br>
     * <b>Moshi:</b> {@link Moshi}. In the Class use {@code @ToJson and @FromJson} on the methods.
     * <br>
     * @param clazz The class to bound the serializer to
     * @param serializers A list of serializer and deserializer.
     * @param <T>
     */
    <T> void addSerializer(Class<T> clazz, List<Object> serializers);
}
