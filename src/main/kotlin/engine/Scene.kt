package engine

abstract class Scene {
    protected var camera: Camera? = null
    var isRunning = false
    var gameObject: MutableList<GameObject> = mutableListOf()

    abstract fun init()

    fun start(){
        for (go in gameObject){
            go.start()
        }
        isRunning = true
    }

    fun addGameObjectToScene(go: GameObject){
        if (!isRunning){
            gameObject.add(go)
        }else{
            gameObject.add(go)
            go.start()
        }
    }

    abstract fun update(dt: Float)

    fun camera(): Camera{
        return camera!!
    }
}
