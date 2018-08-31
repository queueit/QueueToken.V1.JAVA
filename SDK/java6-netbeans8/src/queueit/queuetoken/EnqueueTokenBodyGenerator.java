/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.queuetoken;

import java.util.*;

public class EnqueueTokenBodyGenerator {
    private EnqueueTokenBody body;
    
    public EnqueueTokenBodyGenerator() {
         this.body = new EnqueueTokenBody();
    }

    public EnqueueTokenBodyGenerator withKey(String key) {
        this.body = new EnqueueTokenBody(this.body, key);
        
        return this;
    }
    
    public EnqueueTokenBodyGenerator withRank(double rank) {
        this.body = new EnqueueTokenBody(this.body, rank);
        
        return this;
    }

    public EnqueueTokenBodyGenerator withCustomData(String key, String value) {
        this.body = new EnqueueTokenBody(this.body, key, value);
        
        return this;
    }
    
    public IEnqueueTokenBody generate() {
        return this.body;
    }
}
