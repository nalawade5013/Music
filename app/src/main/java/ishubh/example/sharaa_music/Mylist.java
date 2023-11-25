package ishubh.example.sharaa_music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Mylist extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();   // ctl+o for override method
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeak.interrupt();
    }

    TextView textView;
    ImageView play,back,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
        Thread  updateSeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylist);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.fa)));
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.play);
        back=findViewById(R.id.back);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);

        Intent inten=getIntent();

        Bundle bd=inten.getExtras();
        songs=(ArrayList) bd.getParcelableArrayList("songlist");
        textContent= inten.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
       position=inten.getIntExtra("position",0);

       Uri uri=Uri.parse(songs.get(position).toString());
       mediaPlayer=MediaPlayer.create(Mylist.this,uri);
       mediaPlayer.start();
       seekBar.setMax(mediaPlayer.getDuration());   // avoide if the user touch the seekbar then song will be stopped  and restart song from starting so avoide this we can use this line

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  // seekbar update
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
          mediaPlayer.seekTo(seekBar.getProgress());
           }
       });

       updateSeak=new Thread(){
           @Override
           public void run() {
               super.run();

               int currentposition=0;
               try{
                   while(currentposition<mediaPlayer.getDuration()){  // this line for if less duration for currentposition from mediplayer
                       currentposition=mediaPlayer.getCurrentPosition();  // this run method is used to update seekbar
                       seekBar.setProgress(currentposition);
                       sleep(800);
                   }
               }catch(Exception e){
                   e.printStackTrace();
               }

           }
       };
       updateSeak.start();


   play.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(mediaPlayer.isPlaying()){
               play.setImageResource(R.drawable.play);
               mediaPlayer.pause();
           }else{
               play.setImageResource(R.drawable.pause);
               mediaPlayer.start();
           }
       }
   });

   back.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           mediaPlayer.stop();
           mediaPlayer.release();
           if(position!=0){
               position=position-1;
           }
           else{
               position=songs.size()-1;
           }
           Uri uri=Uri.parse(songs.get(position).toString());
           mediaPlayer=MediaPlayer.create(Mylist.this,uri);
           mediaPlayer.start();
           play.setImageResource(R.drawable.pause);
           seekBar.setMax(mediaPlayer.getDuration());
           textContent= songs.get(position).getName().toString();
           textView.setText(textContent);
       }
   });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(Mylist.this,uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textContent= songs.get(position).getName().toString();
                textView.setText(textContent);





            }
        });


    }
}