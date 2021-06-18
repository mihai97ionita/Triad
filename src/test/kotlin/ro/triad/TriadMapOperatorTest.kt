package ro.triad

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TriadMapOperatorTest {

    @Test
    fun `TriadMaps with differing size return equals false`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        val map2 = TriadMap<String, String>()

        assertTrue { map1.size == 1 }
        assertTrue { map2.size == 0 }
        assertFalse { map1 == map2 }
    }

    @Test
    fun `TriadMaps with same values return equals true`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        val map2 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }

        assertTrue { map1 == map2 }
    }

    @Test
    fun `TriadMaps with same keys return equals true`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        val map2 = TriadMap<String, String>().also { it.setTriad(Triad.ofMissing("123")) }

        assertTrue { map1 == map2 }
    }

    @Test
    fun `equals operator test for reflexive`(){
        val map = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        assertTrue { map == map }
    }

    @Test
    fun `equals operator test for symmetric`(){
        val map = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        val mapCopy = TriadMap<String, String>().also { it.setTriad(Triad.ofMissing("123")) }
        assertTrue { map == mapCopy }
        assertTrue { mapCopy == map }
    }

    @Test
    fun `equals operator test for transitive`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        val map2 = TriadMap<String, String>().also { it.setTriad(Triad.ofMissing("123")) }
        val map3 = TriadMap<String, String>().also { it.setTriad(Triad.ofError("123", RuntimeException())) }

        assertTrue { map1 == map2 }
        assertTrue { map2 == map3 }
        assertTrue { map3 == map1 }
    }

    @Test
    fun `equals operator test for consistent`(){
        val map1 = TriadMap<String, String>()
        val map2 = TriadMap<String, String>()

        assertTrue { map1 == map2 }

        map1.setTriad(Triad.ofSuccess("123", ""))
        assertFalse { map1 == map2 }
    }

    @Test
    fun `equals operator test comparing it to String`(){
        val map = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        assertFalse { map.equals("") }
    }

    @Test
    fun `equals operator test comparing it to Exception`(){
        val map = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "")) }
        assertFalse { map == RuntimeException() }
    }

    @Test
    fun `hashCode internal consistency test`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "123")) }
        val initialHash = map1.hashCode()

        map1.setTriad(Triad.ofSuccess("x123", ""))

        assertFalse { initialHash == map1.hashCode() }
    }

    @Test
    fun `hashCode should differ for diff maps test `(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("xsazsa", "sazszsa")) }
        val map2 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("123", "123")) }

        assertFalse { map1.hashCode() == map2.hashCode() }
    }

    @Test
    fun `hashCode equals consistency test`(){
        val map1 = TriadMap<String, String>().also { it.setTriad(Triad.ofSuccess("333", "")) }
        val map2 = TriadMap<String, String>().also { it.setTriad(Triad.ofMissing("333")) }
        val map3 = TriadMap<String, String>().also { it.setTriad(Triad.ofError("333", RuntimeException())) }

        assertTrue { map1.hashCode() == map2.hashCode() }
        assertTrue { map1 == map2 }


        assertTrue { map1.hashCode() == map3.hashCode() }
        assertTrue { map1 == map3 }

        assertTrue { map2.hashCode() == map3.hashCode() }
        assertTrue { map2 == map3 }
    }
}