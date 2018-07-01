package com.thereisnospon.util.parse.type;


/**
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTableEntrySimpleKv extends ResTableEntryKv {

    /**
     * 简单类型的资源实体的 value
     */
    public ResValue value;

    public ResTableEntrySimpleKv(ResTableEntry entry, ResValue value) {
        super(entry);
        this.value = value;
    }
}
