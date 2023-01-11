package com.loop.unpacker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.loop.render.IRender
import com.loop.unpacker.Test_render.Test_render

class MainActivity : AppCompatActivity() {
    private lateinit var render: IRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        render = Test_render { cmd, obj ->
            Log.e("MainActivity", "Cmd=$cmd")
            Log.e("MainActivity", "obj=$obj")
        }
    }

    fun test(view: View) {
        render.render(byteArrayOf(0x0, 0x1, 0x2, 0x0, 0x0, 0x5, 0x6, 0x7, 0x8, 0x9))
    }
}