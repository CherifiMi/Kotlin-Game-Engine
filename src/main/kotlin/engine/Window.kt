package engine

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.math.max
import kotlin.random.Random

class Window() {
    private val width = 1920/2
    private val height = 1080/2
    private val title = "KGE"
    private var glfwWindow: Long = 0L
    var fadeToBlack = false
    var r = 1f
    var g = 1f
    var b = 1f
    var a = 1f

    companion object {
        private var window: Window? = null

        fun get(): Window {
            if (window == null) {
                window = Window()
            }
            return window!!
        }
    }

    fun run(){
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
        if (!glfwInit()){
            throw(java.lang.IllegalStateException("Unable to init GLFW."))
        }

        // config glfw
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE)

        //  create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL)
        if (glfwWindow == NULL){
            throw(IllegalStateException("Fail to create GLFW window."))
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

        // make window visible
        glfwShowWindow(glfwWindow)

        GL.createCapabilities()
    }
    private fun loop() {
        while (!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents()

            // color stuff :)
            glClearColor(r, g, b, a)
            glClear(GL_COLOR_BUFFER_BIT)

            //// mouse keys events
            //if (fadeToBlack){
            //    r = max(r - 0.01f, 0f)
            //    g = max(g - 0.01f, 0f)
            //    b = max(b - 0.01f, 0f)
            //}
            //when{
            //    KeyListener().isKeyPressed(GLFW_KEY_SPACE) -> {
            //        fadeToBlack = true
            //    }
            //}

            r = (0f + Math.random() * (1f-0f)).toFloat()
            g = (0f + Math.random() * (1f-0f)).toFloat()
            b = (0f + Math.random() * (1f-0f)).toFloat()

            glfwSwapBuffers(glfwWindow)
        }
    }
}
