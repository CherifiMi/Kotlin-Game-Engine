package renderer

import componenets.SpriteRenderer
import engine.GameObject




class Renderer {
    private val MAX_BATCH_SIZE = 1000
    private val batches: MutableList<RenderBatch>

    init {
        batches = ArrayList()
    }

    fun add(go: GameObject) {
        val spr = go.getComponent(SpriteRenderer()::class.java)
        spr?.let { add(it) }
    }

    private fun add(sprite: SpriteRenderer) {
        var added = false
        for (batch in batches) {
            if (batch.hasRoom) {
                batch.addSprite(sprite)
                added = true
                break
            }
        }
        if (!added) {
            val newBatch = RenderBatch(MAX_BATCH_SIZE)
            newBatch.start()
            batches.add(newBatch)
            newBatch.addSprite(sprite)
        }
    }

    fun render() {
        for (batch in batches) {
            batch.render()
        }
    }
}