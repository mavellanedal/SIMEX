import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.ServerSocket
import java.net.Socket

private const val PORT = 5000
private val BASE_DIR = File("arxius")

fun main() {
    if (!BASE_DIR.exists()) BASE_DIR.mkdirs()

    ServerSocket(PORT).use { server ->
        println("Server listening on port " + PORT + ", dir: " + BASE_DIR.absolutePath)

        while (true) {
            val socket = server.accept()
            println("Client connected:" + socket.inetAddress.hostAddress)
            handleClient(socket)
            println("Client done.")
        }
    }
}

private fun handleClient(socket: Socket) {
    socket.use { s ->
        val input = DataInputStream(s.getInputStream())
        val output = DataOutputStream(s.getOutputStream())

        val action = input.readUTF()

        when (action) {
            "UPLOAD" -> receiveUpload(input, output)
            "DOWNLOAD" -> sendDownload(input, output)
            else -> {
                output.writeUTF("ERROR: Unknown action!")
                output.flush()
            }
        }
    }
}

private fun receiveUpload(input: DataInputStream, output: DataOutputStream) {
    val userId = input.readInt()
    val filenameRaw = input.readUTF()
    val filename = File(filenameRaw).name
    val size = input.readLong()

    val safeFilename = "user_${userId}_$filename"
    val target = File(BASE_DIR, safeFilename)

    FileOutputStream(target).use { fos ->
        var remaining = size
        while (remaining > 0) {
            val b = input.read()
            if (b == -1) throw IllegalStateException("Stream ended early during upload")
            fos.write(b)
            remaining--
        }
        fos.flush()
    }

    output.writeUTF("OK")
    output.writeUTF(safeFilename)
    output.flush()
}




private fun sendDownload(input: DataInputStream, output: DataOutputStream) {
    val userId = input.readInt()
    val filenameRaw = input.readUTF()
    val filename = File(filenameRaw).name
    val safeFilename = "user_${userId}_$filename"
    val file = File(BASE_DIR, safeFilename)
    if (!file.exists() || !file.isFile) {
        output.writeBoolean(false)
        output.flush()
        println("Download missing: " + safeFilename)
        return
    }

    output.writeBoolean(true)
    output.writeLong(file.length())
    output.flush()

    FileInputStream(file).use { fis ->
        var b = fis.read()
        while (b != -1) {
            output.write(b)
            b = fis.read()
        }
        output.flush()
    }


    println("Downloaded: " + filename)
}
