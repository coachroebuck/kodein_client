package com.ancestry.kodeinapplication

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class KodeinApplication: Application(), KodeinAware {

    override val kodein = Kodein {
        bind<KodeinDomain>(tag = "home") with singleton { KodeinDomain("home") }
        bind<KodeinDomain>(tag = "remote") with singleton { KodeinDomain("remote") }
        bind<KodeinDomain>(tag = "ngrok") with singleton { KodeinDomain("ngrok") }
        bind<KodeinDomain>(tag = "stage") with singleton { KodeinDomain("stage") }
        bind<KodeinDomain>(tag = "live") with singleton { KodeinDomain("live") }
    }

}