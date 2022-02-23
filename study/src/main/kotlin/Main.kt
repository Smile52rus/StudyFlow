import javafx.application.Application.launch
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

suspend fun main() {
    val ee: Example = Example()
    println(ee.param)
}

class Example {
    var param: String by Delegate()
}

class Delegate(): ReadWriteProperty<Any, String> {
    private var trimmedValue: String = ""
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        println(property.name + thisRef.toString())
        return property.name
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        trimmedValue = value.trim()
    }

}
