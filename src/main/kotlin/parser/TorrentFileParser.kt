package parser

import model.Torrent
import java.nio.file.Files
import java.nio.file.Path

class TorrentFileParser {
    private lateinit var bencodeDecoder: BencodeDecoder

    fun parseFileToMap(path: Path): Map<String, Any>? {
        val bytes = openFile(path)
        return if (bytes != null) {
            bencodeDecoder = BencodeDecoder(bytes)
            getTorrentMap()
        } else {
            null
        }
    }

    fun parseFile(path: Path): Torrent? {
        val bytes = openFile(path)
        return if (bytes != null) {
            bencodeDecoder = BencodeDecoder(bytes, convertToObject = true)
            bencodeDecoder.getNext() // Start parsing the file
            bencodeDecoder.getTorrent()
        } else {
            null
        }
    }


    private fun openFile(path: Path): ByteArray? {
        return try {
            Files.readAllBytes(path)
        } catch (e: Exception) {
            println("Reading file failed!\n$e")
            null
        }
    }

    private fun getTorrentMap(): Map<String, Any>? {
        return try {
            bencodeDecoder.getNext() as Map<String, Any>
        } catch (e: Exception) {
            println("Parsing file failed!\n$e")
            null
        }
    }
}