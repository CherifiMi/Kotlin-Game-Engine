package componenets

import engine.Component

class SpriteRenderer: Component() {

    var firstTime = false

    override fun start() {
        println("starting componenets. ${this.gameObject?.name}")
    }

    override fun update(dt: Float) {
        if (!firstTime){
            println("update hiii componenetssss ${this.gameObject?.name}")
            firstTime = true
        }
    }
}