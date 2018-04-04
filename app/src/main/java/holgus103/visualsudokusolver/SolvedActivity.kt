package holgus103.visualsudokusolver

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SolvedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solved)
    }

    fun save(v: View){
        val i = Intent(this, MainActivity::class.java);
        this.startActivity(i);
    }
}
