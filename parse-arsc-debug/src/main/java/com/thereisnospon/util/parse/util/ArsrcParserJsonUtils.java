package com.thereisnospon.util.parse.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thereisnospon.util.parse.type.*;

/**
 * Created by yzr on 2018/6/20.
 */
public class ArsrcParserJsonUtils {

    public static JSONObject toJsObj(ResTableEntry entry) {
        if (entry.__empty) {
            return null;
        }
        JSONObject j = new JSONObject();
        j.put("__debug_class", entry.type());
        j.put("size", entry.size);
        j.put("flags", entry.flags);
        j.put("key", entry.key.toString() + "//" + entry.getKeyString(entry.key.index));
        if (entry instanceof ResTableMapEntry) {
            j.put("parent", ((ResTableMapEntry) entry).parent.ident);
            j.put("count", ((ResTableMapEntry) entry).count);
        }
        return j;
    }

    public static JSONObject toJsObj(ResChunkHeader header) {
        JSONObject object = new JSONObject();
        object.put("debug_offset", header.debug_offset + "//0x" + Integer.toHexString(header.debug_offset));
        object.put("type", header.type + "//0x" + Integer.toHexString(header.type));
        object.put("headerSize", header.headerSize);
        object.put("size", header.size);
        return object;
    }

    public static JSONObject toJsObj(ResFile resFile) {
        JSONObject object = new JSONObject();
        object.put("header", toJsObj(resFile.header));
        object.put("globalStringPool", toJsObj(resFile.globalStringPool));
        return object;
    }

    public static JSONObject toJsObj(ResTableEntryKv kv) {
        JSONObject o = new JSONObject();
        o.put("entry", toJsObj(kv.entry));
        if (kv instanceof ResTableMapEntryKv) {
            ResTableMapEntryKv mapKv = (ResTableMapEntryKv) kv;
            JSONArray array = new JSONArray();
            for (int i = 0; i < mapKv.listMap.size(); i++) {
                array.add(toJsObj(mapKv.listMap.get(i)));
            }
            o.put("_mapItems", array);
        } else if (kv instanceof ResTableEntrySimpleKv) {
            ResTableEntrySimpleKv simpleKv = (ResTableEntrySimpleKv) kv;
            o.put("value", toJsObj(simpleKv.value));
        }
        return o;
    }

    public static JSONObject toJsObj(ResStringPool pool) {
        JSONObject object = new JSONObject();
        object.put("_stringPoolHeader", toJsObj(pool.header));
        JSONArray indexArray = new JSONArray();
        JSONArray styleArray = new JSONArray();
        JSONArray stringS = new JSONArray();
        for (int i = 0; pool.stringIndexArray != null && i < pool.stringIndexArray.length; i++) {
            indexArray.add(pool.stringIndexArray[i]);
        }
        for (int i = 0; pool.styleIndexArray != null && i < pool.styleIndexArray.length; i++) {
            styleArray.add(pool.styleIndexArray[i]);
        }
        for (int i = 0; pool.strings != null && i < pool.strings.size(); i++) {
            stringS.add(pool.strings.get(i));
        }
        object.put("stringIndexArray", indexArray);
        object.put("styleIndexArray", styleArray);
        object.put("strings", stringS);
        return object;
    }

    public static JSONObject toJsObj(ResStringPoolHeader header) {
        JSONObject object = new JSONObject();
        object.put("_chunk_header", toJsObj(header.header));
        object.put("stringCount", header.stringCount);
        object.put("styleCount", header.styleCount);
        object.put("flags", header.flags);
        object.put("stringsStart", header.stringsStart);
        object.put("stylesStart", header.stylesStart);
        return object;
    }

    public static JSONObject toJsObj(ResTableHeader header) {
        JSONObject object = new JSONObject();
        object.put("_chunk_header", toJsObj(header.header));
        object.put("packageCount", header.packageCount);
        return object;
    }

    public static JSONObject toJsObj(ResTableMap map) {
        JSONObject j = new JSONObject();
        j.put("name", map.name.ident);
        j.put("value", toJsObj(map.value));
        return j;
    }

    public static JSONObject toJsObj(ResType resType) {
        JSONObject res = new JSONObject();
        res.put("_typeSpec", toJsObj(resType.spec));
        JSONArray array = new JSONArray();
        if (resType.typeList != null) {
            for (ResTableType type : resType.typeList) {
                array.add(toJsObj(type));
            }
        }
        res.put("_typeList", array);
        return res;
    }

    public static JSONObject toJsObj(ResTablePackage pkg) {
        JSONObject object = new JSONObject();
        object.put("_package_header", toJsObj(pkg.header));
        object.put("_typeStringPool", toJsObj(pkg.typeStringPool));
        object.put("_keyStringPool", toJsObj(pkg.keyStringPool));
        JSONArray typeSpecs = new JSONArray();
        if (pkg.resTypes != null) {
            for (ResType resType : pkg.resTypes) {
                typeSpecs.add(toJsObj(resType));
            }
        }
        object.put("__resTypes", typeSpecs);
        return object;
    }

    public static JSONObject toJsObj(ResTablePackageHeader header) {
        JSONObject object = new JSONObject();
        object.put("_chunk_header", toJsObj(header.header));
        object.put("id", header.id);
        object.put("name", header.nameByte.toString() + "//" + header.__debugPkgName());
        object.put("typeStrings", header.typeStrings);
        object.put("lastPublicType", header.lastPublicType);
        object.put("keyStrings", header.keyStrings);
        object.put("lastPublicKey", header.lastPublicKey);
        return object;
    }

    public static JSONObject toJsObj(ResTableType type) {
        JSONObject object = new JSONObject();
        object.put("_type_header", toJsObj(type.header));
        JSONArray array = new JSONArray();
        for (int i = 0; type.entryArray != null && i < type.entryArray.length; i++) {
            array.add(type.entryArray[i]);
        }
        JSONArray kvarra = new JSONArray();
        if (type.resKvList != null) {
            for (ResTableEntryKv kv : type.resKvList) {
                if (kv instanceof ResTableMapEntryKv) {
                    kvarra.add(toJsObj(kv));
                } else if (kv instanceof ResTableEntrySimpleKv) {
                    kvarra.add(toJsObj(kv));
                }
            }
        }
        object.put("_kvlist", kvarra);
        object.put("indexArray", array);
        return object;
    }

    public static JSONObject toJsObj(ResTableTypeHeader header) {
        JSONObject js = new JSONObject();
        //js.put("_debug_offset",debug_offset+"//0x"+Integer.toHexString(debug_offset));
        js.put("_chunk_header", toJsObj(header.header));
        js.put("id", header.id);
        js.put("res0", header.res0);
        js.put("res1", header.res1);
        js.put("entryCount", header.entryCount);
        js.put("entriesStart", header.entriesStart);
        js.put("resConfig", toJsObj(header.resConfig));

        return js;
    }

    public static JSONObject toJsObj(ResTableTypeSpecHeader header) {
        JSONObject object = new JSONObject();
        object.put("header", toJsObj(header.header));
        object.put("id", header.id);
        object.put("res0", header.res0);
        object.put("res1", header.res1);
        object.put("entryCount", header.entryCount);

        return object;
    }

    public static JSONObject toJsObj(ResTypeSpec spec) {
        JSONObject object = new JSONObject();
        object.put("header", toJsObj(spec.header));
        JSONArray a = new JSONArray();
        for (int i = 0; spec.array != null && i < spec.array.length; i++) {
            a.add(spec.array[i]);
        }
        object.put("array", a);
        return object;
    }

    public static JSONObject toJsObj(ResValue value) {
        JSONObject j = new JSONObject();
        j.put("size", value.size);
        j.put("res0", value.res0);
        j.put("dataType", value.dataType + "//" + value.getTypeStr());
        j.put("data", value.data + "//" + value.getDataStr());
        return j;
    }


    private static JSONObject kv(String key, Object value, byte[] bytes) {

        JSONObject o = new JSONObject();
        o.put("key", key);

        StringBuilder str = new StringBuilder(value.toString());
        if (value.getClass() == int.class || value.getClass() == Integer.class) {
            str.append("//0x" + Integer.toHexString((int) value));
        } else if (value.getClass() == short.class || value.getClass() == Short.class) {
            str.append("//0x" + Integer.toHexString((short) value));
        } else if (value.getClass() == byte.class || value.getClass() == Byte.class) {
            str.append("//0x" + Integer.toHexString((byte) value));
        }

        o.put("value", str);
        o.put("bytes", bstr(bytes));


        return o;
    }

    private static String bstr(byte bytes[]) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toHexString(b) + " ");
        }
        return sb.toString();
    }

    private interface si {
        JSONObject s();
    }

    private static JSONObject u(si... sis) {
        JSONObject object = new JSONObject();
        for (int i = 0; i < sis.length; i++) {
            object.put("u" + i, sis[i].s());
        }
        return object;
    }

    private static JSONObject g(byte[] bytes, si... sis) {
        JSONObject object = new JSONObject();
        for (int i = 0; i < sis.length; i++) {
            object.put("s" + i, sis[i].s());
        }
        object.put("bytes", bstr(bytes));
        return object;
    }


    public static JSONArray toJsObj(final ResTableConfig config) {


        byte src[] = config.__raw_bytes;

        final byte[] sizeByte = ByteUtils.copyByte(src, 0, 4);
        config.size = ByteUtils.byte2int(sizeByte);


        JSONArray a = new JSONArray();

        a.add(g(sizeByte, new si() {
            @Override
            public JSONObject s() {
                return kv("size", config.size, sizeByte);
            }
        }));

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
        final byte[] mccByte = ByteUtils.copyByte(src, 4, 2);
        config.mcc = ByteUtils.byte2Short(mccByte);
        final byte[] mncByte = ByteUtils.copyByte(src, 6, 2);
        config.mnc = ByteUtils.byte2Short(mncByte);
        final byte[] imsiByte = ByteUtils.copyByte(src, 4, 4);
        config.imsi = ByteUtils.byte2int(imsiByte);

        a.add(g(imsiByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("mcc", config.mcc, mccByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("mnc", config.mnc, mncByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("imsi", config.imsi, imsiByte);
            }
        }));

        /*
        union {
            struct {
                char language[2];
                char country[2];
            };
            uint32_t locale; 4
        };*/
        final byte[] languageByte = ByteUtils.copyByte(src, 8, 2);
        config.language = languageByte;
        final byte[] countryByte = ByteUtils.copyByte(src, 10, 2);
        config.country = countryByte;
        final byte[] localeByte = ByteUtils.copyByte(src, 8, 4);
        config.locale = ByteUtils.byte2int(localeByte);

        a.add(g(localeByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("language", config.language, languageByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("country", config.country, countryByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("locale", config.locale, localeByte);
            }
        }));

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
        final byte[] orientationByte = ByteUtils.copyByte(src, 12, 1);
        config.orientation = orientationByte[0];
        final byte[] touchscreenByte = ByteUtils.copyByte(src, 13, 1);
        config.touchscreen = touchscreenByte[0];
        final byte[] densityByte = ByteUtils.copyByte(src, 14, 2);
        config.density = ByteUtils.byte2Short(densityByte);
        final byte[] screenTypeByte = ByteUtils.copyByte(src, 12, 4);
        config.screenType = ByteUtils.byte2int(screenTypeByte);


        a.add(g(screenTypeByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("orientation", config.orientation, orientationByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("touchscreen", config.touchscreen, touchscreenByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("density", config.density, densityByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("screenType", config.screenType, screenTypeByte);
            }
        }));
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
        final byte[] keyboardByte = ByteUtils.copyByte(src, 16, 1);
        config.keyboard = keyboardByte[0];
        final byte[] navigationByte = ByteUtils.copyByte(src, 17, 1);
        config.navigation = navigationByte[0];
        final byte[] inputFlagsByte = ByteUtils.copyByte(src, 18, 1);
        config.inputFlags = inputFlagsByte[0];
        final byte[] inputPad0Byte = ByteUtils.copyByte(src, 19, 1);
        config.inputPad0 = inputPad0Byte[0];
        final byte[] inputByte = ByteUtils.copyByte(src, 16, 4);
        config.input = ByteUtils.byte2int(inputByte);

        a.add(g(inputByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("keyboard", config.keyboard, keyboardByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("navigation", config.navigation, navigationByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("inputFlags", config.inputFlags, inputFlagsByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("inputPad0", config.inputPad0, inputPad0Byte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("input", config.input, inputByte);
            }
        }));
        /*
         union {
            struct {
                uint16_t screenWidth;
                uint16_t screenHeight;
            };
            uint32_t screenSize; 4
        };
         */
        final byte[] screenWidthByte = ByteUtils.copyByte(src, 20, 2);
        config.screenWidth = ByteUtils.byte2Short(screenWidthByte);
        final byte[] screenHeightByte = ByteUtils.copyByte(src, 22, 2);
        config.screenHeight = ByteUtils.byte2Short(screenHeightByte);
        final byte[] screenSizeByte = ByteUtils.copyByte(src, 20, 4);
        config.screenSize = ByteUtils.byte2int(screenSizeByte);


        a.add(g(screenSizeByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("screenWidth", config.screenWidth, screenWidthByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("screenHeight", config.screenHeight, screenHeightByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("screenSize", config.screenSize, screenSizeByte);
            }
        }));
        /*
         union {
            struct {
                uint16_t sdkVersion;
                uint16_t minorVersion;
            };
            uint32_t version; 4
         };
         */
        final byte[] sdVersionByte = ByteUtils.copyByte(src, 24, 2);
        config.sdVersion = ByteUtils.byte2Short(sdVersionByte);

        final byte[] minorVersionByte = ByteUtils.copyByte(src, 26, 2);
        config.minorVersion = ByteUtils.byte2Short(minorVersionByte);
        final byte[] versionByte = ByteUtils.copyByte(src, 24, 4);
        config.version = ByteUtils.byte2int(versionByte);

        a.add(g(versionByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("sdVersion", config.sdVersion, sdVersionByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("minorVersion", config.minorVersion, minorVersionByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("version", config.version, versionByte);
            }
        }));

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
        final byte[] screenLayoutByte = ByteUtils.copyByte(src, 28, 1);
        config.screenLayout = screenLayoutByte[0];
        final byte[] uiModeByte = ByteUtils.copyByte(src, 29, 1);
        config.uiMode = uiModeByte[0];
        final byte[] smallestScreenWidthDpByte = ByteUtils.copyByte(src, 30, 2);
        config.smallestScreenWidthDp = ByteUtils.byte2Short(smallestScreenWidthDpByte);
        final byte[] screenConfigByte = ByteUtils.copyByte(src, 28, 4);
        config.screenConfig = ByteUtils.byte2int(screenConfigByte);


        a.add(g(screenConfigByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("screenLayout", config.screenLayout, screenLayoutByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("uiMode", config.uiMode, uiModeByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("smallestScreenWidthDp", config.smallestScreenWidthDp, smallestScreenWidthDpByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("screenConfig", config.screenConfig, screenConfigByte);
            }
        }));
        /*
        union {
            struct {
                uint16_t screenWidthDp;
                uint16_t screenHeightDp;
            };
            uint32_t screenSizeDp; 4
        };
        */
        final byte[] screenWidthDpByte = ByteUtils.copyByte(src, 32, 2);
        config.screenWidthDp = ByteUtils.byte2Short(screenWidthDpByte);
        final byte[] screenHeightDpByte = ByteUtils.copyByte(src, 34, 2);
        config.screenHeightDp = ByteUtils.byte2Short(screenHeightDpByte);
        final byte[] screenSizeDpByte = ByteUtils.copyByte(src, 32, 4);
        config.screenSizeDp = ByteUtils.byte2int(screenSizeDpByte);

        a.add(g(screenSizeDpByte, new si() {
            @Override
            public JSONObject s() {
                return u(new si() {
                    @Override
                    public JSONObject s() {
                        return kv("screenWidthDp", config.screenWidthDp, screenWidthDpByte);
                    }
                }, new si() {
                    @Override
                    public JSONObject s() {
                        return kv("screenHeightDp", config.screenHeightDp, screenHeightDpByte);
                    }
                });
            }
        }, new si() {
            @Override
            public JSONObject s() {
                return kv("screenSizeDp", config.screenSizeDp, screenSizeDpByte);
            }
        }));

        final byte[] localeScriptByte = ByteUtils.copyByte(src, 36, 4);
        config.localeScript = localeScriptByte;

        a.add(g(localeScriptByte, new si() {
            @Override
            public JSONObject s() {
                return kv("localeScript", config.localeScript, localeScriptByte);
            }
        }));

        final byte[] localeVariantByte = ByteUtils.copyByte(src, 40, 8);
        config.localeVariant = localeVariantByte;

        a.add(g(localeVariantByte, new si() {
            @Override
            public JSONObject s() {
                return kv("localeVariant", config.localeVariant, localeVariantByte);
            }
        }));

        return a;
    }


}
