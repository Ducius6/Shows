package com.example.ducius

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_episode.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        const val EPISODE_TITLE = "episode_title"
        const val EPISODE_DESC = "episode_desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        setSupportActionBar(episodeToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        episodeTitleEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editableText: Editable) {}

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                saveButton.isEnabled =
                    (episodeTitleEditText.text.isNotEmpty() && episodeDescEditText.text.isNotEmpty())
            }
        })

        episodeDescEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                saveButton.isEnabled =
                    (episodeTitleEditText.text.isNotEmpty() && episodeDescEditText.text.isNotEmpty())
            }
        })

        saveButton.setOnClickListener {
            var intent = Intent()
            intent.putExtra(EPISODE_TITLE, episodeTitleEditText.text.toString())
            intent.putExtra(EPISODE_DESC, episodeDescEditText.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (episodeTitleEditText.text.isNotEmpty() || episodeDescEditText.text.isNotEmpty()) {
                createDialog()
            } else {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun createDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to go back?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, _ -> this@AddEpisodeActivity.finish() })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        val alert = builder.create()
        alert.show()
    }
}
