package ro.triad.promise

import ro.triad.realisation.RealisedMapBuilder

data class PromisedMap<K, V>(val promisedIds: Set<K>) {

    fun build() : RealisedMapBuilder<K, V> {
        return RealisedMapBuilder(promisedIds)
    }
}

