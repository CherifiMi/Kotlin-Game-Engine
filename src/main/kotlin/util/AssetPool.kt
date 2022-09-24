package util

import renderer.Shader
import java.io.File

class AssetPool {
    private lateinit var shaders: MutableMap<String, Shader>

    fun getShaders(resName: String): Shader{
        val file = File(resName)
        return if (shaders.containsKey(file.absolutePath)){
            shaders.get(file.absolutePath)!!
        }
        else{
            val shader = Shader(resName)
            shader.compile()
            AssetPool().shaders[file.absolutePath] = shader
            return shader
        }
    }
}