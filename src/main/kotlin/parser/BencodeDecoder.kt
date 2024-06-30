package parser

class BencodeDecoder(private val bytes: ByteArray) {
    var iterator = 0

    fun getNext(): Any {
        return when(val currentChar = bytes[iterator].toInt().toChar()) {
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
                iterator++
                decodeDictionary()
            }
            // List
            'l' -> {
                iterator++
                decodeList()
            }
            // Something is wrong
            else -> {
                iterator++
                println("No solution found for ($currentChar) at index: $iterator")
            }
        }
    }

    private fun decodeDictionary(): MutableMap<String, Any> {
        val newMap = mutableMapOf<String, Any>()

        while (bytes[iterator].toInt().toChar() != 'e') {
            if (newMap.isEmpty() || newMap[newMap.keys.last()] != "") {
                newMap[getNext().toString()] = ""
            } else {
                newMap[newMap.keys.last()] = getNext()
            }
        }
        iterator++ // Skipping the map ending

        return newMap
    }

    private fun decodeList(): MutableList<Any> {
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
        var lenght = 0

        while (iterator < bytes.lastIndex) {
            if (bytes[iterator] == ':'.code.toByte()) {
                lenght = String(bytes.sliceArray(startIndex..<iterator)).toInt()
                iterator++
                break
            } else {
                iterator++
            }
        }
        val text = String(bytes.sliceArray(iterator..<iterator + lenght))
        iterator += lenght
        return text
    }
}