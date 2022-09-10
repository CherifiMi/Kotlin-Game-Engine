package engine

class LevelScene : Scene() {
    private val window = Window.get()

    init {
        println("Inside level scene")
        window.r = 1f
        window.g = 1f
        window.b = 1f
    }

    override fun update(dt: Float) {}
}