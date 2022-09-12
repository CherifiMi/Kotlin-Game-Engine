package engine

import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import renderer.Shader
import java.awt.event.KeyEvent


class LevelEditorScene : Scene() {
    private var defaultShader: Shader = Shader("vertex_shader.glsl", "fragment_shader.glsl")

    private val vertexArray = floatArrayOf(
        // position                 // rgb             //alpha
        100.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,    // Bottom right 0
        -0.5f, 100.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,    // Top left     1
        100.5f, 100.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,    // Top right    2
        -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, .74f, 1.0f,    // Bottom left  3
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
        this.camera = Camera(Vector2f())
        defaultShader.compile()

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
        glEnableVertexAttribArray(1)
    }

    override fun update(dt: Float) {
        moveCamera()

        defaultShader.use()
        defaultShader.uploadMat4f("uProj", camera!!.getProjectionMatrix())
        defaultShader.uploadMat4f("uView", camera!!.getViewMatrix())

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


        defaultShader.detach()
    }

    private fun moveCamera() {
        if (KeyListener().isKeyPressed(KeyEvent.VK_A)) {
            camera!!.position.x += 10f
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_D)) {
            camera!!.position.x -= 10f
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_S)) {
            camera!!.position.y += 10f
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_W)) {
            camera!!.position.y -= 10f
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_SPACE)) {
            camera!!.position.y -= 30f
        }
        if (camera!!.position.y < 0f && !KeyListener().isKeyPressed(KeyEvent.VK_W) && !KeyListener().isKeyPressed(KeyEvent.VK_W)) {
            camera!!.position.y += 10f
        }
    }
}