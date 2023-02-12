package me.alex.jsonwrapper.provider;

import com.squareup.moshi.Moshi;
import lombok.extern.slf4j.Slf4j;
import me.alex.jsonwrapper.JsonProvider;

import java.io.*;
import java.util.List;

@Slf4j(topic = "Json-Provider")
public class MoshiProviderImpl implements JsonProvider {
    private Moshi moshi = new Moshi.Builder()
            .build();

    @Override
    public <T> String toJson(Class<T> clazz, T object) {
        return moshi.adapter(clazz)
                .lenient()
                .indent("   ")
                .toJson(object);
    }

    @Override
    public <T> void toJson(Class<T> clazz, T object, OutputStream outputStream) {
        try {
            outputStream.write(toJson(clazz, object).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void toJson(Class<T> clazz, T object, Writer writer) {
        try {
            writer.write(toJson(clazz, object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        try {
            return fromJson(new BufferedReader(new InputStreamReader(inputStream)).readLine(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return moshi.adapter(clazz)
                    .lenient()
                    .indent("   ")
                    .fromJson(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(Reader reader, Class<T> clazz) {
        try {
            //Read reader to a string
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            return moshi.adapter(clazz)
                    .lenient()
                    .indent("   ")
                    .fromJson(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void addSerializer(Class<T> clazz, List<Object> serializers) {
        Moshi.Builder builder = moshi.newBuilder();
        for (Object serializer : serializers) {
            if (serializer instanceof Moshi) {
                builder.add(serializer);
            }
        }
        moshi = builder.build();
    }

}
