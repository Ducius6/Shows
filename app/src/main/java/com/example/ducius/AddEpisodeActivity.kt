package com.example.ducius

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_episode.*
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.camera_gallery_dialog_layout.view.*
import kotlinx.android.synthetic.main.picker_layout.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val MIN_SEASON_EPISODE = 1
private const val MAX_SEASON = 20
private const val MAX_EPISODE = 99
private const val TEN = 10
private const val REQUEST_CAMERA_PERMISSION = 99
private const val TAKE_PIC_REQUEST_CODE = 6
private const val REQUEST_GALLERY_PERMISSION = 66
private const val PIC_FROM_GALLERY_REQUEST_CODE = 7
private const val EPISODE_BYTE_ARRAY_KEY = "episode_bitmap"
private var episodeBitmap: Bitmap? = null

class AddEpisodeActivity : AppCompatActivity() {

    var pathToFile: String? = null

    companion object {
        const val EPISODE_TITLE = "episode_title"
        const val EPISODE_DESC = "episode_desc"
        const val SEASON_EPISODE_NUMBER = "season_episode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        val cameraInflater = LayoutInflater.from(this).inflate(R.layout.camera_gallery_dialog_layout, null)

        val cameraAndGalleryDialog = AlertDialog.Builder(this)
            .setView(cameraInflater)
            .setCancelable(true).create()

        cameraInflater.cameraTextView.setOnClickListener {
            cameraAndGalleryDialog.dismiss()
            handleCameraPermission()
        }

        cameraInflater.galleryTextView.setOnClickListener {
            cameraAndGalleryDialog.dismiss()
            handleGalleryPermission()
        }

        cameraImageView.setOnClickListener {
            cameraAndGalleryDialog.show()
        }

        uploadPhotoTextView.setOnClickListener {
            cameraImageView.performClick()
        }

        changePhotoTextView.setOnClickListener {
            cameraAndGalleryDialog.show()
        }

        episodeFrameLayout.setOnClickListener {
            changePhotoTextView.performClick()
        }

        setSupportActionBar(episodeToolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val inflater = LayoutInflater.from(this).inflate(R.layout.picker_layout, null)
        inflater.seasonNumberPicker.maxValue = MAX_SEASON
        inflater.seasonNumberPicker.minValue = MIN_SEASON_EPISODE
        inflater.episodeNumberPicker.maxValue = MAX_EPISODE
        inflater.episodeNumberPicker.minValue = MIN_SEASON_EPISODE

        val dialog = AlertDialog.Builder(this)
            .setView(inflater)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener { dialog, _ ->
                val episode: String
                val season: String
                if (inflater.episodeNumberPicker.value < TEN) {
                    episode = String.format("0%s", inflater.episodeNumberPicker.value.toString())
                } else {
                    episode = inflater.episodeNumberPicker.value.toString()
                }
                if (inflater.seasonNumberPicker.value < TEN) {
                    season = String.format("0%s", inflater.seasonNumberPicker.value.toString())
                } else {
                    season = inflater.seasonNumberPicker.value.toString()
                }
                pickSeasonAndEp.text = String.format("S %s, E %s", season, episode)
                dialog.cancel()
            }).create()

        pickSeasonAndEp.setOnClickListener {
            dialog.show()
            dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.pink))
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
            val intent = Intent()
            intent.putExtra(EPISODE_TITLE, episodeTitleEditText.text.toString())
            intent.putExtra(EPISODE_DESC, episodeDescEditText.text.toString())
            intent.putExtra(SEASON_EPISODE_NUMBER, pickSeasonAndEp.text)
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

    private fun createDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dialog_question))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.positive)
            ) { _, _ -> this@AddEpisodeActivity.finish() }
            .setNegativeButton(
                getString(R.string.negative)
            ) { dialog, _ -> dialog.cancel() }.create().show()
    }

    private fun handleCameraPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Please allow this app to use camera and external storage")
                    .setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }.create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun handleGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Please allow this app to open your gallery")
                    .setNeutralButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            REQUEST_GALLERY_PERMISSION
                        )
                    }.create().show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_GALLERY_PERMISSION
                )
            }
        }
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PIC_FROM_GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            handleCameraPermission()
        }
        if (grantResults.isNotEmpty() && requestCode == REQUEST_GALLERY_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            handleGalleryPermission()
        }
    }

    private fun openCamera() {
        val takePic = Intent("android.media.action.IMAGE_CAPTURE")
        if (takePic.resolveActivity(packageManager) != null) {
            val photoFile = createPhotoFile()
            if (photoFile != null) {
                pathToFile = photoFile.absolutePath
                takePic.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, "com.example.ducius.fileprovider", photoFile)
                )
                startActivityForResult(takePic, TAKE_PIC_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PIC_REQUEST_CODE) {
                episodeBitmap = BitmapFactory.decodeFile(pathToFile)
                episodeImageView.setImageBitmap(episodeBitmap)
                changeViewsVisibility()
            } else if (requestCode == PIC_FROM_GALLERY_REQUEST_CODE) {
                try {
                    episodeBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.getData()))
                    episodeImageView.setImageBitmap(episodeBitmap)
                    changeViewsVisibility()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeViewsVisibility() {
        cameraImageView.visibility = View.GONE
        uploadPhotoTextView.visibility = View.GONE
        episodeFrameLayout.visibility = View.VISIBLE
        changePhotoTextView.visibility = View.VISIBLE
    }

    private fun createPhotoFile(): File? {
        var imageFile: File? = null
        try {
            imageFile = File.createTempFile(
                SimpleDateFormat(getString(R.string.date_format)).format(Date()),
                ".webp",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            )
        } catch (e: IOException) {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return imageFile
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        val stream: ByteArrayOutputStream? = ByteArrayOutputStream()
        if (episodeBitmap != null) {
            episodeBitmap?.compress(Bitmap.CompressFormat.WEBP, 100, stream)
            savedInstanceState.putByteArray(EPISODE_BYTE_ARRAY_KEY, stream?.toByteArray())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getByteArray(EPISODE_BYTE_ARRAY_KEY) != null) {
            val array = savedInstanceState.getByteArray(EPISODE_BYTE_ARRAY_KEY)
            episodeImageView.setImageBitmap(BitmapFactory.decodeByteArray(array, 0, array.size))
            changeViewsVisibility()
        }
    }
}