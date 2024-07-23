import parser.TorrentFileParser
import kotlin.io.path.Path
import kotlin.system.measureTimeMillis


fun main() {
    val torrentFileParser = TorrentFileParser()
    val torrentFilePath = Path("Path to torrent file here")

    val time = measureTimeMillis {
        val torrent = torrentFileParser.parseFile(torrentFilePath)
        println(torrent!!.files)
    }
    println("Parsing finished in $time ms")
}