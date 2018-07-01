package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

import java.util.ArrayList;
import java.util.List;

import static com.thereisnospon.util.parse.util.DebugUtils.assertTrue;

/**
 * 字符串池
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResStringPool {

    /**
     * 字符串池 chunk header
     */
    public ResStringPoolHeader header;
    /**
     * 字符串索引
     */
    public int[] stringIndexArray;
    /**
     * style索引
     */
    public int[] styleIndexArray;
    /**
     * 当前字符串池拥有的字符串
     */
    public List<String> strings;
    /**
     * 当前字符串池拥有的style
     */
    public List<ResStringPoolSpanList> styles;

    public ResStringPool(ResStringPoolHeader header) {
        this.header = header;
    }

    /**
     * 解析字符串池chunk 的 body 部分
     *
     * @param src
     * @param stringPoolChunkOffset
     * @return
     */
    public static ResStringPool parseStringPoolChunk(byte[] src, int stringPoolChunkOffset) {
        /*
          头部信息
         */
        ResStringPoolHeader stringPoolHeader = ResStringPoolHeader.parseStringPoolHeader(src, stringPoolChunkOffset);
        ResStringPool pool = new ResStringPool(stringPoolHeader);

        /*
           内容信息
         */
        //获取字符串内容的索引数组和样式内容的索引数组
        int[] stringIndexArray = new int[stringPoolHeader.stringCount];
        int[] styleIndexArray = new int[stringPoolHeader.styleCount];

        //        int stringIndex = offset + 20;
        // stringPool header 后面就是 stringIndex 数组
        int stringIndex = stringPoolHeader.header.debug_offset + stringPoolHeader.header.headerSize;
        for (int i = 0; i < stringPoolHeader.stringCount; i++) {
            stringIndexArray[i] = ByteUtils.byte2int(ByteUtils.copyByte(src, stringIndex + i * 4, 4));
        }
        // stringIndex 数组后面是 style 数组
        int styleIndex = stringIndex + 4 * stringPoolHeader.stringCount;
        for (int i = 0; i < stringPoolHeader.styleCount; i++) {
            styleIndexArray[i] = ByteUtils.byte2int(ByteUtils.copyByte(src, styleIndex + i * 4, 4));
        }

        pool.stringIndexArray = stringIndexArray;
        pool.styleIndexArray = styleIndexArray;

        parseStrings(pool, src);
        parseStyles(pool, src);
        return pool;
    }

    /**
     * 解析字符串池的 字符串数据
     *
     * @param pool
     * @param src
     */
    private static void parseStyles(ResStringPool pool, byte src[]) {

        ResStringPoolHeader stringPoolHeader = pool.header;
        boolean encodeUtf16 = stringPoolHeader.flags == 0;

        int spanStart = pool.header.header.debug_offset + pool.header.stylesStart;
        int stingPoolEnd = pool.header.header.debug_offset + pool.header.header.size;
        pool.styles = new ArrayList<>();
        for (int i = 0; i < pool.header.styleCount; i++) {
            int spOff = pool.styleIndexArray[i] + spanStart;
            List<ResStringPoolSpan> spans = new ArrayList<>();
            while (spOff < stingPoolEnd) {
                byte[] nameBytes = ByteUtils.copyByte(src, spOff, 4);
                if ((nameBytes[0] == ResStringPoolSpan.END && nameBytes[1] == ResStringPoolSpan.END)) {
                    break;
                }
                ResStringPoolSpan span = new ResStringPoolSpan();
                span.name = new ResStringPoolRef();
                span.name.index = ByteUtils.byte2int(nameBytes);
                span.firstChar = ByteUtils.byte2int(ByteUtils.copyByte(src, spOff + 4, 4));
                span.lastChar = ByteUtils.byte2int(ByteUtils.copyByte(src, spOff + 8, 4));
                spans.add(span);
                spOff += 12;
            }
            ResStringPoolSpanList stringType = new ResStringPoolSpanList();
            stringType.spans = spans;
            pool.styles.add(stringType);
        }
    }


    /**
     * 解析字符串池的 style 数据
     *
     * @param pool
     * @param src
     */
    private static void parseStrings(ResStringPool pool, byte src[]) {

        ResStringPoolHeader stringPoolHeader = pool.header;
        boolean encodeUtf16 = stringPoolHeader.flags == 0;

        //每个字符串的头两个字节的最后一个字节是字符串的长度
        //这里获取所有字符串的内容
        //int stringContentIndex = styleIndex + stringPoolHeader.styleCount * 4;
        //assertTrue(stringContentIndex == pool.header.header.debug_offset + pool.header.stringsStart);
        int stringContentIndex = pool.header.header.debug_offset + pool.header.stringsStart;
        int stringStartOffset = pool.header.header.debug_offset + pool.header.stringsStart;
        int index = 0;
        List<String> stringList = new ArrayList<>();
        while (index < stringPoolHeader.stringCount) {
            assertTrue(stringContentIndex == pool.stringIndexArray[index] + stringStartOffset);
            int stringSize = 0;
            if (index + 1 < stringPoolHeader.stringCount) {
                int nextOffset = pool.stringIndexArray[index + 1];
                int curOffset = pool.stringIndexArray[index];
                int metaSize = (encodeUtf16 ? 2 : 1) + 2;
                stringSize = (nextOffset - curOffset - metaSize);
                //assertTrue(stringSize == (debug_string_size));
            } else {
                //这个应该也有问题。可能会对齐。。。
                int nextOffset = pool.header.stylesStart > 0 ? pool.header.stylesStart - pool.header.stringsStart : (pool.header.header.size + pool.header.header.debug_offset - stringStartOffset);
                int curOffset = pool.stringIndexArray[index];
                int metaSize = (encodeUtf16 ? 2 : 1) + 2;
                stringSize = (nextOffset - curOffset - metaSize);
                byte[] stringSizeByte = ByteUtils.copyByte(src, stringContentIndex, 2);
                //实测正确。。应该有问题。。Orz
                int debug_string_size = encodeUtf16 ? (stringSizeByte[0] | stringSizeByte[1] << 8) * 2 : ((stringSizeByte[1] & 0x7F));
                //assertTrue(stringSize == (debug_string_size));
                stringSize = debug_string_size;
            }
            if (stringSize != 0) {
                String val = "";
                try {
                    String charset = encodeUtf16 ? "UTF-16LE" : "UTF-8";
                    val = new String(ByteUtils.copyByte(src, stringContentIndex + 2, stringSize), charset);
                } catch (Exception e) {

                }
                stringList.add(val);
            } else {
                stringList.add("");
            }
            //2 字节的 stringSize, utf16 以 0x00 00 结尾， utf8 以 0x00 结尾
            stringContentIndex += (stringSize + 2 + (encodeUtf16 ? 2 : 1));
            index++;
        }
        pool.strings = stringList;
    }
}
