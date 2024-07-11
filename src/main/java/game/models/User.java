package game.models;

import game.util.ObjectHelper;

public class User {

    //String id;
    String mail;
    String password;
    String username;

    public User() {}
    public User(String mail, String password, String username) {
        this();
        this.setMail(mail);
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getMail() {
        return this.mail;
    }
    public void setMail(String mail) {
        this.mail=mail;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username=username;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password=password;
    }

    @Override
    public String toString() {
        return "User [mail=" + mail + ", username=" + username +", password=" + password + "]";
    }
    
    public boolean isEquals(User u) throws NoSuchMethodException {
        for (String field : ObjectHelper.getFields(this)){
            try {
                if (!(ObjectHelper.getter(this, field) == ObjectHelper.getter(u, field))) {
                    return false;
                }
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
