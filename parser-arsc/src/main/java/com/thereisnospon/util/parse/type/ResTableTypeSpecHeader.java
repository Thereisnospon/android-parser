package com.thereisnospon.util.parse.type;

/**
 * 资源配置
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTableTypeSpecHeader {

    public final static int SPEC_PUBLIC = 0x40000000;
    /**
     * chunk header
     */
    public ResChunkHeader header;
    /**
     * 资源类型id
     */
    public byte id;
    //保留
    public byte res0;
    //保留
    public short res1;
    /**
     * 当前类型，同名资源的个数
     */
    public int entryCount;

    public ResTableTypeSpecHeader() {

    }

}
