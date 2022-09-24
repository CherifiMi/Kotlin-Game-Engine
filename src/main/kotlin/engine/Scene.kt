package engine

import renderer.Renderer

abstract class Scene {
    val renderer = Renderer()
    var camera: Camera? = null
    var zoom: Float = 1f
    var isRunning = false
    var gameObject: MutableList<GameObject> = mutableListOf()

    abstract fun setup()

    fun start(){
        for (go in gameObject){
            go.start()
            this.renderer.add(go)
        }
        isRunning = true
    }

    fun addGameObjectToScene(go: GameObject){
        if (!isRunning){
            gameObject.add(go)
        }else{
            gameObject.add(go)
            go.start()
            this.renderer.add(go)
        }
    }

    abstract fun update(dt: Float)

}
