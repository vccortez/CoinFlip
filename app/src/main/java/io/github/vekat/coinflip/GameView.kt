package io.github.vekat.coinflip

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.game_view.view.*
import java.security.SecureRandom

@Suppress("UNUSED_PARAMETER")
/**
 * A compound ViewGroup based on [RelativeLayout] to present the game.
 */
class GameView : RelativeLayout, Animation.AnimationListener {
  companion object {
    val DECELERATE_INTERPOLATOR = DecelerateInterpolator()
    val ACCELERATE_INTERPOLATOR = AccelerateInterpolator()
  }

  private val coinImageView: ImageView by lazy { coin_image }
  private val playButton: Button by lazy { play_button }
  private val scoreLabel: TextView by lazy { score_text }
  private val latestFlipLabel: TextView by lazy { latest_flip_text }
  private val headsChoiceButton: Button by lazy { heads_button }
  private val tailsChoiceButton: Button by lazy { tails_button }

  private val rand: SecureRandom = SecureRandom()
  private val anim: Animation = ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

  private var score: Int = 0

  private var isHeads: Boolean = true
  private var chosenHeads: Boolean = true

  private var spinning: Boolean = false

  /**
   * when this value is true, the coin is 'closing' and will end up 'sideways' to the player.
   * when it is false, the coin is 'opening' and will show up as 'head or tails'.
   */
  private var animationCycleStart: Boolean = true

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(attrs)
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
    init(attrs, defStyle)
  }

  private fun init(attrs: AttributeSet? = null, defStyle: Int = 0) {
    LayoutInflater.from(context).inflate(R.layout.game_view, this, true)

    anim.duration = 60
    anim.fillAfter = true
    anim.setAnimationListener(this)

    headsChoiceButton.setOnClickListener(toggleChoiceButtons)
    tailsChoiceButton.setOnClickListener(toggleChoiceButtons)

    playButton.setOnClickListener(onPlayButton)
  }

  private val toggleChoiceButtons: (_: View?) -> Unit = {
    chosenHeads = !chosenHeads
    headsChoiceButton.isEnabled = !chosenHeads
    tailsChoiceButton.isEnabled = chosenHeads
  }

  private val onPlayButton: (_: View?) -> Unit = {
    if (spinning) {
      spinning = false
      playButton.text = resources.getString(R.string.flip)
    } else {
      spinning = true
      playButton.text = resources.getString(R.string.stop)
      startSpinAnimation()
    }
  }

  private fun updateCoinImage() {
    coinImageView.setImageResource(if (isHeads) R.drawable.coina else R.drawable.coinb)
  }

  private fun startSpinAnimation() {
    animationCycleStart = true

    anim.interpolator = ACCELERATE_INTERPOLATOR

    coinImageView.startAnimation(anim)
  }

  private fun selectRandomCoin() {
    isHeads = rand.nextBoolean()

    updateCoinImage()

    if (chosenHeads == isHeads) {
      score++
    }

    if (score <= 0) {
      scoreLabel.text = resources.getString(R.string.zero_score)
    } else {
      scoreLabel.text = resources.getString(R.string.other_score, score)
    }

    val latestResult = if (isHeads) resources.getString(R.string.heads) else resources.getString(R.string.tails)

    latestFlipLabel.text = resources.getString(R.string.latest_flip, latestResult)
  }

  override fun onAnimationEnd(p0: Animation?) {
    animationCycleStart = !animationCycleStart

    if (animationCycleStart) {
      if (!spinning) {
        selectRandomCoin()
        return
      }

      anim.interpolator = ACCELERATE_INTERPOLATOR
    } else {
      isHeads = !isHeads

      anim.interpolator = ReverseInterpolator(DECELERATE_INTERPOLATOR)
    }

    coinImageView.startAnimation(anim)
  }

  override fun onAnimationStart(p0: Animation?) {
    updateCoinImage()
  }

  override fun onAnimationRepeat(p0: Animation?) = Unit

  inner class ReverseInterpolator(private val delegate: Interpolator) : Interpolator {
    override fun getInterpolation(input: Float): Float = 1f - delegate.getInterpolation(input)
  }
}
