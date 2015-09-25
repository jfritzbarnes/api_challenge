package com.disney.studios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple tests to see if we got all the persistence stuff in order.
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PersistenceTests {
    
    @Autowired
    DogVoteCountRepository countRepository;
    
    @Autowired
    DogVoteHistoryRepository historyRepository;
    
    @Test
    public void testDogCounts() {
    
        DogVoteCount pugCount = new DogVoteCount("pug");
        DogVoteCount yorkieCount = new DogVoteCount("yorkie");
        countRepository.save(pugCount);
        countRepository.save(yorkieCount);
        
        Iterable<DogVoteCount> counts = countRepository.findAll();
        
        int count = 0;
        for(DogVoteCount p : counts){
            count++;
        }
        
        // We should have 2 in db
        assertEquals(count, 2);
        
        // Now lets try increments / decrements
        int pugVotes = pugCount.increment();
        pugVotes = pugCount.increment();
        pugVotes = pugCount.increment();
        pugVotes = pugCount.increment();
        pugVotes = pugCount.increment();
        pugVotes = pugCount.decrement();
        
        countRepository.save(pugCount);
        
        // Lets fetch a fresh instance from db
        DogVoteCount pugCountFromDb = countRepository.findOne("pug");
        System.out.println("PUG: " + pugCountFromDb);
        
        assertEquals(pugVotes, pugCountFromDb.getCount());
        
    }
    
    @Test
    public void testDogCountHistory() {
        
        DogVoteHistory history = new DogVoteHistory("pug", "the_client");
        historyRepository.save(history);
        
        Iterable<DogVoteHistory> counts = historyRepository.findAll();
        
        int count = 0;
        for(DogVoteHistory p : counts){
            count++;
            System.out.println(p);
        }
        // We should have 1 in db
        assertEquals(count, 1);
        
        boolean exists = historyRepository.exists(history.getVoteHistoryId());
        
        // Check the exsistence
        assertTrue(exists);
    
    }
    

}