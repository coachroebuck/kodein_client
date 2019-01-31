package com.ancestry.kodeinapplication

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(Parameterized::class)
class ExampleUnitTest(private val environment: String): KodeinAware {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<String> {
            return arrayOf(
                "home"
                , "remote"
                , "ngrok"
                , "stage"
                , "live"
            ).toList()
        }
    }

    override val kodein = Kodein {
        bind<KodeinDomain>() with singleton { KodeinDomain(environment) }
    }

    private val exampleDomain: KodeinDomain by instance()


    @Test
    fun addition_isCorrect() {
        assertEquals(environment, exampleDomain.text)
    }
}
