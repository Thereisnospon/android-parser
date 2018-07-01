package com.thereisnospon.util.parse.type;

import java.util.List;

/**
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTableMapEntryKv extends ResTableEntryKv {

    /**
     * 复杂类型的资源实体，对应多个
     * @see ResTableMap
     */
    public List<ResTableMap> listMap;

    public ResTableMapEntryKv(ResTableEntry entry, List<ResTableMap> listMap) {
        super(entry);
        this.listMap = listMap;
    }
}
