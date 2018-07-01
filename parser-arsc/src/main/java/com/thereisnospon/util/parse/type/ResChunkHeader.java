package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

/**
 * Header that appears at the front of every data chunk in a resource.
 * <p>
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResChunkHeader {

    /**
     * chunk 为划分的模块单位，有 文件，字符串池，资源包等各种chunk，type 表示类型
     * <p>
     * Type identifier for this chunk.  The meaning of this value depends
     * on the containing chunk.
     *
     * @see ChunkType
     */
    public short type;

    /**
     * 当前 chunk 的 header 的 大小（字节数）
     * Size of the chunk header (in bytes).  Adding this value to
     * the address of the chunk allows you to find its associated data
     * (if any).
     */
    public short headerSize;

    /**
     * 当前整个 chunk 的大小（字节数），包括 chunk 的 header 和 body
     * Total size of this chunk (in bytes).  This is the chunkSize plus
     * the size of any data associated with the chunk.  Adding this value
     * to the chunk allows you to completely skip its contents (including
     * any child chunks).  If this value is the same as chunkSize, there is
     * no data associated with the chunk.
     */
    public int size;

    /**
     * chunk 为划分的模块单位，有 文件，字符串池，资源包等各种chunk
     */
    public interface ChunkType {
        int RES_NULL_TYPE = 0x0000;
        int RES_STRING_POOL_TYPE = 0x0001;
        int RES_TABLE_TYPE = 0x0002;
        int RES_XML_TYPE = 0x0003;
        int RES_XML_FIRST_CHUNK_TYPE = 0x0100;
        int RES_XML_START_NAMESPACE_TYPE = 0x0100;
        int RES_XML_END_NAMESPACE_TYPE = 0x0101;
        int RES_XML_START_ELEMENT_TYPE = 0x0102;
        int RES_XML_END_ELEMENT_TYPE = 0x0103;
        int RES_XML_CDATA_TYPE = 0x0104;
        int RES_XML_LAST_CHUNK_TYPE = 0x017f;
        int RES_XML_RESOURCE_MAP_TYPE = 0x0180;
        int RES_TABLE_PACKAGE_TYPE = 0x0200;
        int RES_TABLE_TYPE_TYPE = 0x0201;
        int RES_TABLE_TYPE_SPEC_TYPE = 0x0202;
    }

    /**
     * 最基本的 chunk header 大小
     *
     * @return
     */
    public int getResChunkHeaderSize() {
        return 2 + 2 + 4;
    }

    /**
     * 调试用，当前 chunk 在文件的绝对偏移
     */
    public int debug_offset;

    /**
     * 解析资源头部信息
     * 所有的Chunk公共的头部信息
     *
     * @param src    源数据
     * @param offset 当前 chunk 在文件的绝对偏移
     * @return
     */
    public static ResChunkHeader parseResChunkHeader(byte[] src, int offset) {

        ResChunkHeader header = new ResChunkHeader();

        //debug
        header.debug_offset = offset;

        //解析头部类型
        byte[] typeByte = ByteUtils.copyByte(src, offset, 2);
        header.type = ByteUtils.byte2Short(typeByte);

        //解析头部大小
        byte[] headerSizeByte = ByteUtils.copyByte(src, offset + 2, 2);
        header.headerSize = ByteUtils.byte2Short(headerSizeByte);

        //解析整个Chunk的大小
        byte[] tableSizeByte = ByteUtils.copyByte(src, offset + 4, 4);
        header.size = ByteUtils.byte2int(tableSizeByte);
        return header;
    }
}
