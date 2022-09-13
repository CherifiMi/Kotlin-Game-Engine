package componenets

import engine.Component

class FontRenderer: Component() {

    override fun start() {
        if (gameObject?.getComponent(SpriteRenderer::class.java) != null){
            println("Found font renderer!")
        }
    }

    override fun update(dt: Float) {
    }
}