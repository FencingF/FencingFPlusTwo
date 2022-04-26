package org.fenci.fencingfplus2.manager.login;

public class Token {
    private final String encryptedInfo;

    public Token(String encryptedInfo) {
        this.encryptedInfo = encryptedInfo;
    }

    public String getEncryptedInfo() {
        return encryptedInfo;
    }

}
