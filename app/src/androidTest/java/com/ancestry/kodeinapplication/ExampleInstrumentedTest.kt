package com.ancestry.kodeinapplication

import android.content.Context
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.rule.ActivityTestRule

import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runners.Parameterized
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(Parameterized::class)
class ExampleInstrumentedTest(private val environment: String): KodeinAware {

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

    @Suppress("BooleanLiteralArgument")
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun launchActivity() {
        val intent = Intent(appContext, MainActivity::class.java)
        intent.putExtra("environment", environment)
        intent.putExtra("selectedIndex", data().indexOf(environment))
        activityRule.launchActivity(intent)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.ancestry.kodeinapplication", appContext.packageName)
    }

    @Test
    fun testEnvironmentSet() {
        launchActivity()
        val textOfInterest = String.format(
            Locale.getDefault(),
            appContext.getString(R.string.current_environment, environment))
        Espresso.onView(ViewMatchers.withText(textOfInterest)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
