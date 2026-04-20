package com.mygdx.primelogistics.android.utils

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

class DataSecurity {
    companion object {
        private const val AES_TRANSFORMATION = "AES/ECB/PKCS5Padding"
        private const val CLAVE_BASE = "simex-dni-seguro"

        fun encriptarDatos(data: ByteArray, currentUserId: Int): ByteArray {
            val key = SecretKeySpec(generarClaveAes(currentUserId), "AES")
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.doFinal(data)
        }

        fun desencriptarDatos(encryptedData: ByteArray, currentUserId: Int): ByteArray {
            val key = SecretKeySpec(generarClaveAes(currentUserId), "AES")
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return cipher.doFinal(encryptedData)
        }

        private fun generarClaveAes(currentUserId: Int): ByteArray {
            val semilla = "$CLAVE_BASE-$currentUserId".toByteArray(Charsets.UTF_8)
            return MessageDigest.getInstance("SHA-256").digest(semilla).copyOf(16)
        }
    }
}
