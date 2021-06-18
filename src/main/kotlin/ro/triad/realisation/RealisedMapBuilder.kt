package ro.triad.realisation

import ro.triad.Triad
import ro.triad.TriadMap

class RealisedMapBuilder<K, V> internal constructor(val promisedIds: Set<K>) {

    private val map: TriadMap<K, V> = TriadMap()

//    companion object{
//
//        fun <K, V> realise(action: (Id: K) -> Triad<K, V>): Triad<K, V>{
//
//        }
//    }



}