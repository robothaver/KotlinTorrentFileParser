package parser

import model.Torrent

class BencodeDecoder(
    private val bytes: ByteArray,
    private val convertToObject: Boolean = false
) {
    private val torrentFileBuilder = TorrentFileBuilder()
    var iterator = 0

    fun getTorrent(): Torrent {
        return torrentFileBuilder.createTorrent()
    }

    fun getNext(): Any {
        return when (bytes[iterator].toInt().toChar()) {
            // String
            in '0'..'9' -> {
                decodeByteString()
            }
            // Number
            'i' -> {
                decodeInt()
            }
            // Dictionary
            'd' -> {
                decodeDictionary()
            }
            // List
            'l' -> {
                decodeList()
            }
            // Something is wrong
            else -> {
                throw IllegalArgumentException("File is malformed and cannot be processed")
            }
        }
    }

    private fun decodeDictionary(): MutableMap<String, Any> {
        iterator++ //  Skipping d

        val newMap = mutableMapOf<String, Any>()
        var currentKey = ""
        var lastValue: Any = ""

        while (bytes[iterator].toInt().toChar() != 'e') {
            // Is key
            if (newMap.isEmpty() || newMap[newMap.keys.last()] != "") {
                val key = getNext().toString()
                newMap[key] = ""
                currentKey = key
            } else { // Is value
                val currentValue = getNext()
                newMap[newMap.keys.last()] = currentValue

                if (convertToObject) {
                    torrentFileBuilder.processKey(currentKey, currentValue, lastValue)
                    lastValue = currentValue
                }
            }
        }
        iterator++ // Skipping the map ending

        return newMap
    }

    private fun decodeList(): MutableList<Any> {
        iterator++ // Skipping l

        val newList = mutableListOf<Any>()

        while (bytes[iterator].toInt().toChar() != 'e') {
            newList.add(getNext())
        }

        iterator++ // Skipping the list ending

        return newList
    }

    private fun decodeInt(): Long {
        iterator++ // Skipping the i

        val startIndex = iterator
        var number = 0L

        while (iterator < bytes.lastIndex) {
            if (bytes[iterator] == 'e'.code.toByte()) {
                number = String(bytes.sliceArray(startIndex..<iterator)).toLong()
                iterator++
                break
            } else {
                iterator++
            }
        }
        return number
    }

    private fun decodeByteString(): String {
        val startIndex = iterator
        var length = 0

        while (iterator < bytes.lastIndex) {
            if (bytes[iterator] == ':'.code.toByte()) {
                length = String(bytes.sliceArray(startIndex..<iterator)).toInt()
                iterator++
                break
            } else {
                iterator++
            }
        }
        val text = String(bytes.sliceArray(iterator..<iterator + length))
        iterator += length
        return text
    }
}