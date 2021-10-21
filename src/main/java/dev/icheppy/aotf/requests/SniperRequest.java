package dev.icheppy.aotf.requests;

import com.google.gson.Gson;
import dev.icheppy.aotf.AspectOfTheFlipper;
import dev.icheppy.aotf.dataclasses.ApiResponse;
import dev.icheppy.aotf.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class SniperRequest {

    public ApiResponse getResponse() throws IOException, KeyUnauthorizedException, SocketTimeoutException {
        Gson gson = new Gson();
        URL req_url = new URL(AspectOfTheFlipper.baseUrl + "/sniper" + Utils.getRequestParams());
        HttpURLConnection urlConnection = (HttpURLConnection) req_url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(1000);
        urlConnection.setConnectTimeout(1000);
        urlConnection.connect();
        System.out.println("Attempting to fetch flips. Response code: " + Integer.toString(urlConnection.getResponseCode()));
        if (urlConnection.getResponseCode() != 200){
            throw new KeyUnauthorizedException();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) sb.append(line);
        return gson.fromJson(sb.toString(), ApiResponse.class);
    }
}
