package cn.sz.pxd.floatword.utils;

import cn.sz.pxd.floatword.WordFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class DownloadFile {

    private static final Logger logger = LoggerFactory.getLogger(WordFrame.class);

    public static void downloadByGet(String word, String soundDirPath) {
        String remoteUrl = String.format(
            "https://ssl.gstatic.com/dictionary/static/sounds/oxford/%s--_us_1.mp3",
            word
        );
        logger.info("下载音频文件：{}", remoteUrl);
        HttpURLConnection con = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(remoteUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                inputStream = con.getInputStream();
                byte[] bytes = new byte[1024];
                int i;
                String filePath = soundDirPath + File.separator + word + "_us.mp3";
                fileOutputStream = new FileOutputStream(filePath);
                while ((i = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, i);
                    fileOutputStream.flush();
                }
                logger.info("download file success");
            } else {
                logger.error("download file error,status code:" + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

}
