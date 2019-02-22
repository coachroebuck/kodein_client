package com.ancestry.kodeinapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.factory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var domain: KodeinDomain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configure((intent?.getStringExtra("environment") ?: "home"))
        button.setOnClickListener { launchNextActivity() }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateDomainText(env = parent?.getItemAtPosition(position).toString())
            }
        }
        spinner.setSelection(intent?.getIntExtra("selectedIndex", 0) ?: 0)
    }

    private fun configure(env: String) {
        updateDomainText(env)
    }

    private fun updateDomainText(env: String) {
        val kodein by closestKodein(this)
        val domainFactory: (String) -> KodeinDomain by kodein.factory()

        domain = domainFactory(env)
        textView.text = String.format(Locale.getDefault(),
            getString(R.string.current_environment,
                domain.text))
    }

    private fun launchNextActivity() {

    }
}
