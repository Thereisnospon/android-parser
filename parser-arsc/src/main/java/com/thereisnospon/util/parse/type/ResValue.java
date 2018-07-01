package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

import java.util.List;

/**
 * 资源项的值
 */
public class ResValue {

    // Structure of complex data values (TYPE_UNIT and TYPE_FRACTION)
    interface ComplexDataValue {
        // Where the unit type information is.  This gives us 16 possible
        // types; as defined below.
        int COMPLEX_UNIT_SHIFT = 0;
        int COMPLEX_UNIT_MASK = 0xf;
        // TYPE_DIMENSION: Value is raw pixels.
        int COMPLEX_UNIT_PX = 0;
        // TYPE_DIMENSION: Value is Device Independent Pixels.
        int COMPLEX_UNIT_DIP = 1;
        // TYPE_DIMENSION: Value is a Scaled device independent Pixels.
        int COMPLEX_UNIT_SP = 2;
        // TYPE_DIMENSION: Value is in points.
        int COMPLEX_UNIT_PT = 3;
        // TYPE_DIMENSION: Value is in inches.
        int COMPLEX_UNIT_IN = 4;
        // TYPE_DIMENSION: Value is in millimeters.
        int COMPLEX_UNIT_MM = 5;
        // TYPE_FRACTION: A basic fraction of the overall size.
        int COMPLEX_UNIT_FRACTION = 0;
        // TYPE_FRACTION: A fraction of the parent size.
        int COMPLEX_UNIT_FRACTION_PARENT = 1;
        // Where the radix information is; telling where the decimal place
        // appears in the mantissa.  This give us 4 possible fixed point
        // representations as defined below.
        int COMPLEX_RADIX_SHIFT = 4;
        int COMPLEX_RADIX_MASK = 0x3;
        // The mantissa is an integral number -- i.e.; 0xnnnnnn.0
        int COMPLEX_RADIX_23p0 = 0;
        // The mantissa magnitude is 16 bits -- i.e; 0xnnnn.nn
        int COMPLEX_RADIX_16p7 = 1;
        // The mantissa magnitude is 8 bits -- i.e; 0xnn.nnnn
        int COMPLEX_RADIX_8p15 = 2;
        // The mantissa magnitude is 0 bits -- i.e; 0x0.nnnnnn
        int COMPLEX_RADIX_0p23 = 3;
        // Where the actual value is.  This gives us 23 bits of
        // precision.  The top bit is the sign.
        int COMPLEX_MANTISSA_SHIFT = 8;
        int COMPLEX_MANTISSA_MASK = 0xffffff;
    }

   /* public static final int
            COMPLEX_UNIT_PX = 0,
            COMPLEX_UNIT_DIP = 1,
            COMPLEX_UNIT_SP = 2,
            COMPLEX_UNIT_PT = 3,
            COMPLEX_UNIT_IN = 4,
            COMPLEX_UNIT_MM = 5,
            COMPLEX_UNIT_SHIFT = 0,
            COMPLEX_UNIT_MASK = 15,
            COMPLEX_UNIT_FRACTION = 0,
            COMPLEX_UNIT_FRACTION_PARENT = 1,
            COMPLEX_RADIX_23p0 = 0,
            COMPLEX_RADIX_16p7 = 1,
            COMPLEX_RADIX_8p15 = 2,
            COMPLEX_RADIX_0p23 = 3,
            COMPLEX_RADIX_SHIFT = 4,
            COMPLEX_RADIX_MASK = 3,
            COMPLEX_MANTISSA_SHIFT = 8,
            COMPLEX_MANTISSA_MASK = 0xFFFFFF;*/

    // Number of bytes in this structure.
    public short size;
    // Always set to 0.
    public byte res0;

    // Type of the data value.
    interface DataValueType {
        // The 'data' is either 0 or 1; specifying this resource is either
        // undefined or empty; respectively.
        int TYPE_NULL = 0x00;
        // The 'data' holds a ResTable_ref; a reference to another resource
        // table entry.
        int TYPE_REFERENCE = 0x01;
        // The 'data' holds an attribute resource identifier.
        int TYPE_ATTRIBUTE = 0x02;
        // The 'data' holds an index into the containing resource table's
        // global value string pool.
        int TYPE_STRING = 0x03;
        // The 'data' holds a single-precision floating point number.
        int TYPE_FLOAT = 0x04;
        // The 'data' holds a complex number encoding a dimension value;
        // such as "100in".
        int TYPE_DIMENSION = 0x05;
        // The 'data' holds a complex number encoding a fraction of a
        // container.
        int TYPE_FRACTION = 0x06;
        // The 'data' holds a dynamic ResTable_ref; which needs to be
        // resolved before it can be used like a TYPE_REFERENCE.
        int TYPE_DYNAMIC_REFERENCE = 0x07;
        // Beginning of integer flavors...
        int TYPE_FIRST_INT = 0x10;
        // The 'data' is a raw integer value of the form n..n.
        int TYPE_INT_DEC = 0x10;
        // The 'data' is a raw integer value of the form 0xn..n.
        int TYPE_INT_HEX = 0x11;
        // The 'data' is either 0 or 1; for input "false" or "true" respectively.
        int TYPE_INT_BOOLEAN = 0x12;
        // Beginning of color integer flavors...
        int TYPE_FIRST_COLOR_INT = 0x1c;
        // The 'data' is a raw integer value of the form #aarrggbb.
        int TYPE_INT_COLOR_ARGB8 = 0x1c;
        // The 'data' is a raw integer value of the form #rrggbb.
        int TYPE_INT_COLOR_RGB8 = 0x1d;
        // The 'data' is a raw integer value of the form #argb.
        int TYPE_INT_COLOR_ARGB4 = 0x1e;
        // The 'data' is a raw integer value of the form #rgb.
        int TYPE_INT_COLOR_RGB4 = 0x1f;
        // ...end of integer flavors.
        int TYPE_LAST_COLOR_INT = 0x1f;
        // ...end of integer flavors.
        int TYPE_LAST_INT = 0x1f;
    }

    // Possible data values for TYPE_NULL.
    interface DataTypeNull {
        // The value is not defined.
        int DATA_NULL_UNDEFINED = 0;
        // The value is explicitly defined as empty.
        int DATA_NULL_EMPTY = 1;
    }

    public byte dataType;

    public int data;

    public ResTableEntry __entry;

    /**
     * 解析ResValue内容
     *
     * @param src
     * @return
     */
    public static ResValue parseResValue(byte[] src) {
        ResValue resValue = new ResValue();
        byte[] sizeByte = ByteUtils.copyByte(src, 0, 2);
        resValue.size = ByteUtils.byte2Short(sizeByte);

        byte[] res0Byte = ByteUtils.copyByte(src, 2, 1);
        resValue.res0 = (byte) (res0Byte[0] & 0xFF);

        byte[] dataType = ByteUtils.copyByte(src, 3, 1);
        resValue.dataType = (byte) (dataType[0] & 0xFF);

        byte[] data = ByteUtils.copyByte(src, 4, 4);
        resValue.data = ByteUtils.byte2int(data);

        return resValue;
    }

    public int getSize() {
        return 2 + 1 + 1 + 4;
    }

    public String getTypeStr() {
        switch (dataType) {
            case DataValueType.TYPE_NULL:
                return "TYPE_NULL";
            case DataValueType.TYPE_REFERENCE:
                return "TYPE_REFERENCE";
            case DataValueType.TYPE_ATTRIBUTE:
                return "TYPE_ATTRIBUTE";
            case DataValueType.TYPE_STRING:
                return "TYPE_STRING";
            case DataValueType.TYPE_FLOAT:
                return "TYPE_FLOAT";
            case DataValueType.TYPE_DIMENSION:
                return "TYPE_DIMENSION";
            case DataValueType.TYPE_FRACTION:
                return "TYPE_FRACTION";
            case DataValueType.TYPE_FIRST_INT:
                return "TYPE_FIRST_INT";
            case DataValueType.TYPE_INT_HEX:
                return "TYPE_INT_HEX";
            case DataValueType.TYPE_INT_BOOLEAN:
                return "TYPE_INT_BOOLEAN";
            case DataValueType.TYPE_FIRST_COLOR_INT:
                return "TYPE_FIRST_COLOR_INT";
            case DataValueType.TYPE_INT_COLOR_RGB8:
                return "TYPE_INT_COLOR_RGB8";
            case DataValueType.TYPE_INT_COLOR_ARGB4:
                return "TYPE_INT_COLOR_ARGB4";
            case DataValueType.TYPE_INT_COLOR_RGB4:
                return "TYPE_INT_COLOR_RGB4";
        }
        return "";
    }

    public String getResString(int index) {
        List<String> resStringList = __entry.__type.__pkg.__resFile.globalStringPool.strings;
        if (index >= resStringList.size() || index < 0) {
            return "";
        }
        return resStringList.get(index);
    }

    public String getDataStr() {
        if (dataType == DataValueType.TYPE_STRING) {
            return getResString(data);
        }
        if (dataType == DataValueType.TYPE_ATTRIBUTE) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (dataType == DataValueType.TYPE_REFERENCE) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (dataType == DataValueType.TYPE_FLOAT) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (dataType == DataValueType.TYPE_INT_HEX) {
            return String.format("0x%08X", data);
        }
        if (dataType == DataValueType.TYPE_INT_BOOLEAN) {
            return data != 0 ? "true" : "false";
        }
        if (dataType == DataValueType.TYPE_DIMENSION) {
            return Float.toString(complexToFloat(data)) +
                    DIMENSION_UNITS[data & ComplexDataValue.COMPLEX_UNIT_MASK];
        }
        if (dataType == DataValueType.TYPE_FRACTION) {
            return Float.toString(complexToFloat(data)) +
                    FRACTION_UNITS[data & ComplexDataValue.COMPLEX_UNIT_MASK];
        }
        if (dataType >= DataValueType.TYPE_FIRST_COLOR_INT && dataType <= DataValueType.TYPE_LAST_COLOR_INT) {
            return String.format("#%08X", data);
        }
        if (dataType >= DataValueType.TYPE_FIRST_INT && dataType <= DataValueType.TYPE_LAST_INT) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, dataType);
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    public static float complexToFloat(int complex) {
        return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private static final float RADIX_MULTS[] = {
            0.00390625F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F
    };

    private static final String DIMENSION_UNITS[] = {
            "px", "dip", "sp", "pt", "in", "mm", "", ""
    };

    private static final String FRACTION_UNITS[] = {
            "%", "%p", "", "", "", "", "", ""
    };
}
