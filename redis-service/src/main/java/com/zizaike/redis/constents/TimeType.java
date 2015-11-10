package com.zizaike.redis.constents;

import com.zizaike.core.framework.ienum.IEnum;

public enum TimeType implements IEnum{
    /**
     * 一分钟
     */
    MINUTE(60),
    /**
     * 一小时
     */
    HOUR(60*60),
    /**
     * 一天 24小时
     */
    DAY(60*60*24),
    ;
    private int value;
    private TimeType(int value) {
        this.value = value;
    }
    @Override
    public int getValue() {
        return this.value;
    }
}
