package renderer

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import util.readTextFromFile

class Shader {
    var shaderProgramId: Int = 0
    private var vertexShaderSrc: String
    private var fragmentShaderSrc: String

    constructor(v: String, f: String){
        vertexShaderSrc = readTextFromFile("shaders/$v")
        fragmentShaderSrc = readTextFromFile("shaders/$f")
    }

    fun compile(){
        var vertexId = 0
        var fragmentId = 0

        // load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER)

        // pass the shader to GPU
        glShaderSource(vertexId, vertexShaderSrc)
        glCompileShader(vertexId)

        // look for errors in compilation
        var success: Int = glGetShaderi(vertexId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            println("ERROR:  'vertex_shader.glsl'\n Vertex shader compilation failed.")
            println(glGetShaderInfoLog(vertexId, len))
            assert(false)
        }

        // load and compile vertex shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER)

        // pass the shader to GPU
        glShaderSource(fragmentId, fragmentShaderSrc)
        glCompileShader(fragmentId)

        // look for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            println("ERROR:  'fragment_shader.glsl'\n Fragment shader compilation failed.")
            println(glGetShaderInfoLog(fragmentId, len))
            assert(false)
        }


        //____________________________

        // link shader to program
        shaderProgramId = glCreateProgram()
        glAttachShader(shaderProgramId, vertexId)
        glAttachShader(shaderProgramId, fragmentId)
        glLinkProgram(shaderProgramId)

        // check for error
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(shaderProgramId, GL_INFO_LOG_LENGTH)
            println("ERROR:  'default.glsl'\n Linking shaders failed")
            println(glGetProgramInfoLog(shaderProgramId, len))
            assert(false)
        }
    }

    fun use(){
        // bind shader program
        glUseProgram(shaderProgramId)
    }

    fun detach(){
        glUseProgram(0)
    }
}
