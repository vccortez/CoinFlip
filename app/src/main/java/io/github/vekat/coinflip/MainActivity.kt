package io.github.vekat.coinflip

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  val gameView: GameView by bind(R.id.game_view)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    /**
     * YOUR CODE GOES HERE
     */
  }
}
