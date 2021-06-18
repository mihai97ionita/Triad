package ro.triad

import ro.triad.Triad.Companion.onSuccess
import java.lang.RuntimeException

fun main(args: Array<String>) {
	val ceva = Triad.ofSuccess("123", 200.0)
	val none = Triad.ofMissing<String, Double>("222")
	val exception = Triad.ofError<String, Double>("333", RuntimeException())

	var map = TriadMap<String, Double>()
	map += none
	map += exception
	map += ceva

	println(map["123"])
	val cpy = TriadMap(map)
	println(cpy)
	val cacat = ( map - ceva + Triad.ofMissing<String, Double>("1292092092"))
	println(cacat)
	println(cpy == cacat)
}

