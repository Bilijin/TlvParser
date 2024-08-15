package com.mobolajialabi.tlvparser.utils

import java.math.BigInteger

class TlvParser(tlvData: String) {
    private val bytes: ByteArray

    init {
        bytes = hexStringToByteArray(tlvData)
    }

    fun getTag(index: Int): String {
        val tag: ByteArray
        /**
         * Checks if bits 5 - 1 of the current byte are all set (i.e equal to 11111) by performing a bitwise AND operation
         * using 31 (11111 in binary) and the current byte. The result will be equal to 31 if they are all set else it means
         * one or more of them is unset (i.e. equal to 0)
         * If they're all set, it means the tag is longer than one Byte. If not then the tag is contained in just one byte
         */
        if ((bytes[index].toInt() and 31 == 31)) {
            var curTagLength = 1
            /**
             * checks if bit 8 of the current byte (index + curTagLength) is set. If it is set, it means the tag extends to the next
             * byte and the current tag length variable is incremented by 1. Repeats this until b8 of the current byte is unset. The
             * logic behind this is that the maximum value a byte can contain is 255 (11111111). The minimum value for which b8 can
             * be set is 256 (100000000) which implies that if b8 is set, the value is bigger than the current byte can contain.
             */
            while (getBit(bytes[index + curTagLength].toInt(), 8) == 1) {
                curTagLength += 1
            }
            tag = bytes.copyOfRange(index, index + curTagLength + 1)
        } else {
            tag = bytes.copyOfRange(index, index + 1)
        }
        return tag.toHexString()
    }

    fun getLength(index: Int): ByteArray {
        val length: ByteArray
        /**
         * checks if bit 8 of the current byte (index + curLength) is set. If it is set, it means the value of length extends to the next
         * byte and the curLength variable is incremented by 1. Repeats this until b8 of the current byte is unset. The
         * logic behind this is that the maximum value a byte can contain is 255 (11111111). The minimum value for which b8 can
         * be set is 256 (100000000) which implies that if b8 is set, the value is bigger than the current byte can contain.
         */
        if (getBit(bytes[index].toInt(), 8) == 1) {
            var curLength = 1
            while (getBit(bytes[index + curLength].toInt(), 8) == 1) {
                curLength += 1
            }
            length = bytes.copyOfRange(index, index + curLength + 1)
        } else {
            length = bytes.copyOfRange(index, index + 1)
        }
        return length
    }

    /**
     * Gets the value using the length and current index.
     * If the sum of the index and length is greater than the length of the byteArray,
     * it returns null to notify the viewmodel that an error an error has occurred
     */
    fun getValue(index: Int, length: ByteArray): String? {
        val decimalLength = BigInteger(length.toHexString(), 16).toInt()
        if (index + decimalLength > bytes.size) return null
        return bytes.copyOfRange(index, index + decimalLength).toHexString()
    }

    /**
     * Uses the shr function to get the bit at the provided position of the value
     */
    private fun getBit(value: Int, position: Int): Int {
        return (value shr position) and 1
    }

    /**
     * Splits the hexadecimal string into a list of Strings with a length of 2
     * and then maps each String in the list first to an Integer of radix 16 and then to a byte
     * Finally, it converts the List<Byte> to a ByteArray
     */
    private fun hexStringToByteArray(hex: String) =
        hex.chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()

    /**
     * Converts a byte array to an hexadecimal string
     * First it transforms/maps each byte into a string by carrying out a bitwise AND operation between the byte and
     * 0xFF (255) and converting the result of that operation to a radix 16 String. Then it calls the pad start method
     * on the resulting string to append one or two zeros to the start of the string to make sure it has a length of 2.
     * Finally it joins the List of strings into a single string without a separator
     */
    private fun ByteArray.toHexString() =
        joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }
}
