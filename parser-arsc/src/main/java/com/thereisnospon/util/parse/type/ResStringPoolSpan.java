package com.thereisnospon.util.parse.type;

/**
 * This structure defines a span of style information associated with
 * a string in the pool.
 * Created by yzr on 2018/6/20.
 */
public class ResStringPoolSpan {

    public final static int END = 0xFFFFFFFF;

    interface Enum {
        int END = 0xFFFFFFFF;
    }

    /**
     * This is the name of the span -- that is, the name of the XML
     * tag that defined it.  The special value END (0xFFFFFFFF) indicates
     * the end of an array of spans.
     */
    public ResStringPoolRef name;
    /**
     * The range of characters in the string that this span applies to.
     */
    public int firstChar, lastChar;
}

