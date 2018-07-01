package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

/**
 * Created by yzr on 2018/6/20.
 */
public class ResTableMapEntry extends ResTableEntry {

    public ResTableRef parent;
    public int count;

    public ResTableMapEntry(ResTableType __type) {
        super(__type);
        parent = new ResTableRef();
    }

    public static int getSize(){
        return ResTableEntry.getSize()+ ResTableRef.getSize() + 4;
    }

    /**
     * 解析ResMapEntry内容
     *
     * @param src
     * @return
     */
    public static ResTableMapEntry parseResMapEntry(ResTableType type, byte[] src) {
        ResTableMapEntry entry = new ResTableMapEntry(type);

        byte[] sizeByte = ByteUtils.copyByte(src, 0, 2);
        entry.size = ByteUtils.byte2Short(sizeByte);

        byte[] flagByte = ByteUtils.copyByte(src, 2, 2);
        entry.flags = ByteUtils.byte2Short(flagByte);

        ResStringPoolRef key = new ResStringPoolRef();
        byte[] keyByte = ByteUtils.copyByte(src, 4, 4);
        key.index = ByteUtils.byte2int(keyByte);
        entry.key = key;

        ResTableRef ref = new ResTableRef();
        byte[] identByte = ByteUtils.copyByte(src, 8, 4);
        ref.ident = ByteUtils.byte2int(identByte);
        entry.parent = ref;
        byte[] countByte = ByteUtils.copyByte(src, 12, 4);
        entry.count = ByteUtils.byte2int(countByte);

        return entry;
    }

    @Override
   public String type() {
        return "ResTableMapEntry";
    }
}
