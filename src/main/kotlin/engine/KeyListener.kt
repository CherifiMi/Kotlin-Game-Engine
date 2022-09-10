package engine

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import java.awt.SystemColor.window

class KeyListener {
    private var keyPressed = BooleanArray(350)

    companion object {
        private var instance: KeyListener? = null

        fun get(): KeyListener {
            if (instance == null) {
                instance = KeyListener()
            }
            return instance!!
        }
    }

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true
        }
        if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false
        }

        println(key)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return if (keyCode < get().keyPressed.size) {
            get().keyPressed[keyCode]
        } else {
            false
        }

    }
}