# Kotlin torrent file parser
A kotlin app that allows you to read the contents of a torrent file.

# Features

- Parse torrent file to a map
- Parse torrent file to a Torrent object

### Torrent object structure

    class Torrent(
        val announce: String,
        val announceList: List<List<String>>?,
        val azureusProperties: Map<String, Any>?,
        val comment: String?,
        val creator: String?,
        val creationDate: String?,
        val encoding: String?,
        val isSingleFile: Boolean,
        val totalSize: Long,
        val files: MutableList<File>,
        val name: String,
        val source: String,
        val isPrivate: Boolean,
        val pieceLength: Long,
        val pieces: String
    )