
package engine

class GameObject(n: String, t: Transform = Transform()) {

    private val name = n
    private var components: MutableList<Component> = mutableListOf()
    var transform = t


    fun <T : Component?> getComponent(componentClass: Class<T>): T? {
        for (c in components) {
            if (componentClass.isAssignableFrom(c.javaClass)) {
                try {
                    return componentClass.cast(c)
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                    assert(false) { "Error: Casting component $name." }
                }
            }
        }
        return null
    }

    fun <T : Component?> removeComponent(componentClass: Class<T>) {
        for (c in components) {
            if (componentClass.isAssignableFrom(c.javaClass)) {
                components.remove(c)
                return
            }
        }
    }

    fun addComponents(c: Component){
        components.add(c)
        c.gameObject = this
    }

    fun update(dt: Float){
        for(c in components){
            c.update(dt)
        }
    }

    fun start(){
        for(c in components){
            c.start()
        }
    }
}


