package engine

abstract class Component {
    var gameObject: GameObject? = null
    open fun start(){}
    abstract fun update(dt: Float)
}