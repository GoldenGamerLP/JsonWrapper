package me.alex.jsonwrapper.configurations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.extern.slf4j.Slf4j;
import me.alex.jsonwrapper.JsonMapper;
import me.alex.jsonwrapper.JsonProvider;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Custom Class De-/Serializer. Can be used for extending classes. <p>
 *
 * <b>Loader Class Example:</b> JsonConfigurationLoader<MyConfig> cfg = new JsonConfigurationLoader(youreFile,MyConfig.class); <p>
 *
 * <b>Config Class:</b> Do not use a constructor with specifications and define variables with the annotation {@link SerializedName} & {@link Expose}. <p>
 * Use <b>Getters/Setters</b> to read/write to the class. <p>
 *
 * @param <T> The corresponding class
 * @author Alex W - GoldenGamer_LP
 */
@Slf4j(topic = "Configuration-Loader")
public final class JsonConfigurationLoader<T> {
    private static final JsonProvider jsonProvider;

    static {
        jsonProvider = JsonMapper.getJsonProvider();
    }

    private final Class<T> of;
    private final File file;
    private T object;

    /**
     * Used for extending or creating new FileDataLoader class.
     *
     * @param file The File to write/read to.
     * @param of   The class to be used.
     */
    private JsonConfigurationLoader(@NotNull File file, @NotNull Class<T> of) {
        this.of = of;
        this.file = file;
    }

    /**
     * @param file The File to write/read to.
     * @param of The class to be used.
     * @param <T> The corresponding class
     * @return The FileDataLoader class.
     */
    public static <T> JsonConfigurationLoader<T> of(@NotNull File file, @NotNull Class<T> of) {
        return new JsonConfigurationLoader<>(file, of).load();
    }

    /**
     * @param path The path to the file.
     * @param configuration The file to write/read to.
     * @param of The class to be used.
     * @param <T> The corresponding class
     * @return The FileDataLoader class.
     */
    public static <T> JsonConfigurationLoader<T> of(@NotNull Path path, File configuration, @NotNull Class<T> of) {
        return new JsonConfigurationLoader<>(path.resolve(configuration.getName()).toFile(), of).load();
    }

    public synchronized JsonConfigurationLoader<T> load() {
        try {
            if (!file.exists()) {
                log.info("Creating [{}] file successfully: {}", this.file, createFileIfNotExists());
                object = of.getDeclaredConstructor().newInstance();
                writeToFile();
            } else readFromFile();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonConfigurationLoader<T> save() {
        writeToFile();
        return this;
    }

    public T getData() {
        return object;
    }

    private Boolean createFileIfNotExists() {
        try {
            return file.mkdirs() && file.delete() && file.createNewFile();
        } catch (IOException e) {
            log.warn("Failed to create [{}]File with error: {}", this.file, e);
        }
        return false;
    }

    private void writeToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) {
            jsonProvider.toJson(of, object, bufferedWriter);
        } catch (IOException e) {
            log.warn("Failed to save [{}] File with error: {}", this.file, e);
        }
    }

    private void readFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            object = jsonProvider.fromJson(bufferedReader, of);
        } catch (IOException e) {
            log.warn("Failed to save [{}] File with error: {}", this.file, e);
        }
    }
}
