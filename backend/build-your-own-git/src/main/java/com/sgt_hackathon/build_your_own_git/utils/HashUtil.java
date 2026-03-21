package com.sgt_hackathon.build_your_own_git.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static String calculateSHA1(String content){
        try{
            //creating message digest object
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            //convert the content into bytes
            byte[] inputBytes = content.getBytes();

            //calculate hash
            byte[] hashBytes = md.digest(inputBytes);

            //convert bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);//converts integer value into its corresponding hexadecimal string
                // 0xff & b → converts byte into positive integer

                if (hex.length() == 1) {
                    hexString.append('0');  // adds leading zeros
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found");
        }
    }
}
