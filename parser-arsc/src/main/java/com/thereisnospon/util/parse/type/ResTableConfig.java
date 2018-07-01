package com.thereisnospon.util.parse.type;

import com.thereisnospon.util.parse.util.ByteUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a particular resource configuration.
 * Created by yzr on 2018/6/20.
 */
public class ResTableConfig {


    public byte[] __raw_bytes;

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Offset {

        public int offset();

        public int group();

        public int struct() default -1;
    }

    public String debugStr;

    /**
     * uint32_t size;
     * Number of bytes in this structure.
     */
    @Offset(offset = 0, group = 0)
    public int size;

    /*
    union {
        struct {
            uint16_t mcc;
            uint16_t mnc;
        };
        uint32_t imsi;
    };
    */
    @Offset(offset = 4, group = 1, struct = 0)
    // Mobile country code (from SIM).  0 means "any".
    public short mcc;

    @Offset(offset = 6, group = 1, struct = 0)
    // Mobile network code (from SIM).  0 means "any".
    public short mnc;

    @Offset(offset = 4, group = 1)
    public int imsi;

    // This field can take three different forms:
    // - \0\0 means "any".
    //
    // - Two 7 bit ascii values interpreted as ISO-639-1 language
    //   codes ('fr', 'en' etc. etc.). The high bit for both bytes is
    //   zero.
    //
    // - A single 16 bit little endian packed value representing an
    //   ISO-639-2 3 letter language code. This will be of the form:
    //
    //   {1, t, t, t, t, t, s, s, s, s, s, f, f, f, f, f}
    //
    //   bit[0, 4] = first letter of the language code
    //   bit[5, 9] = second letter of the language code
    //   bit[10, 14] = third letter of the language code.
    //   bit[15] = 1 always
    //
    // For backwards compatibility, languages that have unambiguous
    // two letter codes are represented in that format.
    //
    // The layout is always bigendian irrespective of the runtime
    // architecture.
    @Offset(offset = 8, group = 2, struct = 0)
    public byte[] language = new byte[2];
    // This field can take three different forms:
    // - \0\0 means "any".
    //
    // - Two 7 bit ascii values interpreted as 2 letter region
    //   codes ('US', 'GB' etc.). The high bit for both bytes is zero.
    //
    // - An UN M.49 3 digit region code. For simplicity, these are packed
    //   in the same manner as the language codes, though we should need
    //   only 10 bits to represent them, instead of the 15.
    //
    // The layout is always bigendian irrespective of the runtime
    // architecture.
    @Offset(offset = 10, group = 2, struct = 0)
    public byte[] country = new byte[2];
    @Offset(offset = 8, group = 2)
    public int locale;

    /*
    union {
        struct {
            char language[2];
            char country[2];
        };
        uint32_t locale;
    };

    */

    interface ORIENTATION {
        int ORIENTATION_ANY = Constants.ACONFIGURATION_ORIENTATION_ANY;
        int ORIENTATION_PORT = Constants.ACONFIGURATION_ORIENTATION_PORT;
        int ORIENTATION_LAND = Constants.ACONFIGURATION_ORIENTATION_LAND;
        int ORIENTATION_SQUARE = Constants.ACONFIGURATION_ORIENTATION_SQUARE;
    }

    interface TOUCHSCREEN {
        int TOUCHSCREEN_ANY = Constants.ACONFIGURATION_TOUCHSCREEN_ANY;
        int TOUCHSCREEN_NOTOUCH = Constants.ACONFIGURATION_TOUCHSCREEN_NOTOUCH;
        int TOUCHSCREEN_STYLUS = Constants.ACONFIGURATION_TOUCHSCREEN_STYLUS;
        int TOUCHSCREEN_FINGER = Constants.ACONFIGURATION_TOUCHSCREEN_FINGER;
    }

    interface DENSITY {
        int DENSITY_DEFAULT = Constants.ACONFIGURATION_DENSITY_DEFAULT;
        int DENSITY_LOW = Constants.ACONFIGURATION_DENSITY_LOW;
        int DENSITY_MEDIUM = Constants.ACONFIGURATION_DENSITY_MEDIUM;
        int DENSITY_TV = Constants.ACONFIGURATION_DENSITY_TV;
        int DENSITY_HIGH = Constants.ACONFIGURATION_DENSITY_HIGH;
        int DENSITY_XHIGH = Constants.ACONFIGURATION_DENSITY_XHIGH;
        int DENSITY_XXHIGH = Constants.ACONFIGURATION_DENSITY_XXHIGH;
        int DENSITY_XXXHIGH = Constants.ACONFIGURATION_DENSITY_XXXHIGH;
        int DENSITY_ANY = Constants.ACONFIGURATION_DENSITY_ANY;
        int DENSITY_NONE = Constants.ACONFIGURATION_DENSITY_NONE;
    }

    /*
    union {
        struct {
            uint8_t orientation;
            uint8_t touchscreen;
            uint16_t density;
        };
        uint32_t screenType;
    };
    */
    @Offset(offset = 12, group = 3, struct = 0)
    public byte orientation;
    @Offset(offset = 13, group = 3, struct = 0)
    public byte touchscreen;
    @Offset(offset = 14, group = 3, struct = 0)
    public short density;
    @Offset(offset = 12, group = 3)
    public int screenType;

    interface KEYBOARD {
        int KEYBOARD_ANY = Constants.ACONFIGURATION_KEYBOARD_ANY;
        int KEYBOARD_NOKEYS = Constants.ACONFIGURATION_KEYBOARD_NOKEYS;
        int KEYBOARD_QWERTY = Constants.ACONFIGURATION_KEYBOARD_QWERTY;
        int KEYBOARD_12KEY = Constants.ACONFIGURATION_KEYBOARD_12KEY;
    }

    interface NAVIGATION {
        int NAVIGATION_ANY = Constants.ACONFIGURATION_NAVIGATION_ANY;
        int NAVIGATION_NONAV = Constants.ACONFIGURATION_NAVIGATION_NONAV;
        int NAVIGATION_DPAD = Constants.ACONFIGURATION_NAVIGATION_DPAD;
        int NAVIGATION_TRACKBALL = Constants.ACONFIGURATION_NAVIGATION_TRACKBALL;
        int NAVIGATION_WHEEL = Constants.ACONFIGURATION_NAVIGATION_WHEEL;
    }

    interface KEYSHIDDEN {
        int MASK_KEYSHIDDEN = 0x0003;
        int KEYSHIDDEN_ANY = Constants.ACONFIGURATION_KEYSHIDDEN_ANY;
        int KEYSHIDDEN_NO = Constants.ACONFIGURATION_KEYSHIDDEN_NO;
        int KEYSHIDDEN_YES = Constants.ACONFIGURATION_KEYSHIDDEN_YES;
        int KEYSHIDDEN_SOFT = Constants.ACONFIGURATION_KEYSHIDDEN_SOFT;
    }

    interface NAVHIDDEN {
        int MASK_NAVHIDDEN = 0x000c;
        int SHIFT_NAVHIDDEN = 2;
        int NAVHIDDEN_ANY = Constants.ACONFIGURATION_NAVHIDDEN_ANY << SHIFT_NAVHIDDEN;
        int NAVHIDDEN_NO = Constants.ACONFIGURATION_NAVHIDDEN_NO << SHIFT_NAVHIDDEN;
        int NAVHIDDEN_YES = Constants.ACONFIGURATION_NAVHIDDEN_YES << SHIFT_NAVHIDDEN;
    }

    /*
    union {
        struct {
            uint8_t keyboard;
            uint8_t navigation;
            uint8_t inputFlags;
            uint8_t inputPad0;
        };
        uint32_t input;
    };
    */
    @Offset(offset = 16, group = 4, struct = 0)
    public byte keyboard;
    @Offset(offset = 17, group = 4, struct = 0)
    public byte navigation;
    @Offset(offset = 18, group = 4, struct = 0)
    public byte inputFlags;
    @Offset(offset = 19, group = 4, struct = 0)
    public byte inputPad0;
    @Offset(offset = 16, group = 4)
    public int input;
    //--------------


    interface SCREENWIDTH {
        int SCREENWIDTH_ANY = 0;
    }


    interface SCREENHEIGHT {
        int SCREENHEIGHT_ANY = 0;
    }

    /*
    union {
        struct {
            uint16_t screenWidth;
            uint16_t screenHeight;
        };
        uint32_t screenSize;
    };
    */
    @Offset(offset = 20, group = 5, struct = 0)
    public short screenWidth;
    @Offset(offset = 22, group = 5, struct = 0)
    public short screenHeight;
    @Offset(offset = 20, group = 5)
    public int screenSize;


    interface SDKVERSION_ANY {
        int SDKVERSION_ANY = 0;
    }


    interface MINORVERSION_ANY {
        int MINORVERSION_ANY = 0;
    }

    /*
    union {
        struct {
            uint16_t sdkVersion;
            // For now minorVersion must always be 0!!!  Its meaning
            // is currently undefined.
            uint16_t minorVersion;
        };
        uint32_t version;
    };
    */
    @Offset(offset = 24, group = 6, struct = 0)
    public short sdVersion;
    @Offset(offset = 26, group = 6, struct = 0)
    public short minorVersion;
    @Offset(offset = 24, group = 6)
    public int version;


    interface SCREEN {
        // screenLayout bits for screen size class.
        int MASK_SCREENSIZE = 0x0f;
        int SCREENSIZE_ANY = Constants.ACONFIGURATION_SCREENSIZE_ANY;
        int SCREENSIZE_SMALL = Constants.ACONFIGURATION_SCREENSIZE_SMALL;
        int SCREENSIZE_NORMAL = Constants.ACONFIGURATION_SCREENSIZE_NORMAL;
        int SCREENSIZE_LARGE = Constants.ACONFIGURATION_SCREENSIZE_LARGE;
        int SCREENSIZE_XLARGE = Constants.ACONFIGURATION_SCREENSIZE_XLARGE;

        // screenLayout bits for wide/long screen variation.
        int MASK_SCREENLONG = 0x30;
        int SHIFT_SCREENLONG = 4;
        int SCREENLONG_ANY = Constants.ACONFIGURATION_SCREENLONG_ANY << SHIFT_SCREENLONG;
        int SCREENLONG_NO = Constants.ACONFIGURATION_SCREENLONG_NO << SHIFT_SCREENLONG;
        int SCREENLONG_YES = Constants.ACONFIGURATION_SCREENLONG_YES << SHIFT_SCREENLONG;

        // screenLayout bits for layout direction.
        int MASK_LAYOUTDIR = 0xC0;
        int SHIFT_LAYOUTDIR = 6;
        int LAYOUTDIR_ANY = Constants.ACONFIGURATION_LAYOUTDIR_ANY << SHIFT_LAYOUTDIR;
        int LAYOUTDIR_LTR = Constants.ACONFIGURATION_LAYOUTDIR_LTR << SHIFT_LAYOUTDIR;
        int LAYOUTDIR_RTL = Constants.ACONFIGURATION_LAYOUTDIR_RTL << SHIFT_LAYOUTDIR;
    }


    interface UI_MODE {
        // uiMode bits for the mode type.
        int MASK_UI_MODE_TYPE = 0x0f;
        int UI_MODE_TYPE_ANY = Constants.ACONFIGURATION_UI_MODE_TYPE_ANY;
        int UI_MODE_TYPE_NORMAL = Constants.ACONFIGURATION_UI_MODE_TYPE_NORMAL;
        int UI_MODE_TYPE_DESK = Constants.ACONFIGURATION_UI_MODE_TYPE_DESK;
        int UI_MODE_TYPE_CAR = Constants.ACONFIGURATION_UI_MODE_TYPE_CAR;
        int UI_MODE_TYPE_TELEVISION = Constants.ACONFIGURATION_UI_MODE_TYPE_TELEVISION;
        int UI_MODE_TYPE_APPLIANCE = Constants.ACONFIGURATION_UI_MODE_TYPE_APPLIANCE;
        int UI_MODE_TYPE_WATCH = Constants.ACONFIGURATION_UI_MODE_TYPE_WATCH;

        // uiMode bits for the night switch.
        int MASK_UI_MODE_NIGHT = 0x30;
        int SHIFT_UI_MODE_NIGHT = 4;
        int UI_MODE_NIGHT_ANY = Constants.ACONFIGURATION_UI_MODE_NIGHT_ANY << SHIFT_UI_MODE_NIGHT;
        int UI_MODE_NIGHT_NO = Constants.ACONFIGURATION_UI_MODE_NIGHT_NO << SHIFT_UI_MODE_NIGHT;
        int UI_MODE_NIGHT_YES = Constants.ACONFIGURATION_UI_MODE_NIGHT_YES << SHIFT_UI_MODE_NIGHT;
    }

    /*
    union {
        struct {
            uint8_t screenLayout;
            uint8_t uiMode;
            uint16_t smallestScreenWidthDp;
        };
        uint32_t screenConfig;
    };
    */
    /**
     * @see SCREEN
     */
    @Offset(offset = 28, group = 7, struct = 0)
    public byte screenLayout;
    /**
     * @see UI_MODE
     */
    @Offset(offset = 29, group = 7, struct = 0)
    public byte uiMode;
    @Offset(offset = 30, group = 7, struct = 0)
    public short smallestScreenWidthDp;
    @Offset(offset = 28, group = 7)
    public int screenConfig;
    //------------------

    /*
    union {
        struct {
            uint16_t screenWidthDp;
            uint16_t screenHeightDp;
        };
        uint32_t screenSizeDp;
    };
    */
    @Offset(offset = 32, group = 8, struct = 0)
    public short screenWidthDp;
    @Offset(offset = 34, group = 8, struct = 0)
    public short screenHeightDp;
    @Offset(offset = 32, group = 8)
    public int screenSizeDp;

    // The ISO-15924 short name for the script corresponding to this
    // configuration. (eg. Hant, Latn, etc.). Interpreted in conjunction with
    // the locale field.
    @Offset(offset = 36, group = 9)
    public byte[] localeScript = new byte[4];
    // A single BCP-47 variant subtag. Will vary in length between 5 and 8
    // chars. Interpreted in conjunction with the locale field.
    @Offset(offset = 40, group = 10)
    public byte[] localeVariant = new byte[8];


    interface SCREENROUND {
        // screenLayout2 bits for round/notround.
        int MASK_SCREENROUND = 0x03;
        int SCREENROUND_ANY = Constants.ACONFIGURATION_SCREENROUND_ANY;
        int SCREENROUND_NO = Constants.ACONFIGURATION_SCREENROUND_NO;
        int SCREENROUND_YES = Constants.ACONFIGURATION_SCREENROUND_YES;
    }

    /*
    // An extension of screenConfig.
    union {
        struct {
            uint8_t screenLayout2;      // Contains round/notround qualifier.
            uint8_t screenConfigPad1;   // Reserved padding.
            uint16_t screenConfigPad2;  // Reserved padding.
        };
        uint32_t screenConfig2;
    };
    */


    /**
     * 解析 ResTableConfig （资源对屏幕大小，语言地区的配置）
     *
     * @param src
     * @return
     */
    public static ResTableConfig parseResTableConfig(byte[] src) {

        ResTableConfig config = new ResTableConfig();
        config.__raw_bytes = ByteUtils.copyByte(src, 0, 48);


        byte[] sizeByte = ByteUtils.copyByte(src, 0, 4);

        config.size = ByteUtils.byte2int(sizeByte);

        /*
        union {
            struct {
                // Mobile country code (from SIM).  0 means "any".
                uint16_t mcc;
                // Mobile network code (from SIM).  0 means "any".
                uint16_t mnc;
            };
            uint32_t imsi; 8
        };
         */
        byte[] mccByte = ByteUtils.copyByte(src, 4, 2);
        config.mcc = ByteUtils.byte2Short(mccByte);
        byte[] mncByte = ByteUtils.copyByte(src, 6, 2);
        config.mnc = ByteUtils.byte2Short(mncByte);
        byte[] imsiByte = ByteUtils.copyByte(src, 4, 4);
        config.imsi = ByteUtils.byte2int(imsiByte);



        /*
        union {
            struct {
                char language[2];
                char country[2];
            };
            uint32_t locale; 4
        };*/
        byte[] languageByte = ByteUtils.copyByte(src, 8, 2);
        config.language = languageByte;
        byte[] countryByte = ByteUtils.copyByte(src, 10, 2);
        config.country = countryByte;
        byte[] localeByte = ByteUtils.copyByte(src, 8, 4);
        config.locale = ByteUtils.byte2int(localeByte);



        /*
        union {
            struct {
                uint8_t orientation;
                uint8_t touchscreen;
                uint16_t density;
            };
            uint32_t screenType; 4
        };
        */
        byte[] orientationByte = ByteUtils.copyByte(src, 12, 1);
        config.orientation = orientationByte[0];
        byte[] touchscreenByte = ByteUtils.copyByte(src, 13, 1);
        config.touchscreen = touchscreenByte[0];
        byte[] densityByte = ByteUtils.copyByte(src, 14, 2);
        config.density = ByteUtils.byte2Short(densityByte);
        byte[] screenTypeByte = ByteUtils.copyByte(src, 12, 4);
        config.screenType = ByteUtils.byte2int(screenTypeByte);


        /*
        union {
            struct {
                uint8_t keyboard;
                uint8_t navigation;
                uint8_t inputFlags;
                uint8_t inputPad0;
            };
            uint32_t input; 4
         };
         */
        byte[] keyboardByte = ByteUtils.copyByte(src, 16, 1);
        config.keyboard = keyboardByte[0];
        byte[] navigationByte = ByteUtils.copyByte(src, 17, 1);
        config.navigation = navigationByte[0];
        byte[] inputFlagsByte = ByteUtils.copyByte(src, 18, 1);
        config.inputFlags = inputFlagsByte[0];
        byte[] inputPad0Byte = ByteUtils.copyByte(src, 19, 1);
        config.inputPad0 = inputPad0Byte[0];
        byte[] inputByte = ByteUtils.copyByte(src, 16, 4);
        config.input = ByteUtils.byte2int(inputByte);

        /*
         union {
            struct {
                uint16_t screenWidth;
                uint16_t screenHeight;
            };
            uint32_t screenSize; 4
        };
         */
        byte[] screenWidthByte = ByteUtils.copyByte(src, 20, 2);
        config.screenWidth = ByteUtils.byte2Short(screenWidthByte);
        byte[] screenHeightByte = ByteUtils.copyByte(src, 22, 2);
        config.screenHeight = ByteUtils.byte2Short(screenHeightByte);
        byte[] screenSizeByte = ByteUtils.copyByte(src, 20, 4);
        config.screenSize = ByteUtils.byte2int(screenSizeByte);

        /*
         union {
            struct {
                uint16_t sdkVersion;
                uint16_t minorVersion;
            };
            uint32_t version; 4
         };
         */
        byte[] sdVersionByte = ByteUtils.copyByte(src, 24, 2);
        config.sdVersion = ByteUtils.byte2Short(sdVersionByte);

        byte[] minorVersionByte = ByteUtils.copyByte(src, 26, 2);
        config.minorVersion = ByteUtils.byte2Short(minorVersionByte);
        byte[] versionByte = ByteUtils.copyByte(src, 24, 4);
        config.version = ByteUtils.byte2int(versionByte);


        /*
          union {
            struct {
                uint8_t screenLayout;
                uint8_t uiMode;
                uint16_t smallestScreenWidthDp;
            };
            uint32_t screenConfig; 4
         };
         */
        byte[] screenLayoutByte = ByteUtils.copyByte(src, 28, 1);
        config.screenLayout = screenLayoutByte[0];
        byte[] uiModeByte = ByteUtils.copyByte(src, 29, 1);
        config.uiMode = uiModeByte[0];
        byte[] smallestScreenWidthDpByte = ByteUtils.copyByte(src, 30, 2);
        config.smallestScreenWidthDp = ByteUtils.byte2Short(smallestScreenWidthDpByte);
        byte[] screenConfigByte = ByteUtils.copyByte(src, 28, 4);
        config.screenConfig = ByteUtils.byte2int(screenConfigByte);

        /*
        union {
            struct {
                uint16_t screenWidthDp;
                uint16_t screenHeightDp;
            };
            uint32_t screenSizeDp; 4
        };
        */
        byte[] screenWidthDpByte = ByteUtils.copyByte(src, 32, 2);
        config.screenWidthDp = ByteUtils.byte2Short(screenWidthDpByte);
        byte[] screenHeightDpByte = ByteUtils.copyByte(src, 34, 2);
        config.screenHeightDp = ByteUtils.byte2Short(screenHeightDpByte);
        byte[] screenSizeDpByte = ByteUtils.copyByte(src, 32, 4);
        config.screenSizeDp = ByteUtils.byte2int(screenSizeDpByte);


        byte[] localeScriptByte = ByteUtils.copyByte(src, 36, 4);
        config.localeScript = localeScriptByte;

        byte[] localeVariantByte = ByteUtils.copyByte(src, 40, 8);
        config.localeVariant = localeVariantByte;

        return config;
    }

    public int getSize() {
        return 48;
    }

    @Override
    public String toString() {
        return debugStr;
    }
}
