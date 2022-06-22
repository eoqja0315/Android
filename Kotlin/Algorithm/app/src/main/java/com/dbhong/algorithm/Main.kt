package com.dbhong.algorithm

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args : Array<String>) {
    val bw = BufferedReader(InputStreamReader(System.`in`))
    val br = BufferedWriter(OutputStreamWriter(System.out))

    val (n , m) = bw.readLine().split(" ").map { it.toInt() }
    br.write("${n - m}")
    br.flush()
    br.close()
    bw.close()

}