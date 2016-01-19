package kakha.com.roottest.Classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kakha.com.roottest.Models.CPU;
import kakha.com.roottest.Models.Core;

/**
 * Created by kakha on 1/18/2016.
 */
public class RootCPU extends ExecuteAsRootBase {
    @Override
    protected ArrayList<String> getCommandsToExecute() {
        return null;
    }
    public CPU ReadCPUinfo()
    {
        CPU cpu=new CPU();
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                //System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        String[] separated = result.split("\n");
        for(int i=0;i<separated.length;i++){
            String current=separated[i];
            if(current.indexOf("Processor")!=-1){
                cpu.hardware=current.substring(current.indexOf(":")+1,current.length()).trim();
            }
            if(current.indexOf("architecture")!=-1){
                cpu.architecture=current.substring(current.indexOf(":")+1,current.length()).trim();
            }
            if(current.indexOf("Hardware")!=-1){
                cpu.hardware=current.substring(current.indexOf(":")+1,current.length()).trim();
            }
            if(current.indexOf("processor")!=-1){
                Core core=new Core();
                core.name=current.substring(current.indexOf(":")+1,current.length()).trim();
                core.maxFreq=getCpuMaxFreq(core.name);
                core.minFreq=getCpuMinFreq(core.name);
                cpu.cores.add(core);
            }
        }



        //System.out.println(cpu.architecture);

        return cpu;
    }
    public String GetCPUinfo()
    {
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                //System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }



        return result;
    }
    public String getCpuMaxFreq(String cpu){
        return executeCommandBinCat("/sys/devices/system/cpu/cpu" + cpu + "/cpufreq/cpuinfo_max_freq");
    }
    public String getCpuMinFreq(String cpu){
        return executeCommandBinCat("/sys/devices/system/cpu/cpu" + cpu + "/cpufreq/cpuinfo_min_freq");
    }
    public String getCpuCurrentFreq(String cpu){
        return executeCommandBinCat("/sys/devices/system/cpu/cpu" + cpu + "/cpufreq/scaling_cur_freq");
    }

    public String executeCommandBinCat(String command){
        ProcessBuilder cmd;
        String result="";

        try{
            String[] args = {"/system/bin/cat", command};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1){
                //System.out.println(new String(re));
                result = result + new String(re);
            }
            in.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }



        return result;
    }

}
