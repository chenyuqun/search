package com.zizaike.solr.bo;

import org.springframework.context.ApplicationEvent;

import com.zizaike.core.framework.event.Event;

/**
 * 
 * ClassName: IEventPublishService <br/>
 * Function:
 * <p>
 * 通用事件模型。
 * </p>
 * <p>
 * 此处对事件的抽象为事件以3个维度进行描述。<br>
 * 事件类型 <br>
 * 发生时间 <br>
 * 事件源。 <br>
 * </p>
 * date: 2016年4月6日 下午2:07:18 <br/>
 * 
 * @author snow.zhang
 * @version
 * @since JDK 1.7
 */
public interface EventPublishService {

    /****
     * 发布事件
     * 
     * @param event
     */
    public <S> void publishEvent(Event<S> event);

    public void publishEvent(ApplicationEvent event);
}