package ro.triad.realisation

import ro.triad.Triad
import ro.triad.TriadMap

data class RealisedMap<K, V>(val promisedIds: Set<K>, private val map: TriadMap<K, V>) {

    operator fun get(key: K): Triad<K, V> {
        TODO()
    }
}