package ro.triad

class TriadMap<K, V> () {

    private constructor(map: MutableMap<K, Triad<K, V>>) : this() {
        this.internalMap = map
    }

    constructor(triad: TriadMap<K, V>) : this() {
        this.internalMap = HashMap(triad.internalMap)
    }

    private var internalMap: MutableMap<K, Triad<K, V>> = HashMap()

    operator fun get(key: K): Triad<K, V> {
        return try {
            internalMap.getValue(key)
        }catch (e : NoSuchElementException){
            throw NoSuchElementException("Element with key $key was removed or never inserted")
        }
    }

    operator fun set(key: K, triad: Triad<K, V>){
        internalMap[key] = triad
    }

    operator fun set(key: K, value: V?){
        if(value == null){
            internalMap[key] = Triad.ofMissing<K,V>(key)
        }else{
            internalMap[key] = Triad.ofSuccess<K,V>(key, value)
        }
    }

    operator fun set(key: K, exception: Throwable){
        internalMap[key] = Triad.ofError<K,V>(key, exception)
    }

    operator fun plus(anotherMap: TriadMap<K, V>): TriadMap<K, V>{
        val copy = HashMap(this.internalMap)
        val map = anotherMap.internalMap
        val keys = anotherMap.internalMap.keys
        keys.forEach { key ->
            val triad: Triad<K, V> = map[key]!!
            copy[key] = triad
        }
        return TriadMap(copy)
    }

    operator fun plus(triad: Triad<K, V>): TriadMap<K, V> {
        val copy = HashMap(this.internalMap)
        copy[triad.key] = triad
        return TriadMap(copy)
    }

    operator fun minus(triad: Triad<K, V>): TriadMap<K, V> {
        val copy = HashMap(this.internalMap)
        copy.remove(triad.key)
        return TriadMap(copy)
    }

    override fun toString(): String {
        if (this.internalMap.isEmpty())
            return "[]"
        val map = this.internalMap
        val keys = this.internalMap.keys
        val list = keys.map { key ->
             map[key]!!.toString()
        }.reduce { acc, s -> "$acc,$s" }
        return "[$list]"
    }

    override fun equals(other: Any?): Boolean {
        if (other is TriadMap<*, *>){
            if (other.internalMap.size != this.internalMap.size)
                return false
            if (this.internalMap.isEmpty())
                return true
            val map = other.internalMap
            val keys = other.internalMap.keys
            val otherInThis = keys.map { key ->
                val triad = map[key]!!
                try {
                    return this[key as K] == triad
                }catch (e: Exception){
                    false
                }
                //TODO check this reduce
            }.reduce { acc, b -> acc && b }

            val thisInOther = this.internalMap.keys.map { key ->
                val triad = this.internalMap[key]!!
                try {
                    return triad == map[key]
                }catch (e: Exception){
                    false
                }
                //TODO check this reduce
            }.reduce { acc, b -> acc && b }

            return otherInThis && thisInOther
        }
        return false
    }

    override fun hashCode(): Int {
        return this.internalMap.hashCode()
    }
}