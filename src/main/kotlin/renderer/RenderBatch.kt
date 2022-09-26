package renderer

import componenets.SpriteRenderer
import engine.Window
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20C.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import util.AssetPool


class RenderBatch(maxBatchSize: Int) {
    private val POS_SIZE = 2
    private val COLOR_SIZE = 4
    private val TEX_COORDS_SIZE = 2
    private val TEX_ID_SIZE = 1

    private val POS_OFFSET = 0
    private val COLOR_OFFSET = POS_OFFSET + POS_SIZE * java.lang.Float.BYTES
    private val TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * java.lang.Float.BYTES
    private val TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * java.lang.Float.BYTES

    private val VERTEX_SIZE = 9
    private val VERTEX_SIZE_BYTES = VERTEX_SIZE * java.lang.Float.BYTES

    private val sprites: Array<SpriteRenderer?>
    private val textures: MutableList<Texture> = mutableListOf()
    private var numSprites: Int
    var hasRoom: Boolean
    private val vertices: FloatArray
    private val texSlots: IntArray = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
    private var vaoId = 0
    private var vboId = 0
    private val maxBatchSize: Int
    private val shader: Shader = AssetPool().getShaders("default_shader.glsl")

    init {
        shader.compile()
        sprites = arrayOfNulls<SpriteRenderer>(maxBatchSize)
        this.maxBatchSize = maxBatchSize
        vertices = FloatArray(maxBatchSize * 4 * VERTEX_SIZE)
        numSprites = 0
        hasRoom = true
    }


    fun start() {
        // Generate and bind a Vertex Array Object
        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

        // Allocate space for vertices
        vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, (vertices.size * Float.SIZE_BYTES).toLong(), GL_DYNAMIC_DRAW)

        // Create and upload indices buffer
        val eboID: Int = glGenBuffers()
        val indices: IntArray = generateIndices()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET.toLong())
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET.toLong())
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET.toLong())
        glEnableVertexAttribArray(2)

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET.toLong())
        glEnableVertexAttribArray(3)
    }

    fun addSprite(spr: SpriteRenderer) {
        val index = numSprites
        sprites[index] = spr
        numSprites++
        loadVertexProperties(index)
        if (numSprites >= maxBatchSize) {
            hasRoom = false
        }
        if (spr.texture != null){
            if (!textures.contains(spr.texture)){
                textures.add(spr.texture!!)
            }
        }
    }

    fun render() {
        // for now, will rebuffer all data
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices)

        // use shader
        val camera = Window().getScene().camera!!
        val zoom = Window().getScene().zoom
        shader.use()
        shader.uploadMat4f("uProj", camera.getProjectionMatrix())
        shader.uploadMat4f("uView", camera.getViewMatrix())
        shader.uploadFloat("uZoom", zoom)
        shader.uploadIntArray("c", texSlots)

        textures.forEachIndexed { i, tex ->
            glActiveTexture(GL_TEXTURE0 + i + 1)
            tex.bind()
        }


        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        textures.forEachIndexed { _, tex ->
            tex.unbind()
        }

        shader.detach()

    }

    private fun generateIndices(): IntArray {
        val elements = IntArray(6 * maxBatchSize)

        for (index in 0 until maxBatchSize) {
            loadElementIndices(elements, index)
        }

        return elements
    }

    private fun loadElementIndices(elements: IntArray, index: Int) {

        val offsetArrayIndex = 6 * index
        val offset = 4 * index

        elements[offsetArrayIndex] = offset + 3
        elements[offsetArrayIndex + 1] = offset + 2
        elements[offsetArrayIndex + 2] = offset + 0


        elements[offsetArrayIndex + 3] = offset + 0
        elements[offsetArrayIndex + 4] = offset + 2
        elements[offsetArrayIndex + 5] = offset + 1

    }

    private fun loadVertexProperties(index: Int) {
        val sprite = sprites[index]
        var offset = index * 4 * VERTEX_SIZE
        val color = sprite!!.color
        val texture = sprite!!.texture
        val texCoords = sprite.texCoords
        var texId = 0
        if (sprite.texture!=null){
            textures.forEachIndexed{i, tex ->
                if (tex ==sprite.texture){
                    texId = i + 1
                }
            }
        }

        var xAdd = 1.0f
        var yAdd = 1.0f

        for (i in 0..3) {
            when (i) {
                1 -> yAdd = 0.0f
                2 -> xAdd = 0.0f
                3 -> yAdd = 1.0f
            }

            // load position
            vertices[offset] = sprite.gameObject!!.transform.position.x + (xAdd * sprite.gameObject!!.transform.scale.x)
            vertices[offset + 1] =
                sprite.gameObject!!.transform.position.y + (yAdd * sprite.gameObject!!.transform.scale.y)

            // load color
            vertices[offset + 2] = color.x
            vertices[offset + 3] = color.y
            vertices[offset + 4] = color.z
            vertices[offset + 5] = color.w

            //load texture coords
            vertices[offset + 6] = texCoords[i].x
            vertices[offset + 7] = texCoords[i].y

            //load texture id
            vertices[offset + 8] = texId.toFloat()

            offset += VERTEX_SIZE
        }

    }
}
