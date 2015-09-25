package com.disney.studios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Persistent object representing total nr of votes for a particular Dog
 **/
@Entity
public class DogVoteCount {
    
    @Id
    private String dogId;
    private int count = 0;
    
    protected DogVoteCount() {}
    
    public DogVoteCount(String dogId) {
        this.dogId = dogId;
    }
    
    public int getCount() {
        return count;
    }
    
    public int increment() {
        return count++;
    }
    
    public int decrement() {
        return --count;
    }
    
    @Override
    public String toString() {
        return String.format(
                             "DogVote[id=%s, count='%s']",
                             dogId, count);
    }
    

}