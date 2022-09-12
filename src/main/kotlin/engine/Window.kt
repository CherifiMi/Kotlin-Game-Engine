package engine

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import util.setIcon


class Window() {
    private val width = 1920
    private val height = 1080
    private val title = "KGE"
    private var glfwWindow: Long = 0L

    var r = 1f
    var g = 1f
    var b = 1f
    var a = 1f

    private var currentScene: Scene? = null

    fun changeScene(newScene: Int) {
        when (newScene) {
            0 -> {
                currentScene = LevelEditorScene()
                currentScene?.init()
            }
            1 -> {
                currentScene = LevelScene()
                currentScene?.init()
            }
        }
    }

    companion object {
        private var window: Window? = null

        fun get(): Window {
            if (window == null) {
                window = Window()
            }
            return window!!
        }
    }

    fun run() {
        println("hillo LWJGL " + Version.getVersion())
        init()
        loop()

        // free memory
        glfwFreeCallbacks(glfwWindow)
        glfwDestroyWindow(glfwWindow)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()

    }

    private fun init() {
        // setup error call back
        GLFWErrorCallback.createPrint(System.err).set()

        // init glfw
        if (!glfwInit()) {
            throw (java.lang.IllegalStateException("Unable to init GLFW."))
        }

        // config glfw
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE)

        //  create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL)
        if (glfwWindow == NULL) {
            throw (IllegalStateException("Fail to create GLFW window."))
        }

        // mouse callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener()::mousePosCallback)
        glfwSetMouseButtonCallback(glfwWindow, MouseListener()::mouseButtonCallback)
        glfwSetScrollCallback(glfwWindow, MouseListener()::mouseScrollCallback)

        // keys callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener()::keyCallback)

        // make OpenGL context current
        glfwMakeContextCurrent(glfwWindow)
        //Enable v-sync
        glfwSwapInterval(1)

        //change app icon
        setIcon(glfwWindow, "images/dico_icon.jpg")

        // make window visible
        glfwShowWindow(glfwWindow)

        GL.createCapabilities()

        // change scene
        changeScene(0)
    }

    private fun loop() {
        var beginTime: Float = glfwGetTime().toFloat()
        var endTime: Float
        var dt = -1.0f

        while (!glfwWindowShouldClose(glfwWindow)) {
            // poll events
            glfwPollEvents()

            // color stuff :)
            glClearColor(r, g, b, a)
            glClear(GL_COLOR_BUFFER_BIT)

            // mouse keys events
            if (dt >= 0) {
                currentScene?.update(dt)
            }


            glfwSwapBuffers(glfwWindow)

            endTime = glfwGetTime().toFloat()
            dt = endTime - beginTime
            beginTime = endTime
        }
    }
}
