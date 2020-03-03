package com.bluestem.app

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set UI.
        setContentView(R.layout.activity_main)

        // Setup MaterialDropdownView.
        setupMaterialDropdownView()
    }

    private fun setupMaterialDropdownView() {
        // Setup toggle states adapter.
        toggle_enable_states_button.setOnClickListener {
            material_dropdown_0.isEnabled = !material_dropdown_0.isEnabled
            material_dropdown_1.isEnabled = !material_dropdown_1.isEnabled
            material_dropdown_2.isEnabled = !material_dropdown_2.isEnabled
            material_dropdown_3.isEnabled = !material_dropdown_3.isEnabled
            material_dropdown_4.isEnabled = !material_dropdown_4.isEnabled
        }

        // Setup adapters.
        val adapter = ArrayAdapter(
            this,
            R.layout.default_item,
            arrayOf<String>("Item 0", "Item 1", "Item 2", "Item 3")
        )

        material_dropdown_0.adapter = adapter
        material_dropdown_1.adapter = adapter
        material_dropdown_2.adapter = adapter
        material_dropdown_3.adapter = adapter
        material_dropdown_4.adapter = adapter
    }
}
