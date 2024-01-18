package com.bifurcated.wallet.data;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;
import java.util.UUID;

import static org.hibernate.generator.EventType.INSERT;

public class UseIdOrGenerate implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        Object id = session.getEntityPersister(null, owner).getIdentifier(owner, session);
        return id != null ? id : UUID.randomUUID();
    }


    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(INSERT);
    }

}
