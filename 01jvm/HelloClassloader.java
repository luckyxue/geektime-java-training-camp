
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloClassloader extends ClassLoader {
    public static void main(String[] args) {
        try {
            Class<?> aClass = new HelloClassloader().findClass("Hello");
            Object obj = aClass.newInstance();
            Method method = aClass.getMethod("hello");
            method.invoke(obj);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        File f = new File("/Users/xuehaoyun/Downloads/Hello.xlass");
        File f = new File(this.getClass().getResource("/Hello.xlass").getPath());
        int length = (int) f.length();
        byte[] bytes = new byte[length];
        try {
            new FileInputStream(f).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return super.findClass(name);
        }
        decodeBytes(bytes);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private void decodeBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
    }

    public Class<?> transfer(String name) throws IOException {
        byte[] allByte = Files.readAllBytes(Paths.get("/Users/xuehaoyun/Downloads/Hello.xlass"));
        decodeBytes(allByte);
        return defineClass(name, allByte, 0, allByte.length);
    }
}
