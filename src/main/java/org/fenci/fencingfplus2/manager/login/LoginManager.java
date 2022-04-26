package org.fenci.fencingfplus2.manager.login;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import org.fenci.fencingfplus2.util.Globals;
import org.fenci.fencingfplus2.util.client.ClientMessage;

import java.util.Base64;

public class LoginManager implements Globals {

    //reverse the email

    //char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

//    private List<Token> tokenList = new ArrayList<>();
//    private List<LoginInfo> loginInfoList = new ArrayList<>();
//
//    public void addToken(Token token) {
//        if (tokenList.size() >= 1) tokenList.clear();
//        tokenList.add(token);
//    }
//
//    public void addLoginInfo(LoginInfo loginInfo) {
//        if (loginInfoList.size() >= 1) loginInfoList.clear();
//        loginInfoList.add(loginInfo);
//    }

    public Token encrypt(String microsoftToken) {
        //List<UUID> allowedUUIDS = loginInfo.getAllowedUUIDS();
        if (microsoftToken.length() <= 1) {
            ClientMessage.sendMessage("Unable to encrypt");
            return null;
        }
        String finalToken = caesarCipherEncrypt(microsoftToken);

        return new Token(finalToken);
    }

    public Token decrypt(Token encryptedInfo) { //this returns the original token before it was encrypted for the second time
        if (encryptedInfo == null) return null;
        String decryptedEmail = caesarCipherDecrypt(encryptedInfo.getEncryptedInfo());
        return new Token(decryptedEmail);
    }

    protected String caesarCipherEncrypt(String plain) {
        String b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());

        // Reverse the string
        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < reverse.length(); i++) {
            tmp.append((char)(reverse.charAt(i) + OFFSET));
        }
        return tmp.toString();
    }

    protected String caesarCipherDecrypt(String encryptedLogin) {
        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < encryptedLogin.length(); i++) {
            tmp.append((char)(encryptedLogin.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }

    public String getMicrosoftToken(LoginInfo info) {
        String accessToken;
        try {
            MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
            MicrosoftAuthResult result = authenticator.loginWithCredentials(info.getEmail(), info.getPassword());
            accessToken = result.getAccessToken();
            //} catch (NullPointerException exception) {}
        } catch (MicrosoftAuthenticationException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }
}

