package renderer

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import util.readTextFromFile
import java.nio.FloatBuffer

class Shader(shaderFilePath: String) {
    private var shaderProgramId: Int = 0
    var beingUsed = false
    private val shaders = readTextFromFile("shaders/$shaderFilePath").split("/**/")
    private var vertexShaderSrc = shaders[0]
    private var fragmentShaderSrc = shaders[1]


    fun compile(){

        // load and compile vertex shader
        var vertexId: Int = glCreateShader(GL_VERTEX_SHADER)

        // pass the shader to GPU
        glShaderSource(vertexId, vertexShaderSrc)
        glCompileShader(vertexId)

        // look for errors in compilation
        var success: Int = glGetShaderi(vertexId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len: Int = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            println(glGetShaderInfoLog(vertexId, len))
            assert(false) {"ERROR:  'vertex_shader.glsl'\n Vertex shader compilation failed."}
        }

        // load and compile vertex shader
        var fragmentId: Int = glCreateShader(GL_FRAGMENT_SHADER)

        // pass the shader to GPU
        glShaderSource(fragmentId, fragmentShaderSrc)
        glCompileShader(fragmentId)

        // look for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            val len: Int = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            println(glGetShaderInfoLog(fragmentId, len))
            assert(false) {"ERROR:  'fragment_shader.glsl'\n Fragment shader compilation failed."}
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
            val len: Int = glGetShaderi(shaderProgramId, GL_INFO_LOG_LENGTH)
            println(glGetProgramInfoLog(shaderProgramId, len))
            assert(false){"ERROR: Linking shaders failed"}
        }
    }

    fun use(){
        if (!beingUsed){
            glUseProgram(shaderProgramId)
            beingUsed = true
        }
    }

    fun detach(){
        glUseProgram(0)
        beingUsed = false
    }

    fun uploadMat4f(varName: String, mat4: Matrix4f){
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        val matBuffer = BufferUtils.createFloatBuffer(16)
        mat4.get(matBuffer)
        glUniformMatrix4fv(varLocation, false, matBuffer)
    }

    fun uploadVec4f(varName: String, vec: Vector4f){
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w)
    }

    fun uploadMat3f(varName: String?, mat3: Matrix3f) {
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        val matBuffer: FloatBuffer = BufferUtils.createFloatBuffer(9)
        mat3.get(matBuffer)
        glUniformMatrix3fv(varLocation, false, matBuffer)
    }

    fun uploadVec3f(varName: String?, vec: Vector3f) {
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform3f(varLocation, vec.x, vec.y, vec.z)
    }

    fun uploadVec2f(varName: String?, vec: Vector2f) {
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform2f(varLocation, vec.x, vec.y)
    }

    fun uploadFloat(varName: String, v: Float){
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform1f(varLocation, v)
    }

    fun uploadInt(varName: String, v: Int){
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform1i(varLocation, v)
    }

    fun uploadTex(varName: String, v: Int){
        val varLocation = glGetUniformLocation(shaderProgramId, varName)
        use()
        glUniform1i(varLocation, v)
    }

}
