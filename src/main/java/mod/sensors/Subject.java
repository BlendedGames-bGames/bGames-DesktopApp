/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;

import bgapp.utiilities.AttributePlayer;

/**
 *
 * @author InTEracTIon User
 */
public interface Subject {
    
    void addObvserver(Observer obs);
    void removeObserver(Observer obs);
    public void notify(AttributePlayer AP);
    public void notifyOk();
}
