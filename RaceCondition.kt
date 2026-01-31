import kotlin.concurrent.thread
import kotlin.random.Random

val ARRAY_SIZE = 50_000_000  // 50 milhões
val HIST_SIZE = 256

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Uso: kotlin programa.kt <num_threads>")
        return
    }
    val n = args[0].toInt()

    println("Gerando vetor de $ARRAY_SIZE elementos...")
    val arr = IntArray(ARRAY_SIZE) { Random.nextInt(0, 256) }

    println("Inicializando Histograma com tamanho $HIST_SIZE...")
    val hist = IntArray(HIST_SIZE)

    println("Iniciando $n threads...")
    val threads = mutableListOf<Thread>()
    val chunk = ARRAY_SIZE / n
    for (i in 0 until n) {
        val start = i * chunk
        val end = if (i == n-1) ARRAY_SIZE else (i+1) * chunk

        val t = thread(start = true, name = "Worker-$i") {
            println("Thread ${Thread.currentThread().name} processando $start..$end")
            processaHist(arr, start, end, hist)
        }
        threads.add(t)
    }

    threads.forEach { it.join() }

    println("Total de elementos processados: ${arr.size}")
    println("Soma histograma: ${hist.sum()} (deve ser igual a ${arr.size})")
}

fun processaHist(dados: IntArray, inicio: Int, fim: Int, hist: IntArray) {
    for (i in inicio until fim) {
        val v = dados[i]
        hist[v]++
    }
}
