package com.ancestry.kodeinapplication

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory


class KodeinApplication: Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind() from factory { name: String -> KodeinDomain(name) }
    }
}