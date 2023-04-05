package ru.seraf1n.moviefinder.view.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import ru.seraf1n.moviefinder.R
import kotlin.math.min

const val MULTIPLIER_2X = 2f
const val MULTIPLIER_4X = 4
const val MULTIPLIER_08 = 0.8f
const val START_ANGLE = -90f
const val MULTIPLIER_10X = 10f
const val MULTIPLIER_36 = 3.6f


class RatingDonutView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()

    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    //Толщина линии прогресса
    private var stroke = 10f

    //Значение прогресса от 0 - 100
    private var progress = 50

    //Значения размера текста внутри кольца
    private var scaleSize = 60f

    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    private var animator: ValueAnimator? = null
    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(
                R.styleable.RatingDonutView_stroke, stroke
            )
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    private fun initPaint() {
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = MULTIPLIER_2X
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }

    }

    private fun getPaintColor(progress: Int): Int = when (progress) {
        in 0..25 -> Color.parseColor(context.getString(R.string.Color_0_to_25))
        in 26..50 -> Color.parseColor(context.getString(R.string.color_26_to_50))
        in 51..75 -> Color.parseColor(context.getString(R.string.color_51_to_75))
        else -> Color.parseColor(context.getString(R.string.color_76_above))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(MULTIPLIER_2X)
        } else {
            width.div(MULTIPLIER_2X)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = min(chosenWidth, chosenHeight)
        centerX = minSide.div(MULTIPLIER_2X)
        centerY = minSide.div(MULTIPLIER_2X)

        setMeasuredDimension(minSide, minSide)
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    override fun onDraw(canvas: Canvas) {
        //Рисуем кольцо и задний фон
        drawRating(canvas)
        //Рисуем цифры
        drawText(canvas)
    }

    fun setProgress(pr: Int) {
        //Кладем новое значение в наше поле класса
        progress = pr
        //Создаем краски с новыми цветами
        initPaint()
        startAnimation()
        //вызываем перерисовку View
        invalidate()
    }

    private fun drawRating(canvas: Canvas) {
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * MULTIPLIER_08
        //Сохраняем канвас
        canvas.save()
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        canvas.translate(centerX, centerY)
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        canvas.drawArc(oval, START_ANGLE, convertProgressToDegrees(progress), false, strokePaint)
        //Восстанавливаем канвас
        canvas.restore()
    }
    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, progress).apply {
            duration = 1500
            interpolator =  AccelerateDecelerateInterpolator()
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
        }
        animator?.start()
    }

    private fun drawText(canvas: Canvas) {
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
        val message = String.format("%.1f", progress / MULTIPLIER_10X)
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / MULTIPLIER_2X, centerY + advance / MULTIPLIER_4X, digitPaint)
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * MULTIPLIER_36
}


