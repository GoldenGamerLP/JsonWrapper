package me.alex.jsonwrapper;

import me.alex.jsonwrapper.provider.GsonProviderImpl;
import me.alex.jsonwrapper.provider.JacksonProviderImpl;
import me.alex.jsonwrapper.provider.MoshiProviderImpl;

public class JsonMapper {

    private static JsonProvider JSON_PROVIDER;

    public static void init(JsonProviders provider) {
        if (JSON_PROVIDER != null) {
            throw new IllegalStateException("JsonProvider is already initialized");
        }

        JSON_PROVIDER = provider.getProvider();
    }

    public static JsonProvider getJsonProvider() {
        if (JSON_PROVIDER == null) {
            throw new IllegalStateException("JsonProvider is not initialized");
        }

        return JSON_PROVIDER;
    }

    public enum JsonProviders {
        JACKSON,
        MOSHI,
        GSON;

        JsonProviders() {
        }

        public JsonProvider getProvider() {
            return switch (this) {
                case JACKSON -> new JacksonProviderImpl();
                case MOSHI -> new MoshiProviderImpl();
                case GSON -> new GsonProviderImpl();
            };
        }
    }
}
