package renderer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load
import java.nio.ByteBuffer
import java.nio.IntBuffer

class Texture(s: String) {
    private var filePath: String
    private var texId: Int

    init {
        filePath = s

        // make tex on gpu
        texId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texId)

        // set tex pars
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        val width: IntBuffer = BufferUtils.createIntBuffer(1)
        val height: IntBuffer = BufferUtils.createIntBuffer(1)
        val channels: IntBuffer = BufferUtils.createIntBuffer(1)
        val image: ByteBuffer? = stbi_load(filePath, width, height, channels, 0)

        if (image != null) {
            if (channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image)
            }else if (channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image)
            }
            else{
                println("ERROR: texture unknown channels number, file path: $filePath")
                assert(false)
            }
        }
        else{
            println("ERROR: texture could not be loaded, file path: $filePath")
            assert(false)
        }

        stbi_image_free(image)
    }

    fun bind(){
        glBindTexture(GL_TEXTURE_2D, texId)
    }
    fun  unbind(){
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}