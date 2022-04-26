package org.fenci.fencingfplus2.manager.login;

public class LoginInfo {
    private final String email;
    private final String password;
    //private final String accesstoken;

    //private final List<UUID> allowedUUIDS;

    public LoginInfo(String email, String password /*String acessstoken*/) {
        this.email = email;
        this.password = password;
        //this.password = password;
        //this.allowedUUIDS = Collections.singletonList(allowedUUIDS);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /*
        public String getPassword() {
        return password;
    }
    */


//    public List<UUID> getAllowedUUIDS() {
//        return allowedUUIDS;
//    }
}
