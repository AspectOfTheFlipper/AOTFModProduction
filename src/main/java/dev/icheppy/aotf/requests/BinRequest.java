package dev.icheppy.aotf.requests;

import com.google.gson.Gson;
import dev.icheppy.aotf.AspectOfTheFlipper;
import dev.icheppy.aotf.dataclasses.ApiResponse;
import dev.icheppy.aotf.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BinRequest {

    public ApiResponse getResponse() throws IOException {
        Gson gson = new Gson();
        URL req_url = new URL(AspectOfTheFlipper.baseUrl + "/bin_full" + Utils.getRequestParams());
        URLConnection  urlConnection = req_url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) sb.append(line);
        return gson.fromJson(sb.toString(), ApiResponse.class);
    }
}
