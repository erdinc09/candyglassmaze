package com.parabolagames.glassmaze.core.persistence


import com.google.common.io.BaseEncoding
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


internal object Encryption {

    private val ALGO_LONG = String(byteArrayOf(
            'A'.toByte(),
            'E'.toByte(),
            'S'.toByte(),
            '/'.toByte(),
            'E'.toByte(),
            'C'.toByte(),
            'B'.toByte(),
            '/'.toByte(),
            'P'.toByte(),
            'K'.toByte(),
            'C'.toByte(),
            'S'.toByte(),
            '5'.toByte(),
            'P'.toByte(),
            'a'.toByte(),
            'd'.toByte(),
            'd'.toByte(),
            'i'.toByte(),
            'n'.toByte(),
            'g'.toByte(),
    ))

    private val ALGO_SHORT = String(byteArrayOf(
            'A'.toByte(),
            'E'.toByte(),
            'S'.toByte()))
    private val keyValue = byteArrayOf('a'.toByte(),
            '#'.toByte(),
            '@'.toByte(),
            'A'.toByte(),
            '1'.toByte(),
            '-'.toByte(),
            '?'.toByte(),
            'L'.toByte(),
            'Y'.toByte(),
            '='.toByte(),
            'V'.toByte(),
            'i'.toByte(),
            'T'.toByte(),
            's'.toByte(),
            'D'.toByte(),
            'r'.toByte())
    private val secretWordBytes = byteArrayOf('C'.toByte(),
            'D'.toByte(),
            'F'.toByte(),
            'X'.toByte(),
            '-'.toByte(),
            '?'.toByte(),
            '/'.toByte(),
            'O'.toByte(),
            '='.toByte())
    val SECRET_WORD = String(secretWordBytes)

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    @Throws(Exception::class)
    fun encrypt(data: String): String {
        val key: Key = generateKey()
        val c: Cipher = Cipher.getInstance(ALGO_LONG)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal: ByteArray = c.doFinal(data.toByteArray())
        return BaseEncoding.base64().encode(encVal)
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    @Throws(Exception::class)
    fun decrypt(encryptedData: String): String {
        val key: Key = generateKey()
        val c: Cipher = Cipher.getInstance(ALGO_LONG)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedValue = BaseEncoding.base64().decode(encryptedData)
        val decValue: ByteArray = c.doFinal(decodedValue)
        return String(decValue)
    }

    /**
     * Generate a new encryption key.
     */
    @Throws(Exception::class)
    private fun generateKey(): Key {
        return SecretKeySpec(keyValue, ALGO_SHORT)
    }
}