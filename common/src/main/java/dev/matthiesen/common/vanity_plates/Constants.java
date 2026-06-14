package dev.matthiesen.common.vanity_plates;

import dev.matthiesen.common.vanity_plates.util.MetricManager;
import dev.matthiesen.libs.faststats.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Constants {
    public static final String MOD_ID = "vanity_plates";
    public static final String ModName = "Vanity Plates";
    public static @Token final String METRICS_TOKEN = "15f018eba784241058551101acde151d";

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message, Throwable throwable) {
        MetricManager.ERROR_TRACKER.trackError(throwable);
        LOGGER.error(message, throwable);
    }
}
