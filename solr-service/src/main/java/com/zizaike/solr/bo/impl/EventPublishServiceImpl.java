package com.zizaike.solr.bo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;

import com.zizaike.core.framework.event.BusinessOperationEvent;
import com.zizaike.core.framework.event.BusinessOperationFailedEvent;
import com.zizaike.core.framework.event.Event;
import com.zizaike.core.framework.exception.ZZKServiceException;
import com.zizaike.solr.bo.EventPublishService;
import com.zizaike.solr.domain.event.SearchApplicationEvent;

@Service
public class EventPublishServiceImpl implements EventPublishService{
    private static final Logger LOG = LoggerFactory.getLogger(EventPublishServiceImpl.class);
	@Autowired
	private ApplicationEventMulticaster applicationEventMulticaster;


	@SuppressWarnings("unchecked")
	@Override
	public <S> void publishEvent(Event<S> event) {
		ApplicationEvent applicationEvent = null;
		if(event instanceof BusinessOperationEvent){
			BusinessOperationEvent<Object> boEvent = (BusinessOperationEvent<Object>)event;
            ZZKServiceException zzkServiceException = null;
            if(boEvent instanceof BusinessOperationFailedEvent){
                BusinessOperationFailedEvent businessOperationFailedEvent = (BusinessOperationFailedEvent)boEvent;
                zzkServiceException = businessOperationFailedEvent.getCause();
            }
            LOG.info("publish business operation event,operation={},action={},source={},error={}",boEvent.getOperation(),boEvent.getAction(),boEvent.getSource(),zzkServiceException);
			applicationEvent = SearchApplicationEvent.newInstance(boEvent);
		}
		if(applicationEvent!=null){
			applicationEventMulticaster.multicastEvent(applicationEvent);
		}

	}
	
	public void publishEvent(ApplicationEvent event) {
		if(event!=null){
			applicationEventMulticaster.multicastEvent(event);
		}
	}
}

