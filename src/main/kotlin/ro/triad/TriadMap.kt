package ro.triad

class TriadMap<K, V> {

    val size: Int
        get() = this.internalMap.size

    //TODO should this be immutable? return a copy?
    val keys: Set<K>
        get() = this.internalMap.keys

    //TODO should this be immutable? return a copy?
    val entries: Set<Map.Entry<K, Triad<K, V>>>
        get() = this.internalMap.entries

    private var internalMap: MutableMap<K, Triad<K, V>>

    constructor(){
        internalMap = HashMap()
    }

    constructor(triad: TriadMap<K, V>) : this() {
        this.internalMap = HashMap(triad.internalMap)
    }

    private constructor(map: MutableMap<K, Triad<K, V>>) : this() {
        this.internalMap = map
    }

    fun copy(): TriadMap<K, V>{
        return TriadMap(HashMap(this.internalMap))
    }

    operator fun get(key: K): Triad<K, V> {
        return try {
            internalMap.getValue(key)
        }catch (e : NoSuchElementException){
            throw NoSuchElementException("Element with key $key was removed or never inserted")
        }
    }

    fun setTriad(triad: Triad<K, V>){
        internalMap[triad.id] = triad
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

    fun removeByKey(key: K){
        this.internalMap.remove(key)
    }

    fun remove(triad: Triad<K, V>){
        removeByKey(triad.id)
    }

    operator fun plus(otherTriadMap: TriadMap<K, V>): TriadMap<K, V>{
        val copy = this.copy()

        val keys = otherTriadMap.keys
        keys.forEach { key ->
            val newTriad: Triad<K, V> = otherTriadMap[key]
            copy.setTriad(newTriad)
        }

        return copy
    }

    operator fun plus(triad: Triad<K, V>): TriadMap<K, V> {
        val copy = this.copy()
        copy.setTriad(triad)
        return copy
    }

    operator fun minus(otherTriadMap: TriadMap<K, V>): TriadMap<K, V> {
        val copy = this.copy()

        val keys = otherTriadMap.keys
        keys.forEach { key ->
            copy.removeByKey(key)
        }

        return copy
    }

    operator fun minus(triad: Triad<K, V>): TriadMap<K, V> {
        val copy = this.copy()
        copy.removeByKey(triad.id)
        return copy
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
        val otherTriadMap = try {
            other as TriadMap<K, V>
        }catch (e: Exception){
            return false
        }

        if (otherTriadMap.size != this.size)
            return false

        if (this.internalMap.isEmpty())
            return true

        val allKeys = this.keys + otherTriadMap.keys

        return allKeys.map { key ->
            try {
                this[key] == otherTriadMap[key]
            }catch (e: Exception){
                false
            }
        }.reduce { acc, b -> acc && b }

    }

    override fun hashCode(): Int {
        return this.keys.hashCode()
    }
}