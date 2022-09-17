package engine

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera(p: Vector2f) {
    private var projectionMatrix: Matrix4f
    private var viewMatrix: Matrix4f
    var position: Vector2f

    init {
        this.position = p
        this.projectionMatrix = Matrix4f()
        this.viewMatrix = Matrix4f()
        adjustProjection()
    }


    private fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(0f, 32f * 40f, 0f, 32f * 21f, 0f, 100f)
    }

    fun getViewMatrix(): Matrix4f {
        val cameraFront = Vector3f(0f, 0f, -1f)
        val cameraUp = Vector3f(0f, 1f, 0f)
        viewMatrix.identity()
        viewMatrix =
            viewMatrix.lookAt(
                Vector3f(position.x, position.y, 20f),
                cameraFront.add(position.x, position.y, 0f),
                cameraUp
            )
        return  viewMatrix
    }

    fun getProjectionMatrix(): Matrix4f{
        return projectionMatrix
    }
}