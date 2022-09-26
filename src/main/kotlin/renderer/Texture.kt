package renderer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer
import java.nio.IntBuffer

class Texture(private val filePath: String) {
    private var texId = glGenTextures()

    init {
        // make texture on gpu
        glBindTexture(GL_TEXTURE_2D, texId)

        // set texture pars
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        stbi_set_flip_vertically_on_load(true)
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
                assert(false) { "ERROR: texture unknown channels number, file path: $filePath" }
            }
        }
        else{
            assert(false) {"ERROR: texture could not be loaded, file path: $filePath"}
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