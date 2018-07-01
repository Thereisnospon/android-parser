package com.thereisnospon.util.parse.type;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzr on 2018/6/20.
 *
 * @author thereisnospon
 */
public class ResFile {

    /**
     * 当前 文件 chunk 的 header
     *
     * @see ResTableHeader
     */
    public ResTableHeader header;

    /**
     * 全局字符串池，例如文件路径，字符串值等。
     */
    public ResStringPool globalStringPool;

    /**
     * 所有的资源包
     */
    public List<ResTablePackage> pkgs;


    /**
     * 解析一个 resource.arsc 文件
     *
     * @param src
     * @return
     */
    public static ResFile parseResFile(byte src[]) {
        int offset = 0;
        //文件header
        ResTableHeader header = ResTableHeader.parseResTableHeaderChunk(src);
        //越过文件header
        offset += header.header.headerSize;
        //解析全局字符串池
        ResStringPool globalString = ResStringPool.parseStringPoolChunk(src, offset);
        offset += globalString.header.header.size;

        ResFile resFile = new ResFile();
        resFile.header = header;
        resFile.globalStringPool = globalString;
        List<ResTablePackage> packages = new ArrayList<>();
        while (offset < resFile.header.header.size) {
            ResTablePackage pkg = ResTablePackage.parseResPackage(resFile, src, offset);
            packages.add(pkg);
            offset += pkg.header.header.size;
        }
        resFile.pkgs = packages;
        return resFile;
    }

    /**
     * 根据资源 id 获取对应的所有配置下的资源实体
     *
     * @param id 资源id
     * @return
     */
    public List<ResTableEntryKv> getEntry(int id) {
        int packId = (id & 0xff000000) >> 24;
        for (ResTablePackage pkg : pkgs) {
            if (pkg.header.id == packId) {
                return pkg.getEntry(id);
            }
        }
        return null;
    }
}
