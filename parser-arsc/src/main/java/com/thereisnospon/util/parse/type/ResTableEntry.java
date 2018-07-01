package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

import java.util.List;

/**
 * Created by yzr on 2018/6/20.
 */
public class ResTableEntry {

    public final static int FLAG_COMPLEX = 0x0001;
    public final static int FLAG_PUBLIC = 0x0002;

    /**
     * 资源项占用的大小
     */
    public short size;

    /**
     * flag,是否是
     */
    public short flags;

    /**
     * 资源项名。（在包的key字符串池中
     */
    public ResStringPoolRef key;

    /**
     * debug
     */
    public ResTableType __type;


    public ResTableEntry(ResTableType __type) {
        key = new ResStringPoolRef();
        this.__type = __type;
    }

    /**
     * debug，在同类型的资源中的位置
     */
    public int __index;


    /**
     * debug
     * 是否是占位的
     */
    public boolean __empty;

    public static int getSize() {
        return 2 + 2 + ResStringPoolRef.getSize();
    }

    /**
     * 解析ResEntry内容
     *
     * @param src
     * @return
     */
    public static ResTableEntry parseResEntry(ResTableType type, byte[] src) {
        ResTableEntry entry = new ResTableEntry(type);

        byte[] sizeByte = ByteUtils.copyByte(src, 0, 2);
        entry.size = ByteUtils.byte2Short(sizeByte);

        byte[] flagByte = ByteUtils.copyByte(src, 2, 2);
        entry.flags = ByteUtils.byte2Short(flagByte);

        ResStringPoolRef key = new ResStringPoolRef();
        byte[] keyByte = ByteUtils.copyByte(src, 4, 4);
        key.index = ByteUtils.byte2int(keyByte);
        entry.key = key;

        return entry;
    }

    /**
     * 获取在 资源包中key字符串池对应的字符串
     *
     * @param index 索引
     * @return
     */
    public String getKeyString(int index) {
        List<String> keyStringList = __type.__pkg.keyStringPool.strings;
        if (index >= keyStringList.size() || index < 0) {
            return "";
        }
        return keyStringList.get(index);
    }

    public String type() {
        return "ResTableEntry";
    }
}
