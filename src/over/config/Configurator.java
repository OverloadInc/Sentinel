package over.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class Configurator {
    private String file;
    private final String PATH = "over/config/";
    private static Configurator configurator;

    private Configurator() {
    }

    public static Configurator getConfigurator() {
        if(configurator == null)
            configurator = new Configurator();

        return configurator;
    }

    public void initConfigurator() {
        String locale = Locale.getDefault().getLanguage().toLowerCase();

        switch (locale) {
            case "español":
            case "es": setFile("es_config"); break;
            default: setFile("en_config");
        }
    }

    public void setFile(String fileName) {
        file = fileName;
    }

    public String getFile() {
        return file;
    }

    public String getProperty(String property) {
        return ResourceBundle.getBundle(PATH + file).getString(property);
    }
}