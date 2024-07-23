package model

data class Torrent(
    val announce: String,
    val announceList: List<List<String>>?,
    val azureusProperties: Map<String, Any>?,
    val comment: String?,
    val creator: String?,
    val creationDate: String?,
    val encoding: String?,
    val isSingleFile: Boolean,
    val length: Long,
    val files: MutableList<File>,
    val name: String?,
    val source: String,
    val isPrivate: Boolean,
    val pieceLength: Long,
    val pieces: String
)