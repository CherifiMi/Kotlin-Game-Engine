package util

import renderer.Shader
import java.io.File

class AssetPool {
    private lateinit var shaders: MutableMap<String, Shader>

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
}