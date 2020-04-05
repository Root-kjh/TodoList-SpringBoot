package com.drk.todolist.Crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class sha512 {
    public String hash(String pswd) {
        MessageDigest md;
        String tempPassword = "";
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(pswd.getBytes());
            byte[] mb = md.digest();
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                tempPassword += s;
            }
        } catch (NoSuchAlgorithmException e) {
            return tempPassword;
        }
        return tempPassword;
    }
}