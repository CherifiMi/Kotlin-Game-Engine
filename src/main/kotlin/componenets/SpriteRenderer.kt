package componenets

import engine.Component
import org.joml.Vector2f
import org.joml.Vector4f
import renderer.Texture


class SpriteRenderer : Component {
    //__________vals
    var color: Vector4f
        private set
    var texture: Texture?
        private set
    val texCoords: Array<Vector2f> =
        arrayOf(
            Vector2f(1f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f)
        )

    constructor(color: Vector4f) {
        texture = null
        this.color = color
    }

    constructor(texture: Texture) {
        this.texture = texture
        color = Vector4f(1f, 1f, 1f, 1f)
    }

    override fun start() {}
    override fun update(dt: Float) {}
}