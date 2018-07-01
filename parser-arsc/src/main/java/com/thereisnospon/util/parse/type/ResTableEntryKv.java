package com.thereisnospon.util.parse.type;

/**
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public abstract class ResTableEntryKv {

    /**
     * 资源型的key
     */
    public ResTableEntry entry;

    public ResTableEntryKv(ResTableEntry entry) {
        this.entry = entry;
    }
}
