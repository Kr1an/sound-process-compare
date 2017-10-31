package kulturraum.org.myapplication.core;

import android.app.Activity;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;

public class SoundMerge {
    public static String FIRST_FILE_TMP = "TMP_1";
    public static String SECOND_FILE_TMP = "TMP_2";

    public static void prepareTempFiles(InputStream o1, InputStream o2, Activity context) {
        try {
            File f1 = new File(context.getCacheDir(), FIRST_FILE_TMP);
            File f2 = new File(context.getCacheDir(), SECOND_FILE_TMP);

            FileOutputStream fo1 = new FileOutputStream(f1);
            FileOutputStream fo2 = new FileOutputStream(f2);

            byte[] b1 = new byte[o1.available()];
            byte[] b2 = new byte[o2.available()];

            fo1.write(b1);
            fo2.write(b2);

        } catch (Exception e) {
            Log.e("MANAGE_PLAYER::ERROR", "something wrong with files");
        }

    }

    public static void createTmpFile(String filename) {

    }

    public static File getTmpFile(String filename) {
        return null;
    }

    public static void mergeSound(InputStream file1, InputStream file2) {

//        FileInputStream fisToFinal = null;
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mergedFile);
//            fisToFinal = new FileInputStream(mergedFile);
//            for(File mp3File:mp3Files){
//                if(!mp3File.exists())
//                    continue;
//                FileInputStream fisSong = new FileInputStream(mp3File);
//                SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
//                byte[] buf = new byte[1024];
//                try {
//                    for (int readNum; (readNum = fisSong.read(buf)) != -1;)
//                        fos.write(buf, 0, readNum);
//                } finally {
//                    if(fisSong!=null){
//                        fisSong.close();
//                    }
//                    if(sis!=null){
//                        sis.close();
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
    }

}
