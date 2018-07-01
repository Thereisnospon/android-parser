package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

/**
 * 文件 chunk header
 * <p>
 * Header for a resource table.  Its data contains a series of
 * additional chunks:
 * A ResStringPool_header containing all table values.  This string pool
 * contains all of the string values in the entire resource table (not
 * the names of entries or type identifiers however).
 * One or more ResTable_package chunks.
 * Specific entries within a resource table can be uniquely identified
 * with a single integer as defined by the ResTable_ref structure.
 * <p>
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTableHeader {

    /**
     * chunk header
     */
    public ResChunkHeader header;

    /**
     * resource.arsc 文件拥有的资源包数
     * The number of ResTable_package structures.
     */
    public int packageCount;

    public ResTableHeader(ResChunkHeader header) {
        this.header = header;
    }

    /**
     * 解析一个 resource.arsc 文件 的 header
     *
     * @param src 源数据
     * @return
     */
    public static ResTableHeader parseResTableHeaderChunk(byte src[]) {

        ResChunkHeader chunkHeader = ResChunkHeader.parseResChunkHeader(src, 0);
        ResTableHeader resTableHeader = new ResTableHeader(chunkHeader);

        //解析PackageCount个数(一个apk可能包含多个Package资源)
        byte[] packageCountByte = ByteUtils.copyByte(src, resTableHeader.header.getResChunkHeaderSize(), 4);
        resTableHeader.packageCount = ByteUtils.byte2int(packageCountByte);
        return resTableHeader;
    }
}
