package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

/**
 * A collection of resource data types within a package.  Followed by
 * one or more ResTable_type and ResTable_typeSpec structures containing the
 * entry values for each resource type.
 * <p>
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTablePackageHeader {

    public ResChunkHeader header;

    /**
     * 资源包id
     * If this is a base package, its ID.  Package IDs start
     * at 1 (corresponding to the value of the package bits in a
     * resource identifier).  0 means this is not a base package.
     */
    public int id;
    /**
     * package name
     * Actual name of this package, \0-terminated.
     */
    public byte[] nameByte;

    /**
     * type 字符串池相对本chunk 的开始的偏移
     * Offset to a ResStringPool_header defining the resource
     * type symbol table.  If zero, this package is inheriting from
     * another base package (overriding specific values in it).
     */
    public int typeStrings;
    /**
     * Last index into typeStrings that is for public use by others.
     */
    public int lastPublicType;
    /**
     * key 字符串池相对本chunk 的开始的偏移
     * Offset to a ResStringPool_header defining the resource
     * key symbol table.  If zero, this package is inheriting from
     * another base package (overriding specific values in it).
     */
    public int keyStrings;
    /**
     * Last index into keyStrings that is for public use by others.
     */
    public int lastPublicKey;

    //????
    public int typeIdOffset;

    public ResTablePackageHeader() {

    }

    public static ResTablePackageHeader parsePackageHeader(byte[] src, int packageChunkOffset) {

        ResTablePackageHeader resTabPackageHeader = new ResTablePackageHeader();
        //解析头部信息
        resTabPackageHeader.header = ResChunkHeader.parseResChunkHeader(src, packageChunkOffset);

        //package 头部信息
        int offset = packageChunkOffset + resTabPackageHeader.header.getResChunkHeaderSize();

        //解析packId
        byte[] idByte = ByteUtils.copyByte(src, offset, 4);
        resTabPackageHeader.id = ByteUtils.byte2int(idByte);

        //解析包名
        resTabPackageHeader.nameByte = ByteUtils.copyByte(src, offset + 4, 128 * 2);//这里的128是这个字段的大小，可以查看类型说明，是char类型的，所以要乘以2

        //解析类型字符串的偏移值
        byte[] typeStringsByte = ByteUtils.copyByte(src, offset + 4 + 128 * 2, 4);
        resTabPackageHeader.typeStrings = ByteUtils.byte2int(typeStringsByte);

        //解析lastPublicType字段
        byte[] lastPublicType = ByteUtils.copyByte(src, offset + 8 + 128 * 2, 4);
        resTabPackageHeader.lastPublicType = ByteUtils.byte2int(lastPublicType);

        //解析keyString字符串的偏移值
        byte[] keyStrings = ByteUtils.copyByte(src, offset + 12 + 128 * 2, 4);
        resTabPackageHeader.keyStrings = ByteUtils.byte2int(keyStrings);

        //解析lastPublicKey
        byte[] lastPublicKey = ByteUtils.copyByte(src, offset + 12 + 128 * 2, 4);
        resTabPackageHeader.lastPublicKey = ByteUtils.byte2int(lastPublicKey);

        return resTabPackageHeader;
    }

    public String __debugPkgName() {
        String packageName = new String(nameByte);
        return ByteUtils.filterStringNull(packageName);
    }
}
