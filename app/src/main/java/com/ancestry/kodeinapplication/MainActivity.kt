package com.ancestry.kodeinapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var exampleDomain: KodeinDomain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KodeinApplication.configure(intent?.getStringExtra("environment") ?: "home")
        exampleDomain = retrieveDomain()
        textView.text = String.format(Locale.getDefault(),
            getString(R.string.current_environment,
                exampleDomain.text))
        button.setOnClickListener { launchNextActivity() }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val env = parent?.getItemAtPosition(position).toString()
                KodeinApplication.configure(env)
                exampleDomain = retrieveDomain()
                textView.text = String.format(Locale.getDefault(),
                    getString(R.string.current_environment,
                        exampleDomain.text))
            }
        }
    }

    private fun retrieveDomain(): KodeinDomain {
        val exampleDomain: KodeinDomain by instance()
        return exampleDomain
    }

    private fun launchNextActivity() {

    }
}
