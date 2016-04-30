package joshie.harvest.asm;

public class ASMHelper {

    /**
     * Converts a class name to an internal class name.
     *
     * @return internal/class/name
     */
    public static String toInternalClassName(String className) {
        return className.replace('.', '/');
    }

    /**
     * @return true if the String is a valid descriptor;
     */
    public static boolean isDescriptor(String descriptor) {
        return descriptor.length() == 1 || (descriptor.startsWith("L") && descriptor.endsWith(";"));
    }

    /**
     * Converts a class name to a descriptor.
     *
     * @return Linternal/class/name;
     */
    public static String toDescriptor(String className) {
        return isDescriptor(className) ? className : "L" + toInternalClassName(className) + ";";
    }

    /**
     * Turns the given return and parameter values into a method descriptor
     * Converts the types into descriptors as needed
     *
     * @return (LparamType;)LreturnType;
     */
    public static String toMethodDescriptor(String returnType, String... paramTypes) {
        StringBuilder paramDescriptors = new StringBuilder();
        for (String paramType : paramTypes)
            paramDescriptors.append(toDescriptor(paramType));

        return "(" + paramDescriptors.toString() + ")" + toDescriptor(returnType);
    }
}