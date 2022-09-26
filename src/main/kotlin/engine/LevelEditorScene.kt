
package engine

import componenets.SpriteRenderer
import org.joml.Vector2f
import renderer.Texture
import util.AssetPool
import java.awt.event.KeyEvent


class LevelEditorScene : Scene() {

    override fun setup() {
        camera = Camera(Vector2f(-250f, 0f))


        //region draw textures

        val b = GameObject("dico", Transform(Vector2f(400f, 100f), Vector2f(256f, 256f)))
        b.addComponents(SpriteRenderer(AssetPool().getTexture("src/main/resources/images/dico_icon.jpg")))
        addGameObjectToScene(b)
        addGameObjectToScene(b)

        val a = GameObject("mario", Transform(Vector2f(100f, 100f), Vector2f(256f, 256f)))
        a.addComponents(SpriteRenderer(Texture("src/main/resources/images/mario.png")))
        addGameObjectToScene(a)
        addGameObjectToScene(a)

        //endregion

        // region draw shader
        /*val xOffset = 10
        val yOffset = 10

        val totalWidth = (900 - xOffset * 2).toFloat()
        val totalHeight = (600 - yOffset * 2).toFloat()
        val sizeX = totalWidth / 100.0f
        val sizeY = totalHeight / 100.0f
        val padding = 0f

        for (x in 0..99) {
            for (y in 0..99) {
                val xPos = xOffset + x * sizeX + padding * x
                val yPos = yOffset + y * sizeY + padding * y
                val go = GameObject("Obj$x$y", Transform(Vector2f(xPos, yPos), Vector2f(sizeX, sizeY)))
                go.addComponents(SpriteRenderer(Vector4f(xPos / totalWidth, yPos / totalHeight, 1f, 1f)))
                addGameObjectToScene(go)
            }
        }*/


        loadResources()
        // endregion


    }

    private fun loadResources() {
        AssetPool().getShaders("default_shader.glsl")
    }

    override fun update(dt: Float) {
        moveCamera()

        for (go in gameObject) {
            go.update(dt)
        }

        renderer.render()
    }

    //region camera control
    var s = 10f
    private fun moveCamera() {
        if (KeyListener().isKeyPressed(KeyEvent.VK_A)) {
            camera!!.position.x += s
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_D)) {
            camera!!.position.x -= s
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_S)) {
            camera!!.position.y += s
        }
        if (KeyListener().isKeyPressed(KeyEvent.VK_W)) {
            camera!!.position.y -= s
        }
        zoom += MouseListener().scrollY().toFloat()/10f
        MouseListener().endFrame()
    }

    //endregion

}

