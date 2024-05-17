package OpenCJAPAuth.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileTool {
    public static void deleteDirectory(File dir) throws IOException {
        if ((dir == null) || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir + " is not a directory. ");
        }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                deleteDirectory(entries[i]);
            } else {
                entries[i].delete();
            }
        }

        dir.delete();
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator))
            destDirName = destDirName + File.separator;
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean CreateFile(String destFileName) {

        File file = new File(destFileName);
        if (file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }

        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String readFile(File file) {
        FileInputStream fin = null;
        try {
            byte[] kk = new byte[1024];
            fin = new FileInputStream(file);
            StringBuffer buf = new StringBuffer();
            int len = 0;
            while ((len = fin.read(kk)) != -1) {
                buf.append(new String(kk, 0, len));
            }

            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean writeFile(File file, String str) {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bb = str.getBytes();
            fout.write(bb);
            fout.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean writeFile(String fileStr, String str) {
        File file = new File(fileStr);

        if (!file.exists()) {
            CreateFile(fileStr);
        }
        try {
            FileOutputStream fout = new FileOutputStream(file);
            byte[] bb = str.getBytes("UTF-8");
            fout.write(bb);
            fout.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void appendFile(String content) {
        if(!Boolean.valueOf(FileProperties.getInstance().getValue("IS_WRITE_LOG"))){
            return;
        }
        String file = "";
        BufferedWriter out = null;
        try {
            String log_path = FileProperties.getInstance().getValue("logPath");
            file = log_path + "auth_log_" + DateTimeUtils.today() + ".log";
            content = DateTimeUtils.now() + "：[" + content + "]\r\n";
            File file1 = new File(file);

            if (!file1.exists()) {
                CreateFile(file);
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
