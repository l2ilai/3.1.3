package com.override.security.bot.service;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

@Service
public class KeyFileService {
    @SneakyThrows
    public void uploadFile(String file_name, String file_id, String pathDownload, String token) {
        URL url = new URL("https://api.telegram.org/bot" + token + "/getFile?file_id=" + file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL download = new URL("https://api.telegram.org/file/bot" + token + "/" + file_path);
        FileOutputStream fos = new FileOutputStream(pathDownload + file_name);
        System.out.println("Start upload");
        InputStream ins = download.openStream();
        ReadableByteChannel rbc = Channels.newChannel(ins);
        FileChannel fic = fos.getChannel();
        fic.transferFrom(rbc, 0, Long.MAX_VALUE);

        System.out.println("Uploaded!");
    }
}
