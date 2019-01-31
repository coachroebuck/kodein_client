package com.ancestry.kodeinapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()

    private var domain = "home"
    private val exampleDomain: KodeinDomain by instance(tag = domain)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.text = exampleDomain.text
        button.setOnClickListener { launchNextActivity() }
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(parent?.context,
                    "OnItemSelectedListener : " + parent?.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show()
                button.text = exampleDomain.text
            }
        }
    }

    private fun launchNextActivity() {

    }
}
