import kotlin.concurrent.thread

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Uso: kotlin programa.kt <num_threads>")
        return
    }
    val n = args[0].toInt()

    println("Iniciando $n threads...")

    val threads = mutableListOf<Thread>()
    for (i in 0 until n) {
        val t = thread(start = true, name = "Worker-$i") {
            println("Thread ${Thread.currentThread().name}")
            var counter = 0L
            while (true) {
                counter += 1
                Thread.sleep(10)
            }
        }
        threads.add(t)
    }

    threads.forEach { it.join() }
}
