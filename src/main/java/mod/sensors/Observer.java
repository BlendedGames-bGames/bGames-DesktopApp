/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;

import bgapp.utiilities.AttributePlayer;
import bgapp.utiilities.AttributePlayer;
import bgapp.utiilities.SensorNeed;

/**
 *
 * @author InTEracTIon User
 */
public interface Observer {
    public void update();

    public void update(AttributePlayer AP);
    public void update(SensorNeed SN);
}
