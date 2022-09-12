package renderer

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import util.readTextFromFile
import java.nio.FloatBuffer

class Shader(v: String, f: String) {
    private var shaderProgramId: Int = 0
    var beingUsed = false
    private var vertexShaderSrc = readTextFromFile("shaders/$v")
    private var fragmentShaderSrc = readTextFromFile("shaders/$f")


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
            println("ERROR: Linking shaders failed")
            println(glGetProgramInfoLog(shaderProgramId, len))
            assert(false)
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

}
