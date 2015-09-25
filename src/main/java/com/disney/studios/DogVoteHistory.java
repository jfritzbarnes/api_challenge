package com.disney.studios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Persistent object keeping track of client votes
 **/
@Entity
public class DogVoteHistory {
    
    @Id
    private String voteHistoryId;
    private String voteType;
    
    protected DogVoteHistory() {}
    
    public DogVoteHistory(String voteHistoryId, String voteType) {
        this.voteHistoryId = voteHistoryId;
        this.voteType = voteType;
    }
    
    public DogVoteHistory(String dogId, String clientId, String voteType) {
        this.voteHistoryId = generateId(dogId, clientId);
        this.voteType = voteType;
    }
    
    public String getVoteType() {
        return voteType;
    }
    
    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public String getVoteHistoryId() {
        return voteHistoryId;
    }
    
    public static String generateId(String dogId, String clientId) {
        return dogId + "-" + clientId;
    }
    
    @Override
    public String toString() {
        return String.format(
                             "DogVoteHistory[id=%s, voteType=%s]",
                             voteHistoryId, voteType);
    }
    
    
}