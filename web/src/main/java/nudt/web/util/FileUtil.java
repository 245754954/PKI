package nudt.web.util;

import java.io.File;

public class FileUtil {

    public  static boolean delFile(String filename) {
        System.gc();//启动jvm垃圾回收
        filename = filename.replace("\\", "/");
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            String[] filenames = file.list();
            for (String f : filenames) {
                delFile(file.getAbsolutePath() + "/" + f);
            }
            return file.delete();
        }
    }

//    public static void main(String[] args) {
//        String path = "E:\\client\\cert\\rsa\\client\\1591180187553".replace("\\", "/");
//        boolean ans = delFile(path);
//        System.out.println("Delete Result: " + ans);
//    }

}
