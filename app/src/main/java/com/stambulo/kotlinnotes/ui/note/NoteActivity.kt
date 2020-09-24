package com.stambulo.kotlinnotes.ui.note

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_note.*

import com.stambulo.kotlinnotes.R
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.extensions.getColorInt
import com.stambulo.kotlinnotes.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
    companion object {
        private const val NOTE_KEY = "note"
        private const val DATE_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(NOTE_KEY, noteId)
            context.startActivity(this)
        }
    }

    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    override val viewModel: NoteViewModel by viewModel()

    var color: Note.Color = Note.Color.WHITE

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(NOTE_KEY)

        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title =  getString(R.string.new_note)
            initView()
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) {
            finish()
            return
        }

        this.note = data.note
        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textWatcher)
        et_body.removeTextChangedListener(textWatcher)

        note?.let {
            et_title.setTextKeepState(it.title)
            et_body.setTextKeepState(it.text)
            toolbar.setBackgroundColor(it.color.getColorInt(this))
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(it.lastChanged)
        }

        et_title.addTextChangedListener(textWatcher)
        et_body.addTextChangedListener(textWatcher)

        colorPicker.onColorClickListener = {
            color = it
            toolbar.setBackgroundColor(it.getColorInt(this))
            saveNote()
        }
    }

    private fun saveNote() {
        et_title.text?.let {
            if (it.length < 3) return
        } ?: return

        note = note?.copy(
            title = et_title.text.toString(),
            text = et_body.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: Note(UUID.randomUUID().toString(), et_title.text.toString(), et_body.text.toString(), color)

        note?.let { viewModel.save(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.palette -> togglePallete().let { true }
        R.id.delete -> deleteNote().let { true }
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePallete() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete))
            .setNegativeButton(getString(R.string.note_delete_cancel)) { dialog, which -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.note_delete_ok)) { dialog, which -> viewModel.deleteNote() }
            .show()
    }

}