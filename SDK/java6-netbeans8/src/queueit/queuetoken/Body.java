/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.queuetoken;

/**
 *
 * @author mala
 */
public class Body {
       public static EnqueueTokenBodyGenerator enqueue() {
        return new EnqueueTokenBodyGenerator();
    } 
}
