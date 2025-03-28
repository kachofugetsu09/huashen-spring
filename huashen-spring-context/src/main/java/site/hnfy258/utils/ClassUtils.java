package site.hnfy258.utils;

public class ClassUtils {
    public static ClassLoader getDefaultClassLoader()
    {
        ClassLoader contextClassLoader = null;
        try {
            contextClassLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (contextClassLoader == null) {
            contextClassLoader = ClassUtils.class.getClassLoader();
        }
        return contextClassLoader;

    }
}
