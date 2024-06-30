import parser.TorrentFileParser
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis


fun main() {
    val torrentFileParser = TorrentFileParser()
    val torrentFile = Path("Path to torrent file here!")

    var data: Map<String, Any>

    val time = measureTimeMillis {
        data = torrentFileParser.parseFileToMap(torrentFile)!!
    }

    println("Parsing finished in ${time}ms !")
    println(data)
}
