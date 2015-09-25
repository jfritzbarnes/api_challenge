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
    
    protected DogVoteHistory() {}
    
    public DogVoteHistory(String voteHistoryId) {
        this.voteHistoryId = voteHistoryId;
    }
    
    public DogVoteHistory(String dogId, String clientId) {
        this.voteHistoryId = generateId(dogId, clientId);
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
                             "DogVoteHistory[id=%s]",
                             voteHistoryId);
    }
    
    
}