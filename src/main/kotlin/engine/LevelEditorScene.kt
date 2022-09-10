package engine

import java.awt.event.KeyEvent


class LevelEditorScene : Scene() {
    private var changingScene = false
    private var timeToChangeScene = 2.0f
    private val window = Window.get()

    init {
        println("Inside level editor scene")
    }

    override fun update(dt: Float) {
        println(1/dt)
        if (!changingScene && KeyListener().isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true
        }
        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt
            window.r -= dt * 5.0f
            window.g -= dt * 5.0f
            window.b -= dt * 5.0f
        } else if (changingScene) {
            window.changeScene(1)
        }
    }
}