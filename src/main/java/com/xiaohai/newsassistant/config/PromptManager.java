package com.xiaohai.newsassistant.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class PromptManager {

    private static final ResourceBundle BUNDLE;

    static {
        Locale locale = Locale.SIMPLIFIED_CHINESE; // 使用简体中文的 Locale
        Control control = new UTF8Control(); // 使用自定义的 UTF8Control 来读取 UTF-8 编码的 properties 文件
        BUNDLE = ResourceBundle.getBundle("messages", locale, control);
    }

    public static String getPrompt(String key) {
        try {
            return BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return null; // 或者返回默认值或其他处理逻辑
        }
    }

    // 自定义的 ResourceBundle.Control 来以 UTF-8 编码读取 properties 文件
    private static class UTF8Control extends Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            // 构建资源文件的名称
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");

            try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream != null) {
                    // 使用 InputStreamReader 以 UTF-8 编码读取
                    return new Utf8PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                }
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }

    // 自定义的 PropertyResourceBundle 以 UTF-8 编码读取内容
    private static class Utf8PropertyResourceBundle extends ResourceBundle {
        private final Properties properties;

        public Utf8PropertyResourceBundle(InputStreamReader reader) throws IOException {
            properties = new Properties();
            properties.load(reader); // 使用正确的 UTF-8 编码读取
        }

        @Override
        protected Object handleGetObject(String key) {
            return properties.getProperty(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(properties.stringPropertyNames());
        }
    }

    public static void main(String[] args) {
        // 示例输出
        System.out.println(getPrompt("prompt.translate"));
    }
}
