package util

import renderer.Shader
import renderer.Texture
import java.io.File

class AssetPool {
    private var shaders: MutableMap<String, Shader> = mutableMapOf()
    private var textures: MutableMap<String, Texture> = mutableMapOf()

    fun getShaders(resName: String): Shader{
        val file = File(resName)
        return if (shaders.containsKey(file.name)){
            shaders[file.name]!!
        }
        else{
            val shader = Shader(resName)
            shader.compile()
            shaders[file.name] = shader
            return shader
        }
    }

    fun getTexture(resName: String): Texture{
        val file = File(resName)
        return if (textures.containsKey(file.name)){
            textures[file.name]!!
        }
        else{
            val texture = Texture(resName)
            textures[file.name] = texture
            return texture
        }
    }
}