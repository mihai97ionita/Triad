package ro.triad

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class TriadOperatorTest {

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `equals operator test for reflexive`(triad: Triad<*, *>){
        assertTrue { triad == triad }
    }

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `equals operator test for symmetric`(triad: Triad<*, *>){
        val triadCopy = Triad(triad)
        assertTrue { triad == triadCopy }
        assertTrue { triadCopy == triad }
    }

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `equals operator test for transitive`(triad: Triad<*, *>){
        val triadCopy = Triad(triad)
        val triadCopy2 = Triad(triadCopy)
        assertTrue { triad == triadCopy }
        assertTrue { triadCopy == triadCopy2 }
        assertTrue { triad == triadCopy2 }
    }

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `equals operator test comparing it to String`(triad: Triad<*, *>){
        assertFalse { triad.equals("") }
    }

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `equals operator test comparing it to Exception`(triad: Triad<*, *>){
        assertFalse { triad == RuntimeException() }
    }

    @ParameterizedTest
    @MethodSource("provideTriadsWithKasKey")
    fun `hashCode is the hashCode of the id`(triad: Triad<*, *>){
        assertTrue { triad.hashCode() == triad.id.hashCode() }
    }

    @Test
    fun `hashCode equals consistency test`(){
        val triadS = Triad.ofSuccess("k", "value")
        val triadE = Triad.ofError<String, String>("k", RuntimeException())
        val triadM = Triad.ofMissing<String, String>("k")

        assertTrue { triadS.hashCode() == triadE.hashCode() }
        assertTrue { triadS == triadE }


        assertTrue { triadS.hashCode() == triadM.hashCode() }
        assertTrue { triadS == triadM }

        assertTrue { triadE.hashCode() == triadM.hashCode() }
        assertTrue { triadE == triadM }
    }

    companion object {

        @JvmStatic
        private fun provideTriadsWithKasKey(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(Triad.Companion.ofMissing<String, String>("k")),
                Arguments.of(Triad.Companion.ofError<String, String>("k", RuntimeException())),
                Arguments.of(Triad.Companion.ofSuccess<String, String>("k", "value")),

                Arguments.of(Triad.Companion.ofMissing<String, Int>("k")),
                Arguments.of(Triad.Companion.ofError<String, Int>("k", RuntimeException())),
                Arguments.of(Triad.Companion.ofSuccess<String, Int>("k", 10)),

                Arguments.of(Triad.Companion.ofMissing<String, Throwable>("k")),
                Arguments.of(Triad.Companion.ofError<String, Throwable>("k", RuntimeException())),
                Arguments.of(Triad.Companion.ofSuccess<String, Throwable>("k", RuntimeException()))
            )
        }


    }
}