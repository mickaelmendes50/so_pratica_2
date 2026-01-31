import kotlin.concurrent.thread
import kotlin.random.Random

val ARRAY_SIZE = 50_000_000
val HIST_SIZE = 256

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Uso: kotlin programa.kt <num_threads>")
        return
    }
    val n = args[0].toInt()

    println("Gerando vetor de $ARRAY_SIZE elementos...")
    val arr = IntArray(ARRAY_SIZE) { Random.nextInt(0, 256) }

    // Histograma global
    val histGlobal = IntArray(HIST_SIZE)

    println("Iniciando $n threads...")
    val threads = mutableListOf<Thread>()
    val chunk = ARRAY_SIZE / n
    for (i in 0 until n) {
        val start = i * chunk
        val end = if (i == n-1) ARRAY_SIZE else (i+1) * chunk

        val t = thread(start = true, name = "Worker-$i") {
            // Cada thread tem seu histograma local
            val histLocal = IntArray(HIST_SIZE)

            println("Thread ${Thread.currentThread().name} processando $start..$end")
            processaHist(arr, start, end, histLocal)

            // soma local no global
            for (j in histLocal.indices) {
                histGlobal[j] += histLocal[j]
            }
        }
        threads.add(t)
    }

    threads.forEach { it.join() }

    println("Total processado: ${histGlobal.sum()} (deve ser $ARRAY_SIZE)")
}

fun processaHist(dados: IntArray, inicio: Int, fim: Int, hist: IntArray) {
    for (i in inicio until fim) {
        val v = dados[i]
        hist[v]++
    }
}
