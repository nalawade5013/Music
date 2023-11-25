package ishubh.example.sharaa_music;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.faint)));
           list=findViewById(R.id.list);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                      ArrayList<File> mySong=fetchsong(Environment.getExternalStorageDirectory()); // read song on folder
                        String items[]=new String[mySong.size()]; // read song name on folder
                        for(int i=0;i<mySong.size();i++){
                            items[i]=mySong.get(i).getName().replace(".mp3","");


                        }
                        ArrayAdapter<String> ad=new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1,items);
                        list.setAdapter(ad);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,Mylist.class);
                                String currentSong=list.getItemAtPosition(position).toString();
                                intent.putExtra("songlist",mySong);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    // this nnethod is used for fetch the all song in folder
   public  ArrayList<File> fetchsong(File f){

        ArrayList arrylist=new ArrayList();
        File[] song=f.listFiles();
        if(song !=null){
            for(File myFile: song){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrylist.addAll(fetchsong(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrylist.add(myFile);
                    }
                }
            }
        }
        return arrylist;
    }
}