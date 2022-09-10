package engine

abstract class Scene {
    init {
        println("Insid level scene")
    }
    abstract fun update(dt: Float)

}
