package com.ancestry.kodeinapplication

import android.app.Application
import com.ancestry.kodeinapplication.kodein.conf.ConfigurableKodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider


class KodeinApplication: Application(), KodeinAware {

    companion object {
        var kodein: ConfigurableKodein? = null

        fun configure(env: String) {
            kodein?.clear()

            kodein?.addConfig {
                bind<KodeinDomain>() with provider { KodeinDomain(env) }
            }
        }
    }
    override val kodein : ConfigurableKodein = ConfigurableKodein(true)

    override fun onCreate() {
        super.onCreate()

        KodeinApplication.kodein = kodein
    }

}