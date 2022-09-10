package engine

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

class MouseListener {
    private var scrollX: Double = 0.0
    private var scrollY: Double = 0.0

    private var xPos: Double = 0.0
    private var yPos: Double = 0.0
    private var lastY: Double = 0.0
    private var lastX: Double = 0.0
    private var mouseButtonPressed = BooleanArray(3)
    private var isDragging = false

    companion object {
        private var instance: MouseListener? = null

        fun get(): MouseListener {
            if (instance == null) {
                instance = MouseListener()
            }
            return instance!!
        }
    }

    fun mousePosCallback(window: Long, xpos: Double, ypos: Double) {

        get().lastX = get().xPos
        get().lastY = get().yPos
        get().xPos = xpos
        get().yPos = ypos

        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2]
    }

    fun mouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        when (action) {
            GLFW_PRESS -> {
                if (button < get().mouseButtonPressed.size) {
                    get().mouseButtonPressed[button] = true
                }
            }
            GLFW_RELEASE -> {
                if (button < get().mouseButtonPressed.size) {
                    get().mouseButtonPressed[button] = false
                    get().isDragging = false
                }
            }
        }

    }

    fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
        get().scrollX = xOffset
        get().scrollY = yOffset
    }

    fun endFrame() {
        get().scrollX = 0.0
        get().scrollY = 0.0
        get().lastX = get().xPos
        get().lastY = get().yPos
    }

    fun getX(): Double {
        return get().xPos
    }

    fun getY(): Double {
        return get().yPos
    }

    fun getDx(): Double {
        return (get().lastX - get().xPos)
    }

    fun getDy(): Double {
        return (get().lastY - get().yPos)
    }

    fun scrollX(): Double {
        return get().scrollX
    }

    fun scrollY(): Double {
        return get().scrollY
    }

    fun isDragging(): Boolean {
        return get().isDragging
    }

    fun mouseButtonDown(button: Int): Boolean {
        return if (button < get().mouseButtonPressed.size) {
            get().mouseButtonPressed[button]
        } else {
            false
        }
    }
}