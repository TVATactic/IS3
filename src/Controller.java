import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Controller{
    private HashMap<Integer, List<Integer>> macro_tactics = new HashMap<Integer, List<Integer>>();
    private int max_iterations;
    private int cycle = 0;
    private final int GLOBAL_SYMPTOM = 10;

    static final private int MAX_CYCLES = 50;
    static final public int MAX_THRESHOLD = 8;
    static public int critical_failure_count = 0;


    public Controller(int max_iterations, HashMap<Integer, List<Integer>> macro_tactics){
        this.max_iterations = max_iterations;
        this.macro_tactics = macro_tactics;

        ArrayList<ArrayList<Integer>> combined = new ArrayList<>();

        combined.add(runInstance(false)); // Baseline
        combined.add(runInstance(true)); // Purposed algorithm

        writeToCSV(combined);
    }

    private ArrayList<Integer> runInstance(boolean proposed){
        ArrayList<Integer> critical_failures_list = new ArrayList<>();
        int current_iteration = 0;

        while(cycle < MAX_CYCLES) {
            critical_failure_count = 0;
            current_iteration = 0;
            while (current_iteration < max_iterations) {
                MAPEK mape_k = new MAPEK(macro_tactics, proposed);
                mape_k.runMAPEK(GLOBAL_SYMPTOM);

                current_iteration++;
            }
            cycle++;
            critical_failures_list.add(Controller.critical_failure_count);
        }

        cycle = 0;
        return critical_failures_list;
    }

    private void writeToCSV(ArrayList<ArrayList<Integer>> lists){
        String csv_buffer = "Baseline (Critical Failures),Proposed (Critical Failures)\n";
        try{
            FileWriter writer = new FileWriter("output/data.csv");

            ArrayList<Integer> baseline = lists.get(0);
            ArrayList<Integer> proposed = lists.get(1);

            for(int i = 0; i < baseline.size(); i++){
                String line = baseline.get(i) + "," + proposed.get(i) + "\n";
                csv_buffer += line;
            }

            writer.write(csv_buffer);
            writer.close();

        }catch(IOException e){
            System.err.println("Unable to write CSV file. Please check directory/file permissions.");
        }
    }
    
    public static void main(String[] args){
        HashMap<Integer, List<Integer>> tactics = new HashMap<>();

        List<Integer> group1 = Arrays.asList(1,1,1,1,1,1,1,1,1,1,3,3,1,1,9,6,6,
                5,5,4,7,7,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,
                3,3,3,3,1,2,9,1,1,1,1,1,2,1,2,1,4,4,4,4,4,4,4,4,4,5,5,5,
                5,5,4,4,4,4,4,3,5,3,5,3,5,3,3,3,5,3,3,2,2,4,2,2,5,5);
        tactics.put(0, group1);

        List<Double> double_list = Arrays.asList(4.456007,4.082313,3.205769,4.8067,3.364271,3.110964,3.623682,
                4.618294,3.851474,3.121866,5.26111,3.027258,5.289699,3.322588,6.60101,3.413137,4.616915,4.896913,
                4.990854,2.861065,5.818246,4.988922,3.796863,6.216643,4.63566,3.679681,5.094256,2.637219,3.725739,
                3.034386,4.669752,4.94273,4.163923,3.341056,3.90376,4.301815,4.186967,4.670776,4.017151,5.190965,
                3.622514,4.363046,2.999609,4.036991,4.280287,3.011157,2.543877,5.622671,3.869046,6.398634,3.077909,
                3.046906,3.260973,3.423307,1.982473,3.951773,4.983422,3.845991,3.19098,4.365358,4.380781,5.209749,
                3.855872,2.140451,4.565162,3.750386,1.865363,6.166923,3.518735,4.549154,2.997519,3.979626,5.035169,
                4.865504,4.143492,1.922294,5.25331,3.935486,4.244863,4.253498,4.534098,2.844232,5.231128,5.692638,
                2.829378,3.771635,4.276224,3.642153,2.622141,3.96,3.524812,3.171434,3.533439,3.949341,3.032383,4.427482,
                3.023218,4.055793,3.003948);

        List<Integer> final_cast = new ArrayList<>();
        for(int x = 0; x < double_list.size(); x++){
            final_cast.add(double_list.get(x).intValue());
        }
        tactics.put(1, final_cast);

        new Controller(100, tactics);
    }
}