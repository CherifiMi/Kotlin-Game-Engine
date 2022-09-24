
package engine

import componenets.SpriteRenderer
import org.joml.Vector2f
import org.joml.Vector4f
import java.awt.event.KeyEvent


class LevelEditorScene : Scene() {

    override fun init() {
        camera = Camera(Vector2f(-250f, 0f))

        // region draw
        val xOffset = 10
        val yOffset = 10

        val totalWidth = (900 - xOffset * 2).toFloat()
        val totalHeight = (600 - yOffset * 2).toFloat()
        val sizeX = totalWidth / 100.0f
        val sizeY = totalHeight / 100.0f
        val padding = 5f

        for (x in 0..99) {
            for (y in 0..99) {
                val xPos = xOffset + x * sizeX + padding * x
                val yPos = yOffset + y * sizeY + padding * y
                val go = GameObject("Obj$x$y", Transform(Vector2f(xPos, yPos), Vector2f(sizeX, sizeY)))
                go.addComponents(SpriteRenderer(Vector4f(xPos / totalWidth, yPos / totalHeight, 1f, 1f)))
                addGameObjectToScene(go)
            }
        }
        // endregion

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

