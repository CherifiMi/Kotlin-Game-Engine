package engine

abstract class Scene {
    protected var camera: Camera? = null

    abstract fun init()
    abstract fun update(dt: Float)

}
