package Com.Agent.UI;

import Com.Agent.App;
import Com.Agent.MyBCIMethod;
import Com.Entity.DataSet;
import Com.Entity.MethodInstr;
import Com.Entity.Methodvalue;
import Com.Util.LogFormatter;
import Com.Util.MultiColumnPrinter;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.*;
import java.util.*;

public class PrintThread extends Thread{

    menuState state = menuState.METHOD;
    int indexCount = 0;
    SignalHandler signalHandler = new SignalHandler() {
        @Override
        public void handle(Signal sig) {
            if(state != menuState.MENU)
                state = menuState.MENU;
            else{
                System.exit(0);
            }
        }
    };

    enum menuState{
        METHOD,SUMMARY,EVENT,PERFORMACE,CLASS,MENU,SEARCHING
    }

    private String indexing(String name, List<String>index){
        for(String ind: index)
        {
            if (name.contains(ind.toLowerCase()))
            {
                name = name.replace(ind, LogFormatter.ANSI_BLUE+ind+LogFormatter.ANSI_WHITE);
                return name;
            }
        }
        return "-1";
    }
    private void printindexing(String name, List<String>index)
    {
        if (index.size() != 0)
        {
            name = indexing(name ,index);
            if(!name.equals("-1")){
                System.out.println(name);
                indexCount +=1;
            }
            return;
        }
        System.out.println(name);
        indexCount +=1;
    }

    private DataSet findDataSet(MethodInstr method)
    {
        String name = method.getPackageName()+method.getClassName();
        if(App.taskRepository.getClass(name).isPresent()) {
            return App.taskRepository.getClass(name).get();
        }
        return null;
    }

    private Methodvalue findMethod(DataSet dataSet, MethodInstr method)
    {
        for(Methodvalue methodvalue: dataSet.getMethodvalues())
        {
            if(methodvalue.getName().equals(method.getMethodName()))
            {
                return methodvalue;
            }
        }
        return null;
    }
    @Override
    public void run() {


        Signal.handle(new Signal("INT"),signalHandler);
        Scanner sc = new Scanner(System.in);
        List<String> index = new ArrayList<>();
        int printdepth = 20;
        String indexing = "";
        int sort = 2;

        while (true) {
            if (state == menuState.METHOD)
            {


                System.out.println("Sorting by second");
                int col = 4;
                int gap = 10;
                MultiColumnPrinter printer = new MultiColumnPrinter(col, gap ,"*",0,false) {
                    @Override
                    public void doPrint(String str) {
                        System.out.print(str);
                    }

                    @Override
                    public void doPrintln(String str) {
                        System.out.println("");
                    }
                };

                String[] titleRow = new String[col];
                titleRow[0] = "Method";
                titleRow[1] = "Call";
                titleRow[2] = "Average second(ms)";
                titleRow[3] = "TotalTime(ms)";
                printer.addTitle(titleRow);
//                sorting by CallTime
                Map<String, MethodInstr> methodInstrList = MyBCIMethod.getMethodInstrList();

                List<String> keyList = new ArrayList<>(methodInstrList.keySet());

                if (sort == 1)
                {
                    keyList.sort((Comparator.comparing(o -> methodInstrList.get(o).getCalls())).reversed());
                }
                else if (sort == 2)
                {
                    keyList.sort((Comparator.comparing(o -> methodInstrList.get(o).getSecond())).reversed());
                }
                else
                {
                    keyList.sort((Comparator.comparing(o -> methodInstrList.get(o).getTotalTime())).reversed());
                }

//                 0 show all
                if (printdepth !=0) {
                    if (keyList.size() > printdepth) {
                        keyList = new ArrayList<>(keyList.subList(0, printdepth));
                    }
                }

                for (String key : keyList) {
                    String[] row = new String[col];
                    MethodInstr methodInstr = methodInstrList.get(key);
                    String name = methodInstr.getPackageName()+methodInstr.getClassName()+"/"+methodInstr.getMethodName();
                    if(!name.contains(indexing) && !indexing.equals("0"))
                        continue;
                    row[0] = name;
                    row[1] = String.valueOf(methodInstr.getCalls());
                    row[2] = methodInstr.getSecond()+"ms";
                    row[3] = methodInstr.getTotalTime()+"ms";

                    printer.add(row);
                }

                printer.print();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(state ==menuState.SEARCHING)
            {
                Map<String, MethodInstr> methodInstrList = MyBCIMethod.getMethodInstrList();
                System.out.println(index);
                System.out.println("=================================");
                for(Map.Entry<String, MethodInstr> method: methodInstrList.entrySet())
                {
                    printindexing(method.getKey(), index);
                }
                System.out.println("=================================");

                System.out.println("\r\nInput Method Name\r\n");
                String name = sc.nextLine();

                if(name.equals("exit"))
                {
                    state = menuState.MENU;
                }
                else if(name.contains("grep"))
                {
                    index = List.of((name.split(" ")));
                    index = index.subList(1, index.size());
                }
                else if(name.contains("tree"))
                {
                    
                }
                else if (methodInstrList.get(name) != null)
                {
                    MethodInstr method =  methodInstrList.get(name);
                    DataSet dataSet = findDataSet(method);
                    if(dataSet !=null)
                    {
                        Methodvalue methodvalue = findMethod(dataSet, method);
                        if (methodvalue != null) {
                            methodvalue.printInsn();
                        }
                    }


                    System.out.println("=================================");
                    StackTraceElement[] stack = method.getStacks();
                    for(int i=0; i<stack.length; i++)
                    {
                        if(i==0)
                            continue;

                        System.out.println("["+i+"] "+stack[i]);
                    }
                    System.out.println("=================================");
                    sc.nextLine();
                }
            }
            else if (state == menuState.CLASS)
            {
                for( String name : App.taskRepository.classList())
                {
                    printindexing(name, index);
                }
                System.out.println("Count : "+indexCount);
                indexCount = 0;

                System.out.println("\r\nClassName List(exit go to menu)\r\n");
                String className = sc.nextLine();

                if (className.contains("grep"))
                {
                    index = List.of((className.split(" ")));
                    index = index.subList(1, index.size());
                }
                else if (App.taskRepository.getClass(className).isPresent())
                {
                    App.taskRepository.getClass(className).get().printDataset();
                    System.out.println("\r\nContinue to Enter");
                    sc.nextLine();
                    index = new ArrayList<>();

                } else if(className.equals("exit"))
                {
                    state = menuState.MENU;
                }
            }
            else if (state == menuState.EVENT)
            {
                try(BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/agentLog.txt"))){
                    String line;
                    while ((line = reader.readLine()) != null){
                        printindexing(line, index);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
                System.out.println("Count : "+indexCount);
                indexCount = 0;

                System.out.println("\r\nLog List(exit go to menu)");
                String className = sc.nextLine();

                if (className.contains("grep"))
                {
                    index = List.of((className.split(" ")));
                    index = index.subList(1, index.size());
                }
                else if(className.equals("exit"))
                {
                    state = menuState.MENU;
                }
            }
            else if (state == menuState.SUMMARY)
            {
//                OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
                MemoryUsage heapMemoryUsage = memBean.getHeapMemoryUsage();
                MemoryUsage nonheapMemoryUsage = memBean.getNonHeapMemoryUsage();

                long[] allThreadIds = threadMXBean.getAllThreadIds();
                long nano =0;
                for (long id : allThreadIds) {
                    nano += threadMXBean.getThreadCpuTime(id);
                }

                System.out.println("Java Virtual Machine : " + System.getProperty("java.vm.name"));
                System.out.println("Vendor : " + System.getProperty("java.vm.specification.vendor"));
                System.out.println("PID : "+ProcessHandle.current().pid());
                System.out.println("Runtime : "+new Date(runtimeMXBean.getStartTime()));
                System.out.println("Uptime : "+runtimeMXBean.getUptime()/1000+" s");
                System.out.println("Total CPU time : "+nano/1E6);
                System.out.println("Working dir : "+System.getProperty("user.dir"));

                System.out.println("Agent log :" +System.getProperty("user.dir")+"/agentLog.txt");
                System.out.println("=====================================================");
                System.out.println("Limit Heap memory "+ heapMemoryUsage.getMax());
                System.out.println("Allocated Heap memory "+ heapMemoryUsage.getCommitted());
                System.out.println("Using Heap memory "+ heapMemoryUsage.getUsed());

                System.out.println("=======================================================");
                System.out.println("Limit NonHeap memory "+ nonheapMemoryUsage.getMax());
                System.out.println("Allocated NonHeap memory "+ nonheapMemoryUsage.getCommitted());
                System.out.println("Using NonHeap memory "+ nonheapMemoryUsage.getUsed());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            else if (state == menuState.PERFORMACE)
            {
                System.out.println("Not Making");
                state = menuState.MENU;
            }
            else if (state == menuState.MENU)
            {
                while(true)
                {
                    index = new ArrayList<>();
                    indexCount = 0;
                    System.out.println("AgentSlve Menu");
                    System.out.println("1. MethodProfiling");
                    System.out.println("2. SearchClssinfo");
                    System.out.println("3. EventLog");
                    System.out.println("4. Searching");
//                    System.out.println("5. JVM Summary");
                    System.out.println("5. JVM Summary");
                    System.out.print("6. Exit\r\n=>");
                    String asn = sc.nextLine();
                    switch (asn){
                        case "1":
                            state = menuState.METHOD;
                            System.out.println("Input Depth(zero to default)");
                            printdepth = sc.nextInt();
                            System.out.println("Indexing(zero to default)");
                            indexing = sc.next();
                            System.out.println("Sorting Method(default TotalTime)");
                            System.out.println("1 : Call \t 2 : Average Second \t 3 : Total Time");
                            sort = sc.nextInt();
                            break;
                        case "2":
                            state = menuState.CLASS;
                            break;
                        case "3":
                            state = menuState.EVENT;
                            break;
                        case "4":
                            state = menuState.SEARCHING;
                            break;
                        case "5":
                            state = menuState.SUMMARY;
                            break;
                        case "6":
                            System.exit(0);
                        default:
                    }
                    break;
                }
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

    }
}