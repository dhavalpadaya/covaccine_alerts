package covaccine.alerts.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class PropertyUtils {
	public static Properties readPropertyFile(String filePath) throws Exception {
        Path path = getPath(filePath);
        try (InputStream props = Files.newInputStream(path, StandardOpenOption.READ)) {
            Properties properties = new Properties();
            properties.load(props);
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static Path getPath(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)) {
            return Files.createFile(path);
        }
        return path;
    }
}
