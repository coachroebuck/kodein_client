package com.ancestry.kodeinapplication.kodein.conf

import com.ancestry.kodeinapplication.KodeinDomain
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ConfigurableKodeinTest {

    private open class Name(val firstName: String) {
        override fun equals(other: Any?): Boolean{
            if (this === other) return true
            if (other !is Name) return false
            if (firstName != other.firstName) return false
            return true
        }

        override fun hashCode(): Int{
            return firstName.hashCode()
        }
    }

    private class FullName(firstName: String, val lastName: String) : Name(firstName) {
        override fun equals(other: Any?): Boolean{
            if (this === other) return true
            if (other !is FullName) return false
            if (!super.equals(other)) return false
            if (lastName != other.lastName) return false
            return true
        }

        override fun hashCode(): Int{
            return 31 * super.hashCode() + lastName.hashCode()
        }
    }

    @Test
    fun test00_01_Domain() {
        val kodein = ConfigurableKodein(true)

        kodein.addConfig {
            bind<KodeinDomain>() with provider { KodeinDomain("test") }
        }

        val testAnswer: KodeinDomain by kodein.instance()

        assertEquals("test", testAnswer.text)

        kodein.clear()

        kodein.addConfig {
            bind<KodeinDomain>() with provider { KodeinDomain("stage") }
        }

        val stageAnswer: KodeinDomain by kodein.instance()

        assertEquals("stage", stageAnswer.text)

        kodein.clear()

        kodein.addConfig {
            bind<KodeinDomain>() with provider { KodeinDomain("prod") }
        }

        val prodAnswer: KodeinDomain by kodein.instance()

        assertEquals("prod", prodAnswer.text)
    }

    @Test fun test00_00_Configurable() {
        val kodein = ConfigurableKodein()

        kodein.addConfig {
            constant(tag = "answer") with 42
        }

        assertTrue(kodein.canConfigure)

        val answer: Int by kodein.instance(tag = "answer")

        assertEquals(42, answer)

        assertFalse(kodein.canConfigure)
    }

    @Test fun test01_04_Clear() {
        val kodein = ConfigurableKodein(true)

        kodein.addImport(Kodein.Module("Test Module") {
            constant(tag = "answer") with 21
        })

        assertEquals(21, kodein.direct.instance(tag = "answer"))

        kodein.clear()

        kodein.addConfig {
            constant(tag = "answer") with 42
        }

        assertEquals(42, kodein.direct.instance(tag = "answer"))
    }

    @Test fun test01_02_Mutate() {
        val kodein = ConfigurableKodein(true)

        kodein.addExtend(Kodein {
            constant(tag = "half") with 21
        })

        assertEquals(21, kodein.direct.instance(tag = "half"))

        kodein.addConfig {
            constant(tag = "full") with 42
        }

        assertEquals(21, kodein.direct.instance(tag = "half"))
        assertEquals(42, kodein.direct.instance(tag = "full"))
    }

    @Test fun test01_03_NonMutableClear() {
        val kodein = ConfigurableKodein()

        try {

            kodein.addConfig {
                constant(tag = "answer") with 21
            }

            assertEquals(21, kodein.direct.instance(tag = "answer"))
        }
        catch (ex: IllegalStateException)  {
            kodein.clear()
        }
    }

    @Test fun test01_04_NonMutableMutate() {
        val kodein = ConfigurableKodein()

        try {

            kodein.addConfig {
                constant(tag = "answer") with 21
            }

            assertEquals(21, kodein.direct.instance(tag = "answer"))
        }
        catch (ex: IllegalStateException)  {
            kodein.addConfig {}
        }
    }

    @Test fun test02_00_mutateConfig() {
        val kodein = ConfigurableKodein(true)

        kodein.addConfig {
            constant(tag = "half") with 21
        }

        assertEquals(21, kodein.direct.instance(tag = "half"))

        kodein.addConfig {
            constant(tag = "full") with 42
        }

        assertEquals(21, kodein.direct.instance(tag = "half"))
        assertEquals(42, kodein.direct.instance(tag = "full"))
    }

    @Test fun test02_01_nonMutableMutateConfig() {
        val kodein = ConfigurableKodein()

        try {

            kodein.addConfig {
                constant(tag = "half") with 21
            }

            assertEquals(21, kodein.direct.instance(tag = "half"))
        }
        catch (ex: IllegalStateException)  {
            kodein.addConfig {}
        }
    }

    @Test
    fun test03_00_ChildOverride() {
        val kodein = ConfigurableKodein(true)

        kodein.addConfig {
            bind<String>() with factory { n: FullName -> n.firstName }
        }

        assertEquals("Salomon", kodein.direct.factory<FullName, String>().invoke(FullName("Salomon", "BRYS")))

        kodein.addConfig {
            bind<String>(overrides = true) with factory { n: FullName -> n.firstName + " " + n.lastName }
        }

        assertEquals("Salomon BRYS", kodein.direct.factory<FullName, String>().invoke(FullName("Salomon", "BRYS")))
    }

    class Test04 : KodeinGlobalAware {
        val answer: Int by instance(tag = "full")
    }

    @Test fun test04_00_Global() {
        Kodein.global.mutable = true

        Kodein.global.addConfig {
            constant(tag = "half") with 21
        }

        assertEquals(21, Kodein.global.direct.instance(tag = "half"))

        Kodein.global.addConfig {
            constant(tag = "full") with 42
        }

        assertEquals(21, Kodein.global.direct.instance(tag = "half"))
        assertEquals(42, Test04().answer)
    }

    class Test05 {
        val kodein = ConfigurableKodein()

        inner class Loop(@Suppress("UNUSED_PARAMETER") text: String = kodein.direct.instance())
    }

    @Test
    fun test05_00_Loop() {
        val test = Test05()

        test.kodein.addConfig {
            bind() from singleton { "test" }
            bind() from eagerSingleton { test.Loop() }
        }

        test.kodein.getOrConstruct()
    }

    @Test
    fun test06_00_Callback() {
        val kodein = ConfigurableKodein()

        var ready = false

        kodein.addConfig {
            bind() from singleton { "test" }

            onReady {
                ready = true
            }

            assertFalse(ready)
        }

        assertFalse(ready)

        val value: String by kodein.instance()

        assertFalse(ready)

        assertEquals("test", value)

        assertTrue(ready)
    }
}