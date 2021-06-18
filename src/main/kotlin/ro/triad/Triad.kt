package ro.triad



class Triad<K, V> private constructor(val id: K, private val state: State) {

    private enum class State {
        SUCCESS,
        MISSING,
        ERROR
    }

    constructor(triad: Triad<K, V>) : this(triad.id, state = triad.state) {
        this.result = triad.result
    }

    private var result: Result<V>? = null

    fun getValueOrNull(): V?{
        return result?.getOrNull()
    }

    fun getErrorOrNull(): Throwable?{
        return result?.exceptionOrNull()
    }

    val isSuccess: Boolean
        get() = (state == State.SUCCESS)

    val isError: Boolean
        get() = (state == State.ERROR)

    val isMissing: Boolean
        get() = (state == State.MISSING)

    override fun toString(): String {
        return when(this.state){
            State.SUCCESS -> "TriadOfSuccess(key=${this.id},value=${this.getValueOrNull()})"
            State.MISSING -> "TriadOfMissing(key=${this.id})"
            State.ERROR -> "TriadOfError(key=${this.id},exception=${this.getErrorOrNull()})"
        }
    }

    override fun equals(other: Any?): Boolean {
        val otherTriad = try {
            other as Triad<K, V>
        }catch (e: Exception){
            return false
        }

        return otherTriad.id == this.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    companion object {

        inline fun <K, V> runCatching(id: K, function: () -> V?): Triad<K, V> {
            return try {
                ofSuccessOrMissing(id, function.invoke())
            }catch (e: Throwable){
                ofError(id, e)
            }
        }

        inline fun <K, V> Triad<K, V>.onSuccess(function: (value: V, id: K) -> Unit): Triad<K, V> {
            if (this.isSuccess){
                function.invoke(this.getValueOrNull()!!, id)
            }
            return this
        }

        inline fun <K, V> Triad<K, V>.onError(function: (exception: Throwable, id: K) -> Unit): Triad<K, V> {
            if (this.isError){
                function.invoke(this.getErrorOrNull()!!, id)
            }
            return this
        }

        inline fun <K, V> Triad<K, V>.onMissing(function: (id: K) -> Unit): Triad<K, V> {
            if (this.isMissing){
                function.invoke(id)
            }
            return this
        }

        fun <K, V> ofSuccess(id: K, value: V): Triad<K, V>{
            return Triad<K, V>(id, State.SUCCESS)
                    .apply { this.result = Result.success(value) }
        }

        fun <K, V> ofMissing(id: K): Triad<K, V>{
            return Triad<K, V>(id, State.MISSING)
        }

        fun <K, V> ofSuccessOrMissing(id: K, value: V?): Triad<K, V>{
            return if(value!=null)
                ofSuccess(id, value)
            else
                ofMissing(id)
        }


        fun <K, V> ofError(key: K, error: Throwable): Triad<K, V>{
            return Triad<K, V>(key, State.ERROR)
                    .apply { this.result = Result.failure(error) }
        }


    }

}

