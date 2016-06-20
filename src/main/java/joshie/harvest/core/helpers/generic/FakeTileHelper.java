package joshie.harvest.core.helpers.generic;

import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class FakeTileHelper {
    public static Class getFakeClass(String className, String classExtends) {
        try {
            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            out.println("public class " + className + " extends " + classExtends + " {}");
            out.close();
            JavaFileObject file = new JavaSourceFromString(className, writer.toString());
            CompilationTask task = ToolProvider.getSystemJavaCompiler().getTask(null, null, null, null, null, Arrays.asList(file));
            boolean success = task.call();
            if (success) {
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});
                return Class.forName(className, true, classLoader);
            } else return null;
        } catch (Exception e) { return null; }
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
