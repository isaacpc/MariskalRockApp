package com.isaacpc.mariskalrock.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class Log {
        static final boolean LOG = false;

        public static void i(String tag, String string) {
                if (LOG) {
                        appendLog(tag, string);
                        android.util.Log.i(tag, string);
                }
        }

        public static void e(String tag, String string) {
                if (LOG) {
                        appendLog(tag, string);
                        android.util.Log.e(tag, string);
                }
        }

        public static void d(String tag, String string) {
                if (LOG) {
                        appendLog(tag, string);
                        android.util.Log.d(tag, string);
                }
        }

        public static void v(String tag, String string) {
                if (LOG) {
                        // appendLog(tag, string);
                        android.util.Log.v(tag, string);
                }
        }

        public static void w(String tag, String string) {
                if (LOG) {
                        appendLog(tag, string);
                        android.util.Log.w(tag, string);
                }
        }

        /**
         * Escribe en el fichero la traza ejecutada
         * 
         * @param tag
         * @param text
         */
        private static void appendLog(String tag, String text) {

                File fileLog = null;
                try {
                        final String fileName = "log_mr.txt";

                        final File externalStorageDir = Environment.getExternalStorageDirectory();
                        fileLog = new File(externalStorageDir, fileName);

                        if (!fileLog.exists()) {
                                fileLog.createNewFile();
                        }

                        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                        final String currentDateandTime = sdf.format(new Date());

                        final PrintWriter pw = new PrintWriter(new FileOutputStream(fileLog, true));

                        pw.append("+" + currentDateandTime + "-" + tag + "-" + text).append("\n");
                        pw.close();
                } catch (final FileNotFoundException e) {
                        e.printStackTrace();
                } catch (final IOException e) {
                        e.printStackTrace();
                }
        }
}
