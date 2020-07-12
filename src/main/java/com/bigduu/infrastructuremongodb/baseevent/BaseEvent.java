package com.bigduu.infrastructuremongodb.baseevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author bigduu
 * @title: BaseEvent
 * @projectName mjtx
 * @description: TODO
 * @date 2020/6/1922:08
 */
@Getter
public class BaseEvent<T> extends ApplicationEvent {
    private final T payload;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public BaseEvent(T source) {
        super(source);
        this.payload = source;
    }
}
