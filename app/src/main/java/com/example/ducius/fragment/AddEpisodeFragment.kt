package com.example.ducius.fragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ducius.R
import com.example.ducius.model.PostEpisode
import com.example.ducius.shared.gone
import com.example.ducius.shared.visible
import com.example.ducius.ui.AddEpisodeViewModel
import com.example.ducius.ui.LoginActivity
import kotlinx.android.synthetic.main.camera_gallery_dialog_layout.view.*
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.picker_layout.view.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date

private const val MIN_SEASON_EPISODE = 1
private const val MAX_SEASON = 20
private const val MAX_EPISODE = 99
private const val TEN = 10
private const val REQUEST_CAMERA_PERMISSION = 99
private const val TAKE_PIC_REQUEST_CODE = 6
private const val REQUEST_GALLERY_PERMISSION = 66
private const val PIC_FROM_GALLERY_REQUEST_CODE = 7
private var episodeBitmap: Bitmap? = null

class AddEpisodeFragment : Fragment() {

    private var uri: Uri? = null
    private var showID = ""
    private var file: File? = null
    private var pathToFile: String? = null
    private lateinit var viewModel: AddEpisodeViewModel
    private var season: String = "1"
    private var episode: String = "1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddEpisodeViewModel::class.java)

        arguments?.let {
            showID = it.getString(ShowDetailsFragment.SHOW_ID)
        }

        if (viewModel.episodeImageURi != null) {
            uri = Uri.parse(viewModel.episodeImageURi)
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
            episodeImageView.setImageBitmap(bitmap)
            changeViewsVisibility()
        }

        if (viewModel.seasonEpisode != null) {
            pickSeasonAndEp.text = viewModel.seasonEpisode
        }

        val cameraInflater = LayoutInflater.from(context).inflate(R.layout.camera_gallery_dialog_layout, null)

        val cameraAndGalleryDialog = AlertDialog.Builder(requireContext())
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

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(episodeToolbar)
        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val inflater = LayoutInflater.from(context).inflate(R.layout.picker_layout, null)
        with(inflater) {
            seasonNumberPicker.maxValue = MAX_SEASON
            seasonNumberPicker.minValue = MIN_SEASON_EPISODE
            episodeNumberPicker.maxValue = MAX_EPISODE
            episodeNumberPicker.minValue = MIN_SEASON_EPISODE
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(inflater)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.save), DialogInterface.OnClickListener { dialog, _ ->
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
                viewModel.saveSeasonAndEpisode(String.format("S %s, E %s", season, episode))
                dialog.cancel()
            }).create()

        pickSeasonAndEp.setOnClickListener {
            dialog.show()
            dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.pink
                )
            )
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
            if (viewModel.getImageUri() == null) {
                Toast.makeText(context, getString(R.string.chose_photo), Toast.LENGTH_LONG).show()
            } else if (episodeTitleEditText.text.isEmpty() || episodeDescEditText.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.all_fields_must_be_filled), Toast.LENGTH_LONG).show()
            } else if (episodeDescEditText.text.length < 2) {
                episodeDescInputLayout.error = getString(R.string.description_characters)
            } else {
                val episode =
                    PostEpisode(
                        showID,
                        episodeTitleEditText.text.toString(),
                        episodeDescEditText.text.toString(),
                        "",
                        episode,
                        season
                    )
                viewModel.postEpisodeData(File(Uri.parse(viewModel.getImageUri()).path), episode)
                viewModel.liveData.observe(this, Observer {
                    if (it.isSuccessful) {
                        activity?.onBackPressed()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.session_timed_out))
                            .setNeutralButton(getString(R.string.OK)) { dialog, _ ->
                                dialog.dismiss()
                                activity?.finishAffinity()
                                startActivity(Intent(context, LoginActivity::class.java))
                            }.show()
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (episodeTitleEditText.text.isNotEmpty() || episodeDescEditText.text.isNotEmpty()) {
                createDialog()
            } else {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.dialog_question))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.positive)
            ) { _, _ -> activity?.onBackPressed() }
            .setNegativeButton(
                getString(R.string.negative)
            ) { dialog, _ -> dialog.cancel() }.show()
    }

    private fun handleCameraPermission() {
        if (PermissionChecker.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.CAMERA
                ) || shouldShowRequestPermissionRationale(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.external_storage_permission))
                    .setNeutralButton(getString(R.string.OK)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissions(
                            arrayOf(
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }.create().show()
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }
    }

    private fun handleGalleryPermission() {
        if (context?.let {
                PermissionChecker.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.external_storage_permission))
                        .setNeutralButton(getString(R.string.OK)) { dialog, _ ->
                            dialog.dismiss()
                            requestPermissions(
                                arrayOf(
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                REQUEST_GALLERY_PERMISSION
                            )
                        }.create().show()
                }
            } else {
                requestPermissions(
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
        if (takePic.resolveActivity(activity?.packageManager) != null) {
            val photoFile = createPhotoFile()
            file = photoFile
            if (photoFile != null) {
                pathToFile = photoFile.absolutePath
                uri = context?.let { FileProvider.getUriForFile(it, "com.example.ducius.fileprovider", photoFile) }
                viewModel.saveEpisodeImage(uri.toString())
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, uri)
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
                uri = data?.data
                viewModel.saveEpisodeImage(uri.toString())
                try {
                    episodeBitmap =
                        BitmapFactory.decodeStream(activity?.contentResolver?.openInputStream(data?.getData()))
                    episodeImageView.setImageBitmap(episodeBitmap)
                    changeViewsVisibility()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun changeViewsVisibility() {
        cameraImageView.gone()
        uploadPhotoTextView.gone()
        episodeFrameLayout.visible()
        changePhotoTextView.visible()
    }

    private fun createPhotoFile(): File? {
        var imageFile: File? = null
        try {
            imageFile = File.createTempFile("image",
                ".webp",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            )
        } catch (e: IOException) {
            Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return imageFile
    }
}