package engine

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays


class LevelEditorScene : Scene() {
    private var vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}"
    private var fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}"
    private var vertexId: Int = 0
    private var fragmentId: Int = 0
    private var shaderProgram: Int = 0

    private val vertexArray = floatArrayOf(
        // position               // color
        0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,   // Bottom right 0
        -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,   // Top left     1
        0.5f, 0.5f, 0.0f,       1.0f, 0.0f, 1.0f, 1.0f,   // Top right    2
        -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,   // Bottom left  3
    )

    // ? IMPORTANT: Must be in counter-clockwise order
    private val elementArray = intArrayOf(
        /*
                    x        x
                    x        x
             */
        2, 1, 0,  // Top right triangle
        0, 1, 3 // bottom left triangle
    )

    private var vaoId: Int = 0
    private var vboId: Int = 0
    private var eboId: Int = 0

    override fun init() {

        /* ! Compile and load shaders*/

        // * load and compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER)

        // * pass the shader to GPU
        glShaderSource(vertexId, vertexShaderSrc)
        glCompileShader(vertexId)

        // look for errors in compilation
        var success: Int = glGetShaderi(vertexId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            println("ERROR:  'default.glsl'\n Vertex shader compilation failed.")
            println(glGetShaderInfoLog(vertexId, len))
            assert(false)
        }
        //______________________


        // * load and compile vertex shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER)

        // * pass the shader to GPU
        glShaderSource(fragmentId, fragmentShaderSrc)
        glCompileShader(fragmentId)

        // look for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            println("ERROR:  'default.glsl'\n Fragment shader compilation failed.")
            println(glGetShaderInfoLog(fragmentId, len))
            assert(false)
        }

        //____________________________

        // * link shader to program
        shaderProgram = glCreateProgram()
        glAttachShader(shaderProgram, vertexId)
        glAttachShader(shaderProgram, fragmentId)
        glLinkProgram(shaderProgram)

        // * check for error
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS)
        if (success == GL_FALSE) {
            var len: Int = glGetShaderi(shaderProgram, GL_INFO_LOG_LENGTH)
            println("ERROR:  'default.glsl'\n Linking shaders failed")
            println(glGetProgramInfoLog(shaderProgram, len))
            assert(false)
        }

        /* ! generate VAO, VBO, EBO buffer objects and send them to gpu*/
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)


        // Create a float buffer of vertices
        val vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.size)
        vertexBuffer.put(vertexArray).flip()


        // Create VBO upload the vertex buffer
        vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)


        // Create the indices and upload
        val elementBuffer = BufferUtils.createIntBuffer(elementArray.size)
        elementBuffer.put(elementArray).flip()

        eboId = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW)

        // Add the vertex attribute pointers
        val positionsSize = 3
        val colorSize = 4
        val floatSizeBytes = 4
        val vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize * floatSizeBytes).toLong())
        glEnableVertexAttribArray(1);
    }

    override fun update(dt: Float) {
        // bind shader program
        glUseProgram(shaderProgram)

        // bind the vao that we are using
        glBindVertexArray(vaoId)

        // enable the vertex attrs pointer
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, elementArray.size, GL_UNSIGNED_INT, 0)

        // unbind everything
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)

        glBindVertexArray(0)

        glUseProgram(0)

    }
}