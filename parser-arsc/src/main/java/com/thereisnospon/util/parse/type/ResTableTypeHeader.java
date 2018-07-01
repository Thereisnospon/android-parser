package com.thereisnospon.util.parse.type;

/**
 * 例如 string-hdpi.xml, string-zh.xml,string-xxhdpi-cn.xml 对应不同的  ResTableType
 * Created by yzr on 2018/6/20.
 */
public class ResTableTypeHeader {

    public ResChunkHeader header;

    public final static int NO_ENTRY = 0xFFFFFFFF;

    public byte id;
    //保留
    public byte res0;
    //保留
    public short res1;
    /**
     * 资源实体个数
     */
    public int entryCount;
    /**
     * 资源实体相对于本chunk开始的偏移
     */
    public int entriesStart;

    /**
     * 当前资源类型的配置，分辨率，屏幕大小等。
     */
    public ResTableConfig resConfig;

    public ResTableTypeHeader() {
        resConfig = new ResTableConfig();
    }
}
