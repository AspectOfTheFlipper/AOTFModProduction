package dev.icheppy.aotf.dataclasses;

import java.util.ArrayList;


public class ApiResponse {
    public ArrayList<Flip> auctions;
    private Boolean success;
    private long updated;

    public long getUpdated() {
        return updated;
    }

    public ArrayList<Flip> getAuctions() {
        return auctions;
    }

    public Boolean getSuccess() {
        return success;
    }
}
