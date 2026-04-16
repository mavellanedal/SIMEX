import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

private const val PORT = 5000
private val BASE_DIR = File("arxius")

fun main() {
    if (!BASE_DIR.exists()) {
        BASE_DIR.mkdirs()
    }

    ServerSocket(PORT).use { server ->
        println("Server listening on port $PORT, dir: ${BASE_DIR.absolutePath}")

        while (true) {
            val socket = server.accept()
            println("Client connected: ${socket.inetAddress.hostAddress}")

            thread {
                handleClient(socket)
                println("Client done: ${socket.inetAddress.hostAddress}")
            }
        }
    }
}

private fun handleClient(socket: Socket) {
    socket.use { client ->
        try {
            val input = DataInputStream(client.getInputStream())
            val output = DataOutputStream(client.getOutputStream())

            val action = input.readUTF()

            when (action) {
                "UPLOAD" -> receiveUpload(input, output)
                "DOWNLOAD" -> sendDownload(input, output)
                else -> {
                    output.writeUTF("ERROR")
                    output.writeUTF("Unknown action")
                    output.flush()
                }
            }
        } catch (e: Exception) {
            println("Error handling client: ${e.message}")
        }
    }
}

private fun receiveUpload(input: DataInputStream, output: DataOutputStream) {
    val userId: Int
    val rawFileName: String
    val safeFileName: String
    val targetFile: File
    val fileSize: Long
    val buffer = ByteArray(4096)

    userId = input.readInt()
    rawFileName = input.readUTF()
    fileSize = input.readLong()

    safeFileName = "user_${userId}_${File(rawFileName).name}"
    targetFile = File(BASE_DIR, safeFileName)

    FileOutputStream(targetFile).use { fileOutput ->
        var remaining = fileSize

        while (remaining > 0) {
            val bytesToRead = minOf(buffer.size.toLong(), remaining).toInt()
            val bytesRead = input.read(buffer, 0, bytesToRead)

            if (bytesRead == -1) {
                throw IllegalStateException("Stream ended early during upload")
            }

            fileOutput.write(buffer, 0, bytesRead)
            remaining -= bytesRead
        }

        fileOutput.flush()
    }

    output.writeUTF("OK")
    output.writeUTF(safeFileName)
    output.flush()

    println("Uploaded: $safeFileName")
}

private fun sendDownload(input: DataInputStream, output: DataOutputStream) {
    val rawFileName: String
    val fileName: String
    val sourceFile: File
    val buffer = ByteArray(4096)

    rawFileName = input.readUTF()
    fileName = File(rawFileName).name
    sourceFile = File(BASE_DIR, fileName)

    if (!sourceFile.exists() || !sourceFile.isFile) {
        output.writeBoolean(false)
        output.flush()
        println("Download missing: $fileName")
    } else {
        output.writeBoolean(true)
        output.writeLong(sourceFile.length())
        output.flush()

        FileInputStream(sourceFile).use { fileInput ->
            var bytesRead = fileInput.read(buffer)

            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                bytesRead = fileInput.read(buffer)
            }

            output.flush()
        }

        println("Downloaded: $fileName")
    }
}