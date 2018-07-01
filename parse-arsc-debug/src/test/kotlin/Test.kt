import com.thereisnospon.util.parse.ResourceArscFileParser
import com.thereisnospon.util.parse.util.ZipUtil

/**
 * Created by yzr on 2018/7/1.
 */


fun main(args: Array<String>) {

    val apkPath = "/Users/yzr/Desktop/AndroidFileParse/arsc-demo/build/outputs/apk/debug/arsc-demo-debug.apk"
    val resFile = ResourceArscFileParser.parseFile(ZipUtil.getEntryInputStream(apkPath, "resources.arsc"))
    resFile.globalStringPool.strings.forEach {
        println(it)
    }
}