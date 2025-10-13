package com.falkknudsen.regless;

import java.net.URL;

public class ResourceLoader {
    private final static String STYLESHEET = "css/style.css";
    private final static String APP_ICON = "icons/regless.png";

    /** The singleton instance of this class. An instance is needed, but otherwise it
     is for all intents and purposes a utility class. */
    @SuppressWarnings("InstantiationOfUtilityClass") // <- This is known and intentional.
    private static final ResourceLoader singleton = new ResourceLoader();

    private static ResourceLoader getInstance() { return singleton; }

    private ResourceLoader() {}

    public static String loadStylesheet() {
        URL url = getInstance().getClass().getResource(STYLESHEET);
        if (url == null) return null;
        return url.toExternalForm();
    }

    public static String loadAppIcon() {
        URL url = getInstance().getClass().getResource(APP_ICON);
        if (url == null) return null;
        return url.toExternalForm();
    }
}
