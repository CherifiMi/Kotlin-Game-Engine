package engine

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

class Window() {
    private val width = 1920/2
    private val height = 1080/2
    private val title = "KGE"
    private var glfwWindow: Long = 0L

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

            glClearColor(0.180f, 0.95f, 0.61f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)
            glfwSwapBuffers(glfwWindow)
        }
    }
}
