import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class Program {

    public static void main(String[] args) throws Exception {
        set("value", Boolean.FALSE, Boolean.TRUE);
        System.out.println(Boolean.FALSE
                ? "FALSE is TRUE."
                : "FALSE isn't TRUE.");
        set("value", "no", "yes");
        System.out.println("no".equals("yes")
                ? "no means yes."
                : "no doesn't mean yes.");
        set("value", "Romania", "Hungary");
        System.out.println(String.format("Transylvania belongs to %s.", "Romania"));
    }

    public static void set(String field, Object instance, Object value) throws ReflectiveOperationException {
        Class<?> unsafeClass = Class.forName("jdk.internal.misc.Unsafe");
        Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        Object unsafe = theUnsafeField.get(null);
        Field valueField = instance.getClass().getDeclaredField(field);
        long offset = (long) unsafeClass
                .getMethod("objectFieldOffset", Field.class)
                .invoke(unsafe, valueField);
        Object newValue = instance instanceof String
            ? ((String) value).getBytes(StandardCharsets.ISO_8859_1)
            : value;
        unsafeClass
                .getMethod(
                        "putReference",
                        Object.class,
                        long.class,
                        Object.class
                )
                .invoke(unsafe, instance, offset, newValue);
    }
}