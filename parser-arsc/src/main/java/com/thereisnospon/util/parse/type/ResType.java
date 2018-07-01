package com.thereisnospon.util.parse.type;

import java.util.List;

/**
 * Created by yzr on 2018/6/21.
 * 一种资源类型对应的 spec 配置表和所有资源项
 */
public class ResType {

    /**
     * 资源配置
     */
    public ResTypeSpec spec;
    /**
     * 所有配置下的资源项
     */
    public List<ResTableType> typeList;
}
