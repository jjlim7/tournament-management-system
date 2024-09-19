package com.example.userservice.entity;

public class Clan {
    private String clanId;
    private String clanName;

    public Clan() {
    
    }
    
    public Clan(String clanId, String clanName) {
        this.clanId = clanId;
        this.clanName = clanName;
    }

    public String getClanId() {
        return clanId;
    }
    public void setClanId(String clanId) {
        this.clanId = clanId;
    }
    public String getClanName() {
        return clanName;
    }
    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    

}
