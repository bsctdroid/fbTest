package bong.android.fcmtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Polarium on 2017-10-18.
 */

public class FirebaseStorageTestActivity extends Activity {
    public String TAG = FirebaseStorageTestActivity.class.getName();

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fcm-push-test-4a05f.appspot.com");
    StorageReference storageRef = storage.getReference();

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fbstorage);
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        //realtime permission
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[] {"Manifest.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

        }

        StorageReference storageRef = storage.getReference();

        // Create a child reference
        // imagesRef now points to "images"
        //StorageReference imagesRef = storageRef.child("images");

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        //StorageReference spaceRef = storageRef.child("images/space.jpg");
        //searchStorageRef();
        //searchStorageRef();
        //exStorageRef();
        //fileUploadMemory();

        //fileUploadLocal();    //성공
        //fileUploadStremLocal(); //성공
        fileDownloadLocal();
    }

    public void fileDownloadLocal()
    {
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("images/photo2.png");
        //mountainsRef = storageRef.child("images/island.jpg");
        Log.d("BONGTEST", "fileDownload ");
        try{
            //File localFile = File.createTempFile("images", "jpg");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/image_temp.jpg";
            File localFile = new File(path);


            mountainsRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.d("BONGTEST", "fileDownload_success");
                    Toast.makeText(FirebaseStorageTestActivity.this, "다운로드 성공", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.d("BONGTEST", "fileDownload_fail");
                    Toast.makeText(FirebaseStorageTestActivity.this, "다운로드 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
        {

        }

    }

    public static Bitmap getBitmapFromSdcard(String _fileName)
    {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + _fileName;

        //Log.d(TAG, "getBitmapFromSdcard path " + path + "_fileName " + _fileName);

        File file = new File(path);
        Uri uri;
        if(file.length() > 200000)	//200kb위로는 안보이게 메모리 문제가능성있음.
            return null;
        else
            uri = Uri.fromFile(new File(path));
//		Uri uri = Uri.fromFile(new File(path));

        return BitmapFactory.decodeFile(uri.getPath());
    }

    /**
     * 파일 스트림 업로드
     */
    private void fileUploadStremLocal()
    {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

        String filePath = rootPath + "/DCIM/Camera/20171015_171348.jpg";

        try {
            InputStream stream = new FileInputStream(new File(filePath));
            StorageReference mountainsRef = storageRef.child("images/photo2.png");
            UploadTask uploadTask = mountainsRef.putStream(stream);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }catch (Exception e) {

        }





    }

    /**
     * 파일 업로드
     */
    private void fileUploadLocal()
    {
        Bitmap bitmap = getBitmapFromSdcard("/abc_launcher.png");
        imageView.setImageBitmap(bitmap);

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        //Uri file = Uri.fromFile(new File(rootPath + "/abc_launcher.png"));
        Uri file = Uri.fromFile(new File(rootPath + "/DCIM/Camera/20171015_160409.jpg"));
        StorageReference riversRef = storageRef.child("images/photo1.png");
        //StorageReference riversRef = storageRef.child("abc_launcher.png");
        UploadTask uploadTask = riversRef.putFile(file);


// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "fbstorage fail exception " + exception.getMessage().toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "fbstorage success exception ");
            }
        });
    }
    /**
     * 파일 업로드(메모리)
     */
    private void fileUploadMemory()
    {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.jpg");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);


        imageView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "fbstorage downloadUrl " + downloadUrl);
            }
        });
    }

    //참조 경로
    private void exStorageRef()
    {
        // Points to the root reference
        storageRef = storage.getReference();

        // Points to "images"
        StorageReference imagesRef = storageRef.child("images");

        // Points to "images/space.jpg"
        // Note that you can use variables to create child values
        String fileName = "thm_general_profile_image_s.png";
        StorageReference spaceRef = imagesRef.child(fileName);

        // File path is "images/space.jpg"
        String path = spaceRef.getPath();

        // File name is "space.jpg"
        String name = spaceRef.getName();

        // Points to "images"
        imagesRef = spaceRef.getParent();

        Log.d(TAG, "fbstorage path " + path + " name " + name);
    }

    //참조 만들기
    private void makeStorageRef()
    {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a child reference
        // imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        StorageReference spaceRef = storageRef.child("images/thm_general_profile_image_s.png.jpg");
    }

    //참조 탐색
    private void searchStorageRef()
    {
        StorageReference storageRef = storage.getReference();
        // getParent allows us to move our reference to a parent node
        // imagesRef now points to 'images'
        StorageReference imagesRef = storageRef.getParent();

        // getRoot allows us to move all the way back to the top of our bucket
        // rootRef now points to the root
        StorageReference rootRef = storageRef.getRoot();

        String rootRefGetPath = rootRef.getPath();
        String rootRefGetName = rootRef.getName();
        String rootRefgetBucekt = rootRef.getBucket();

        Log.d(TAG, "rootRefGetPath " + rootRefGetPath + " rootRefGetName " + rootRefGetName + " rootRefgetBucekt " + rootRefgetBucekt);
    }
}
