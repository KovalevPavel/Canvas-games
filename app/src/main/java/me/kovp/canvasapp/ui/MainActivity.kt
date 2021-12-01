package me.kovp.canvasapp.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.kovp.canvasapp.databinding.ActivityMainBinding
import me.kovp.canvasapp.di.ApplicationScope
import me.kovp.canvasapp.di.injectViewModel
import me.kovp.canvasapp.models.AnimationParams
import me.kovp.canvasapp.models.DrawParams
import me.kovp.canvasapp.ui.viewModel.AnimationViewModel
import me.kovp.canvasapp.ui.viewModel.AnimationViewModelImpl
import me.kovp.canvasapp.utils.coloredGradient
import me.kovp.canvasapp.utils.coloredPaint
import me.kovp.canvasapp.utils.textPaint
import me.kovp.canvasapp.utils.toDoubleOrNull
import me.kovp.canvasapp.utils.toIntOrNull
import toothpick.Toothpick
import toothpick.ktp.KTP

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bitmap: Bitmap by lazy {
        Bitmap.createBitmap(
            binding.ivImage.width, binding.ivImage.height,
            Bitmap.Config.ARGB_8888
        )
    }
    private val canvas: Canvas by lazy { Canvas(bitmap) }
    private val vm: AnimationViewModel by injectViewModel<AnimationViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toothpick.inject(this, KTP.openScope(ApplicationScope::class.java))
    }

    override fun onStart() {
        super.onStart()
        binding.btnStartAnimation.setOnClickListener { startAnimation() }
        binding.ivImage.setOnClickListener (
            DoubleClickListener(
                onSingleClick = vm::onSingleClick,
                onDoubleClick = vm::onDoubleClick
            )
        )

        vm.sphere.observe(this, ::applyNewImage)
    }

    private fun startAnimation() = AnimationParams(
        overallDuration = binding.etOverallDuration.text.toIntOrNull(),
        dayDuration = binding.etDayDuration.text.toDoubleOrNull()
    ).let(vm::startAnimWithParams)

    private fun applyNewImage(params: DrawParams) {
        val (point, sphere, background) = params
        canvas.drawRect(
            0f,
            0f,
            background.width,
            background.height,
            coloredPaint(background.color)
        )
        canvas.drawCircle(
            point.x.toFloat(),
            point.y.toFloat(),
            sphere.radius.toFloat(),
            coloredGradient(point, sphere.color, sphere.radius.toFloat())
        )
        canvas.drawText(
            sphere.text,
            point.x.toFloat(),
            point.y.toFloat(),
            textPaint()
        )
        binding.ivImage.background = BitmapDrawable(resources, bitmap)
    }
}