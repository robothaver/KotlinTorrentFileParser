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
    private var isSingleFile: Boolean = true
    private var totalSize: Long = 0L
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
                if (files.isNotEmpty()) {
                    isSingleFile = false
                    addParentNameToPaths()
                } else {
                    files.add(File(totalSize, name))
                }
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
                totalSize += value as Long
            }

            "path" -> {
                files.add(File(lastValue as Long, joinFileToPath(value as List<String>)))
            }
        }
    }

    /**
     Adds the name of the torrent to every path's start
     */
    private fun addParentNameToPaths() {
        for (i in 0..files.lastIndex) {
            val path = files[i].path
            files[i] = files[i].copy(path = "$name/$path")
        }
    }

    private fun joinFileToPath(pieces: List<String>): String {
        return pieces.joinToString("/")
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
            totalSize,
            files,
            name,
            source,
            isPrivate,
            pieceLength,
            pieces
        )
    }
}