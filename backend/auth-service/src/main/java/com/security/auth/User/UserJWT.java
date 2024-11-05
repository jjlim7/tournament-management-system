package com.security.auth.User;

public class UserJWT extends User {
    private Boolean isClanLeader;

    public UserJWT(User user, Boolean isClanLeader) {
        super(user.getUserId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.getTokens());
        this.isClanLeader = isClanLeader;
    }

    public static UserJWT newUserJWT(User user, Boolean isClanLeader) {
        return new UserJWT(user, isClanLeader);
    }

    public Boolean getIsClanLeader() {
        return isClanLeader;
    }

    public void setIsClanLeader(Boolean isClanLeader) {
        this.isClanLeader = isClanLeader;
    }

    @Override
    public String toString() {
        return "UserJWT{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role='" + getRole() + '\'' +
                ", tokens=" + getTokens() +
                ", isClanLeader=" + isClanLeader +
                '}';
    }
}
