package dev.icheppy.aotf.requests;

public class KeyUnauthorizedException extends  Exception{
    public KeyUnauthorizedException(){
        super("The key provided is not authorized on the server. Failed to fetch flips");
    }
}
