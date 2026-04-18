package com.mygdx.primelogistics.android.utils

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataSecurity {
    companion object {
        fun encriptarDatos(data: ByteArray, currentUserId: Int): ByteArray {
            val password = currentUserId.toString()
            val key = SecretKeySpec(password.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.doFinal(data)
        }

        fun desencriptarDatos(encryptedData: ByteArray, currentUserId: Int): ByteArray {
            val password = currentUserId.toString()
            val key = SecretKeySpec(password.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, key)
            return cipher.doFinal(encryptedData)
        }
    }
}

