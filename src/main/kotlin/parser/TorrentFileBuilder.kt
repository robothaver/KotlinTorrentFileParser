package parser

import model.File
import model.Torrent
import java.time.Instant
import java.time.ZoneId

class TorrentFileBuilder {
    private var announce: String = ""

    // Optional data
    private var announceList: List<List<String>>? = null
    private var azureusProperties: Map<String, Any>? = null
    private var comment: String? = null
    private var creator: String? = null
    private var creationDate: String? = null
    private var encoding: String? = null

    // Info
    private var isSingleFile: Boolean = false
    private var length: Long = 0L
    private val files: MutableList<File> = mutableListOf()
    private var name: String = ""
    private var source: String = ""
    private var isPrivate: Boolean = false
    private var pieceLength: Long = 0L
    private var pieces: String = ""

    fun processKey(key: String, value: Any, lastValue: Any) {
        when (key) {
            "announce" -> {
                announce = value as String
            }

            "announce-list" -> {
                announceList = value as List<List<String>>
            }

            "azureus_properties" -> {
                azureusProperties = value as Map<String, Any>
            }

            "comment" -> {
                comment = value as String
            }

            "created by" -> {
                creator = value as String
            }

            "creation date" -> {
                creationDate = Instant
                    .ofEpochSecond(value as Long)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .toString()
            }

            "encoding" -> {
                encoding = value as String
            }
            "name" -> {
                name = value as String
            }

            "piece length" -> {
                pieceLength = value as Long
            }

            "pieces" -> {
                pieces = value as String
            }

            "private" -> {
                isPrivate = value as Long == 1L
            }

            "source" -> {
                source = value as String
            }

            "length" -> {
                if (files.isEmpty()) {
                    length = value as Long
                    isSingleFile = true
                } else {
                    length = 0L
                    isSingleFile = false
                }
            }

            "path" -> {
                files.add(File(lastValue as Long, value as List<String>))
            }
        }
    }

    fun createTorrent(): Torrent {
        return Torrent(
            announce,
            announceList,
            azureusProperties,
            comment,
            creator,
            creationDate,
            encoding,
            isSingleFile,
            length,
            files,
            name,
            source,
            isPrivate,
            pieceLength,
            pieces
        )
    }
}