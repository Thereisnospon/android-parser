package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

/**
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTableMap {

    /**
     * map item 对应的 key 字符串
     */
    public ResTableRef name;
    /**
     * value
     */
    public ResValue value;

    public ResTableEntry __entry;

    public ResTableMap() {
        name = new ResTableRef();
        value = new ResValue();
    }

    /**
     * 解析ResTableMap内容
     *
     * @param src
     * @return
     */
    public static ResTableMap parseResTableMap(ResTableEntry entry, byte[] src) {

        ResTableMap tableMap = new ResTableMap();
        tableMap.__entry = entry;

        ResTableRef ref = new ResTableRef();
        byte[] identByte = ByteUtils.copyByte(src, 0, ref.getSize());
        ref.ident = ByteUtils.byte2int(identByte);
        tableMap.name = ref;

        ResValue value = new ResValue();
        value = ResValue.parseResValue(ByteUtils.copyByte(src, ref.getSize(), value.getSize()));
        tableMap.value = value;
        value.__entry = entry;

        return tableMap;
    }

    public int getSize() {
        return ResTableRef.getSize() + value.getSize();
    }

}
