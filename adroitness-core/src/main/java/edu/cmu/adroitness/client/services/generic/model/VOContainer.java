package edu.cmu.adroitness.client.services.generic.model;

import edu.cmu.adroitness.comm.generic.model.BaseEvent;
import edu.cmu.adroitness.client.services.generic.control.GenericContainer;

/**
 * Created by oscarr on 8/31/16.
 */
public interface VOContainer {
    <T extends BaseEvent> T setVO( GenericContainer<? extends DataObject> vo);
}
