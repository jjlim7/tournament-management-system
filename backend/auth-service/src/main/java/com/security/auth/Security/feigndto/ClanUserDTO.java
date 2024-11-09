package com.security.auth.Security.feigndto;

public class ClanUserDTO {
    private Long clanUserId;
    private Boolean isClanLeader;
    private String position;

    public boolean getIsClanLeader() {
        return isClanLeader;
    }
}
