import javafx.application.Application.launch
import kotlinx.coroutines.*
import java.util.*

suspend fun main() {
    coroutineScope {
        val message1 = async { sum(1,5)}
        val message2 = async { sum2(2,5) }
        println(message1.await().toString() + "  " + message2.await().toString())
        println("finish")
    }
}

suspend fun sum(a: Int, b: Int): Int {
    delay(2000)
    return a + b
}

suspend fun sum2(a: Int, b: Int): Int {
    delay(200)
    return a + b
}

