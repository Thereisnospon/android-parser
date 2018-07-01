package com.thereisnospon.util.parse.type;


import com.thereisnospon.util.parse.util.DebugUtils;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源包
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResTablePackage {

    /**
     * 资源包 chunk header
     */
    public ResTablePackageHeader header;

    /**
     * 类型字符串池
     */
    public ResStringPool typeStringPool;
    /**
     * key 字符串池
     */
    public ResStringPool keyStringPool;

    /**
     * 所有的资源类型及其资源项
     */
    public List<ResType> resTypes;

    /**
     * debug
     */
    public ResFile __resFile;


    /**
     * 解析一个资源包
     *
     * @param resFile
     * @param src
     * @param offset
     * @return
     */
    public static ResTablePackage parseResPackage(ResFile resFile, byte src[], int offset) {

        ResTablePackageHeader pkgHeader = ResTablePackageHeader.parsePackageHeader(src, offset);

        ResTablePackage pkg = new ResTablePackage();
        pkg.header = pkgHeader;
        pkg.__resFile = resFile;

        //这里获取类型字符串的偏移值和类型字符串的偏移值
        int keyStringPoolChunkOffset = (offset + pkg.header.keyStrings);
        int typeStringPoolChunkOffset = (offset + pkg.header.typeStrings);

        pkg.keyStringPool = ResStringPool.parseStringPoolChunk(src, keyStringPoolChunkOffset);
        pkg.typeStringPool = ResStringPool.parseStringPoolChunk(src, typeStringPoolChunkOffset);

        offset = pkg.header.header.debug_offset + +pkg.header.header.headerSize +
                pkg.typeStringPool.header.header.size + pkg.keyStringPool.header.header.size;

        List<Pair<ResTypeSpec, ResTableType>> list = new ArrayList<>();

        List<ResType> types = new ArrayList<>();
        while (offset < pkg.header.header.debug_offset + pkg.header.header.size) {
            ResChunkHeader header = ResChunkHeader.parseResChunkHeader(src, offset);
            if (header.type == ResChunkHeader.ChunkType.RES_TABLE_TYPE_SPEC_TYPE) {
                ResTypeSpec spec = ResTypeSpec.parseResTypeSpec(pkg, src, offset);
                offset += spec.header.header.size;
                ResType resType = new ResType();
                resType.spec = spec;
                resType.typeList = new ArrayList<>();
                while (offset < pkg.header.header.debug_offset + pkg.header.header.size) {
                    header = ResChunkHeader.parseResChunkHeader(src, offset);
                    if (header.type == ResChunkHeader.ChunkType.RES_TABLE_TYPE_TYPE) {
                        ResTableType type = ResTableType.parseResTypeInfo(pkg, src, offset);
                        offset += type.header.header.size;
                        resType.typeList.add(type);
                    } else {
                        break;
                    }
                }
                types.add(resType);
            }
        }
        pkg.resTypes = types;
        DebugUtils.assertTrue(offset == pkg.header.header.debug_offset + pkg.header.header.size);
        return pkg;
    }

    /**
     * 获取 资源 id 对应的所有配置下的资源实体
     *
     * @param id
     * @return
     */
    public List<ResTableEntryKv> getEntry(int id) {
        int packId = (id & 0xff000000) >> 24;
        int resTypeId = (id & 0x00ff0000) >> 16;
        int entryId = (id & 0x0000ffff);
        if (packId != header.id) {
            return null;
        }
        for (ResType type : resTypes) {
            if (type.spec.header.id == resTypeId) {
                List<ResTableEntryKv> entries = new ArrayList<>();
                for (ResTableType resTableType : type.typeList) {
                    for (ResTableEntryKv kv : resTableType.resKvList) {
                        if (kv.entry.__index == entryId) {
                            entries.add(kv);
                        }
                    }
//                    ResTableEntryKv kv = resTableType.resKvList.get(entryId);
//                    System.out.println(entryId);
//                    if (kv.entry.__index == entryId) {
//                        entries.add(kv);
//                    }
                }
                return entries;
            }
        }
        return null;
    }

    public static int getResId(int packId, int resTypeId, int entryid) {
        return (((packId) << 24) | (((resTypeId) & 0xFF) << 16) | (entryid & 0xFFFF));
    }
}
