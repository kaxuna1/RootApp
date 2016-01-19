package kakha.com.roottest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kakha.com.roottest.Classes.RootCPU;
import kakha.com.roottest.Models.CPU;

public class CpuActivity extends AppCompatActivity {

    RootCPU rootCPU = new RootCPU();
    CPU cpu;
    List<ProgressBar> progressBars=new ArrayList<ProgressBar>();


    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayout=(LinearLayout)findViewById(R.id.linarLayout2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        cpu = rootCPU.ReadCPUinfo();
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        for(int i=0;i<cpu.cores.size();i++){
                            new UpdateCpuClockSpeeds().doInBackground(i);
                        }

                    }
                }, 0, 1, TimeUnit.SECONDS);
        LayoutInflater factory = LayoutInflater.from(this);
        for(int i=0;i<cpu.cores.size();i++){
            View myView = factory.inflate(R.layout.cpucloack, null);
            ProgressBar progressBar=(ProgressBar)myView.findViewById(R.id.cpuBar);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBars.add(progressBar);

            linearLayout.addView(myView);
        }
    }


    public class UpdateCpuClockSpeeds extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String result = rootCPU.getCpuCurrentFreq(cpu.cores.get(params[0]).name);
            int current=Integer.parseInt(result);
            int cpuMax=Integer.parseInt(cpu.cores.get(params[0]).maxFreq);
            int minFreq=Integer.parseInt(cpu.cores.get(params[0]).minFreq);
            publishProgress(result);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //textView.setText(values[0]);
        }
    }

}
