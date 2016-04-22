package com.zizaike.solr.domain.event;

import org.springframework.context.ApplicationEvent;

import com.zizaike.core.framework.event.BusinessOperation;
import com.zizaike.core.framework.event.BusinessOperationEvent;
import com.zizaike.core.framework.event.BusinessOperationEvent.Action;
import com.zizaike.core.framework.event.BusinessOperationFailedEvent;
import com.zizaike.core.framework.exception.ZZKServiceException;

/**
 * 
 * ClassName: PassportApplicationEvent <br/>  
 * Function: passport应用事件. <br/>  
 * date: 2016年4月5日 下午2:35:12 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class SearchApplicationEvent extends ApplicationEvent {
	
    private static final long serialVersionUID = 1426060520147729423L;

    private long occuredTime;
	
	private ZZKServiceException exception;
	
	private Action action;
	
	private BusinessOperation operation;

	public SearchApplicationEvent(Object source) {
		super(source);
	}
	
	@SuppressWarnings("rawtypes")
	public static SearchApplicationEvent newInstance(BusinessOperationEvent< ? extends Object> businessOperationEvent) {
		SearchApplicationEvent event = new SearchApplicationEvent(businessOperationEvent.getSource());
		event.operation = businessOperationEvent.getOperation();
		event.occuredTime = businessOperationEvent.getOccuredTime();
		event.action = businessOperationEvent.getAction();
		if(businessOperationEvent instanceof BusinessOperationFailedEvent){
			BusinessOperationFailedEvent failedEvent = (BusinessOperationFailedEvent)businessOperationEvent;
			event.exception = failedEvent.getCause();
		}
		return event;
	}


	public long getOccuredTime() {
		return occuredTime;
	}

	public void setOccuredTime(long occuredTime) {
		this.occuredTime = occuredTime;
	}

	
	public ZZKServiceException getException() {
        return exception;
    }

    public void setException(ZZKServiceException exception) {
        this.exception = exception;
    }

    public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public BusinessOperation getOperation() {
		return operation;
	}
	
	

}
