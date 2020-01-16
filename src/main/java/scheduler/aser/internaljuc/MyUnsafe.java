package scheduler.aser.internaljuc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MyUnsafe {

    @SuppressWarnings("restriction")
    public
    static Unsafe getUnsafe() {
        try {

            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            return (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            return null;
        }
    }

}
