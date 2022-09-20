package com.example.imageupload

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.logging.Logger

class ImageActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var pickImage: Button
    private lateinit var upload: Button
    private val mMediaUri: Uri? = null

    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private val btnCapturePicture: Button? = null

    private var mImageFileLocation = ""

    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        imageView = findViewById(R.id.preview) as ImageView
        pickImage = findViewById(R.id.pickImage) as Button
        upload = findViewById(R.id.upload) as Button

        pickImage.setOnClickListener(this)
        upload.setOnClickListener(this)
        initDialog()
    }
    override fun onClick(v: View){
        when (v.id){
            R.id.pickImage -> MaterialDialog.Builder(this)
                .title("Set your Image")
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback{dialog, view, which, text->
                    when (which){
                        0 ->{
                            val galleryIntent = Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
                        }
                        1 ->captureImage()
                        2 ->imageView.setImageResource(R.drawable.ic_launcher_background)

                    }
                }
                .show()
            R.id.upload -> uploadFile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO){
                if (data != null){
                   //get the image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    assert(cursor !=null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)

                    //Set the image in imageView for previewing the media
                    imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()

                    postPath = mediaPath

                }
            }else if (requestCode == CAMERA_PIC_REQUEST){
                if (Build.VERSION.SDK_INT > 21){
                    Glide.with(this).load(fileUri).into(imageView)
                    postPath = fileUri!!.path
                }
            }
        }else if (resultCode != Activity.RESULT_CANCELED){
            Toast.makeText(this, "Sorry, there was an error", Toast.LENGTH_SHORT).show()
        }
    }
    protected fun initDialog(){
     pDialog = ProgressDialog(this)
        pDialog.setMessage(getString(R.string.msg_loading))
        pDialog.setCancelable(true)
    }
    protected fun showpDialog(){
        if (!pDialog.isShowing) pDialog.dismiss()
    }

    private fun captureImage(){
        if (Build.VERSION.SDK_INT >21){
            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
            //we give some instruction to the intent to save the image
            var photoFile: File? = null
            try {
                //if the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile()
            } catch (e: IOException){
                Logger.getAnonymousLogger().info("Exception error in generating the file")
                e.printStackTrace()
            }
            val outputUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION
            )
        }
    }
}
