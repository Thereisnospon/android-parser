package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

import static com.thereisnospon.util.parse.util.DebugUtils.assertTrue;

/**
 * 字符串池 chunk header
 * *******************************************************************
 * String Pool
 * <p>
 * A set of strings that can be references by others through a
 * ResStringPool_ref.
 * <p>
 * **********************************************************************
 * Definition for a pool of strings.  The data of this chunk is an
 * array of uint32_t providing indices into the pool, relative to
 * stringsStart.  At stringsStart are all of the UTF-16 strings
 * concatenated together; each starts with a uint16_t of the string's
 * length and each ends with a 0x0000 terminator.  If a string is >
 * 32767 characters, the high bit of the length is set meaning to take
 * those 15 bits as a high word and it will be followed by another
 * uint16_t containing the low word.
 * <p>
 * If styleCount is not zero, then immediately following the array of
 * uint32_t indices into the string table is another array of indices
 * into a style table starting at stylesStart.  Each entry in the
 * style table is an array of ResStringPool_span structures.
 * <p>
 * Definition for a pool of strings.  The data of this chunk is an
 * array of uint32_t providing indices into the pool, relative to
 * stringsStart.  At stringsStart are all of the UTF-16 strings
 * concatenated together; each starts with a uint16_t of the string's
 * length and each ends with a 0x0000 terminator.  If a string is >
 * 32767 characters, the high bit of the length is set meaning to take
 * those 15 bits as a high word and it will be followed by another
 * uint16_t containing the low word.
 * <p>
 * If styleCount is not zero, then immediately following the array of
 * uint32_t indices into the string table is another array of indices
 * into a style table starting at stylesStart.  Each entry in the
 * style table is an array of ResStringPool_span structures.
 * <p>
 * Definition for a pool of strings.  The data of this chunk is an
 * array of uint32_t providing indices into the pool, relative to
 * stringsStart.  At stringsStart are all of the UTF-16 strings
 * concatenated together; each starts with a uint16_t of the string's
 * length and each ends with a 0x0000 terminator.  If a string is >
 * 32767 characters, the high bit of the length is set meaning to take
 * those 15 bits as a high word and it will be followed by another
 * uint16_t containing the low word.
 * <p>
 * If styleCount is not zero, then immediately following the array of
 * uint32_t indices into the string table is another array of indices
 * into a style table starting at stylesStart.  Each entry in the
 * style table is an array of ResStringPool_span structures.
 * <p>
 * Definition for a pool of strings.  The data of this chunk is an
 * array of uint32_t providing indices into the pool, relative to
 * stringsStart.  At stringsStart are all of the UTF-16 strings
 * concatenated together; each starts with a uint16_t of the string's
 * length and each ends with a 0x0000 terminator.  If a string is >
 * 32767 characters, the high bit of the length is set meaning to take
 * those 15 bits as a high word and it will be followed by another
 * uint16_t containing the low word.
 * <p>
 * If styleCount is not zero, then immediately following the array of
 * uint32_t indices into the string table is another array of indices
 * into a style table starting at stylesStart.  Each entry in the
 * style table is an array of ResStringPool_span structures.
 * <p>
 * Definition for a pool of strings.  The data of this chunk is an
 * array of uint32_t providing indices into the pool, relative to
 * stringsStart.  At stringsStart are all of the UTF-16 strings
 * concatenated together; each starts with a uint16_t of the string's
 * length and each ends with a 0x0000 terminator.  If a string is >
 * 32767 characters, the high bit of the length is set meaning to take
 * those 15 bits as a high word and it will be followed by another
 * uint16_t containing the low word.
 * <p>
 * If styleCount is not zero, then immediately following the array of
 * uint32_t indices into the string table is another array of indices
 * into a style table starting at stylesStart.  Each entry in the
 * style table is an array of ResStringPool_span structures.
 * <p>
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResStringPoolHeader {

    /**
     * 基本 header
     */
    public ResChunkHeader header;

    /**
     * 当前字符串池拥有的字符串数
     * Number of strings in this pool (number of uint32_t indices that follow
     * in the data).
     */
    public int stringCount;

    /**
     * 当前字符串池拥有的style数
     * Number of style span arrays in the pool (number of uint32_t indices
     * follow the string indices).
     */
    public int styleCount;

    public final static int SORTED_FLAG = 1;
    public final static int UTF8_FLAG = (1 << 8);


    interface StringFlags {

        /**
         * If set, the string index is sorted by the string values (based
         * on strcmp16()).
         */
        int SORTED_FLAG = 1 << 0;

        /**
         * String pool is encoded in UTF-8
         */
        int UTF8_FLAG = 1 << 8;
    }

    /**
     * @see StringFlags
     */
    public int flags;
    /**
     * 字符串池的字符串数据 相对于当前 header 起始的偏移
     * Index from header of the string data.
     */
    public int stringsStart;
    /**
     * 字符串池的style数据 相对于当前 header 起始的偏移
     * Index from header of the style data.
     */
    public int stylesStart;


    /**
     * 解析字符串池 chunk header
     *
     * @param src                   源数据
     * @param stringPoolChunkOffset 偏移
     * @return
     */
    public static ResStringPoolHeader parseStringPoolHeader(byte[] src, int stringPoolChunkOffset) {

        ResStringPoolHeader stringPoolHeader = new ResStringPoolHeader();

        //解析 ResChunkHeader 信息
        stringPoolHeader.header = ResChunkHeader.parseResChunkHeader(src, stringPoolChunkOffset);

        // ResChunkHeader 后面是 ResStringPoolHeader 信息
        int offset = stringPoolChunkOffset + stringPoolHeader.header.getResChunkHeaderSize();

        //获取字符串的个数
        byte[] stringCountByte = ByteUtils.copyByte(src, offset, 4);
        stringPoolHeader.stringCount = ByteUtils.byte2int(stringCountByte);

        //解析样式的个数
        byte[] styleCountByte = ByteUtils.copyByte(src, offset + 4, 4);
        stringPoolHeader.styleCount = ByteUtils.byte2int(styleCountByte);

        //这里表示字符串的格式:UTF-8/UTF-16
        byte[] flagByte = ByteUtils.copyByte(src, offset + 8, 4);
        stringPoolHeader.flags = ByteUtils.byte2int(flagByte);


        //字符串内容的开始位置
        byte[] stringStartByte = ByteUtils.copyByte(src, offset + 12, 4);
        stringPoolHeader.stringsStart = ByteUtils.byte2int(stringStartByte);

        //样式内容的开始位置
        byte[] styleStartByte = ByteUtils.copyByte(src, offset + 16, 4);
        stringPoolHeader.stylesStart = ByteUtils.byte2int(styleStartByte);

        assertTrue(offset + 20 == stringPoolHeader.header.debug_offset + stringPoolHeader.header.headerSize);

        return stringPoolHeader;
    }
}
