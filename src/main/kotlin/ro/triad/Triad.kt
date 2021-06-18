package ro.triad



class Triad<K, V> private constructor(val key: K, private val state: State) {

    private enum class State {
        SUCCESS,
        MISSING,
        ERROR
    }

    private var result: Result<V>? = null

    val isSuccess: Boolean
        get() = (state == State.SUCCESS)

    val isError: Boolean
        get() =(state == State.ERROR)

    val isMissing: Boolean
        get() = (state == State.MISSING)

    override fun toString(): String {
        return when(this.state){
            State.SUCCESS -> "TriadOfSuccess(key=${this.key},value=${this.result!!.getOrNull()!!})"
            State.MISSING -> "TriadOfMissing(key=${this.key})"
            State.ERROR -> "TriadOfError(key=${this.key},exception=${this.result!!.exceptionOrNull()!!})"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Triad<*, *>
                && other.key == this.key
                && other.state == this.state
                ){
                    return when(this.state){
                        State.SUCCESS -> other.result!!.getOrNull()!! == this.result!!.getOrNull()!!
                        State.MISSING -> true
                        State.ERROR -> other.result!!.exceptionOrNull()!! == this.result!!.exceptionOrNull()!!
                    }
        }
        return false
    }

    override fun hashCode(): Int {
        return this.key.hashCode()
    }


    companion object {

        fun <K, V> Triad<K, V>.onSuccess(function: (value: V) -> Unit): Triad<K, V> {
            if (this.isSuccess){
                function.invoke(result!!.getOrNull()!!)
            }
            return this
        }

        fun <K, V> Triad<K, V>.onError(function: (exception: Throwable) -> Unit): Triad<K, V> {
            if (this.isError){
                function.invoke(result!!.exceptionOrNull()!!)
            }
            return this
        }

        fun <K, V> Triad<K, V>.onMissing(function: () -> Unit): Triad<K, V> {
            if (this.isMissing){
                function.invoke()
            }
            return this
        }

        fun <K, V> ofSuccess(key: K, value: V): Triad<K, V>{
            return Triad<K, V>(key, State.SUCCESS)
                    .apply { this.result = Result.success(value) }
        }

        fun <K, V> ofError(key: K, error: Throwable): Triad<K, V>{
            return Triad<K, V>(key, State.ERROR)
                    .apply { this.result = Result.failure(error) }
        }

        fun <K, V> ofMissing(key: K): Triad<K, V>{
            return Triad<K, V>(key, State.MISSING)
        }
    }

}

