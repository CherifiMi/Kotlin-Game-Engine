package util

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Throws(Exception::class)
fun setIcon(window: Long, path: String?) {
    val w: IntBuffer = MemoryUtil.memAllocInt(1)
    val h: IntBuffer = MemoryUtil.memAllocInt(1)
    val comp: IntBuffer = MemoryUtil.memAllocInt(1)

    // Icons
    val icon16: ByteBuffer
    val icon32: ByteBuffer
    try {
        icon16 = ioResourceToByteBuffer(path, 2048)!!
        icon32 = ioResourceToByteBuffer(path, 4096)!!
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
    GLFWImage.malloc(2).use { icons ->
        val pixels16: ByteBuffer = STBImage.stbi_load_from_memory(icon16, w, h, comp, 4)!!
        icons
            .position(0)
            .width(w.get(0))
            .height(h.get(0))
            .pixels(pixels16)
        val pixels32: ByteBuffer = STBImage.stbi_load_from_memory(icon32, w, h, comp, 4)!!
        icons
            .position(1)
            .width(w.get(0))
            .height(h.get(0))
            .pixels(pixels32)
        icons.position(0)
        GLFW.glfwSetWindowIcon(window, icons)
        STBImage.stbi_image_free(pixels32)
        STBImage.stbi_image_free(pixels16)
    }
    MemoryUtil.memFree(comp)
    MemoryUtil.memFree(h)
    MemoryUtil.memFree(w)
}


@Throws(IOException::class)
fun ioResourceToByteBuffer(resource: String?, bufferSize: Int): ByteBuffer? {
    var buffer: ByteBuffer
    val path: Path = Paths.get(resource)
    if (Files.isReadable(path)) {
        Files.newByteChannel(path).use { fc ->
            buffer = BufferUtils.createByteBuffer(fc.size().toInt() + 1)
            while (fc.read(buffer) != -1);
        }
    } else {
        Thread.currentThread().contextClassLoader.getResourceAsStream(resource).use { source ->
            Channels.newChannel(source).use { rbc ->
                buffer = BufferUtils.createByteBuffer(bufferSize)
                while (true) {
                    val bytes: Int = rbc.read(buffer)
                    if (bytes == -1) break
                    if (buffer.remaining() === 0) buffer = resizeBuffer(buffer, buffer.capacity() * 2)
                }
            }
        }
    }
    buffer.flip()
    return buffer
}

private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
    val newBuffer: ByteBuffer = BufferUtils.createByteBuffer(newCapacity)
    buffer.flip()
    newBuffer.put(buffer)
    return newBuffer
}
