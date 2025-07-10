package org.example.RSC.Service;


public class ServiceUtil
{
    public static <T> T requirePresent(java.util.Optional<T> opt, String msg)
    {
        return opt.orElseThrow(() -> new RuntimeException(msg));
    }
}