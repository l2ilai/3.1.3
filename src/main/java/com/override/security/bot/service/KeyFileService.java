package com.override.security.bot.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Service
public class KeyFileService {

    public void uploadFile(String file_name, String file_id, String pathDownload, String token) {
        URL url = null;
        try {
            url = new URL("https://api.telegram.org/bot" + token + "/getFile?file_id=" + file_id);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String res = null;
        try {
            res = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = null;
        try {
            downoload = new URL("https://api.telegram.org/file/bot" + token + "/" + file_path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathDownload + file_name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Start upload");
        ReadableByteChannel rbc = null;
        try {
            rbc = Channels.newChannel(downoload.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            rbc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Uploaded!");
    }
}
