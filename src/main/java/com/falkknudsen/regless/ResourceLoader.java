package com.falkknudsen.regless;

import java.net.URL;

public class ResourceLoader {
    private final static String STYLESHEET = "css/style.css";

    /** The singleton instance of this class. An instance is needed, but otherwise it
     is for all intents and purposes a utility class. */
    @SuppressWarnings("InstantiationOfUtilityClass")
    private static final ResourceLoader singleton = new ResourceLoader();

    private static ResourceLoader getInstance() { return singleton; }

    private ResourceLoader() {}

    public static String loadStylesheet() {
        URL url = getInstance().getClass().getResource(STYLESHEET);
        if (url == null) return null;
        return url.toExternalForm();
    }
}
