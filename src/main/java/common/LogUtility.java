package common;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public class LogUtility {
    public void setLogLevel(Level logLevel){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(logLevel);
    }

    /**
     * returns log level from a string (ch.qos.logback.classic.Level)
     *
     * @param logLevel desired log level
     * @return returns log level
     */
    public ch.qos.logback.classic.Level getLogLevel(String logLevel){
        return ch.qos.logback.classic.Level.valueOf(Objects.requireNonNull(Arrays.stream(ch.qos.logback.classic.Level.class.getFields()).filter(field -> {
            field.setAccessible(true);
            String fieldName = field.getName();
            return fieldName.equalsIgnoreCase(logLevel);
        }).findAny().orElse(null)).getName());
    }
}
