package Package;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RR_Scheduling extends RootScheduling {
    @Override
    public void scheduling() {
    	// 프로세스 리스트를 도착시간을 기준으로 정렬시킴.
        Collections.sort(this.getProcesses(), (Object o1, Object o2) -> {
        	 if (((ProcessInfo) o1).getProcessArriveTime() < ((ProcessInfo) o2).getProcessArriveTime()) {
                 return -1;
             }
             else if (((ProcessInfo) o1).getProcessArriveTime() == ((ProcessInfo) o2).getProcessArriveTime()) {
                 return 0;
             }
             else {
                 return 1;
             }
        });
        // 도착시간대로 정렬된 프로세스의 목록에서 프로세스를 하나씩 가져와서 간트차트 리스트에 추가한다.
        List<ProcessInfo> processes = Copy.Copylist(this.getProcesses());
        // presentTime은 현재 Scheduling 중에 흐르는 시간을 나타내며 처음 시간은 첫번째 프로세스가 도착한 시간으로 설정함.
        int presentTime = processes.get(0).getProcessArriveTime(); 
        // 프로세스의 시간할당량을 설정함.
        int processTimeQuantum = this.getProcessTimeQuantum();
        //할당된 작업이나 TimeSlice가 끝날때 마다 해당 프로세스를 삭제 후 다시 삽입하기 때문에 processes 리스트가 없을때까지 반복함.
        while (!processes.isEmpty()) {
        	// 프로세스 리스트의 첫번째 프로세스를 가져옴
        	ProcessInfo process = processes.get(0);
        	// 한번에 실행하게되는 시간 단위로시간할당량보다 프로세스의 남은 실행시간이 적거나 같으면 남은 실행시간으로,시간할당량보다 프로세스의 남은 실행시간이 많다면 시간할당량으로 초기화한다. 
            int oneBurstTime = (process.getProcessBurstTime() <= processTimeQuantum ? process.getProcessBurstTime() : processTimeQuantum);
            // 간트차트 리스트에 프로세스 리스트의 첫 번째 프로세스를 가져와서 삽입하며 현재 흐르는 시간의 단위는 앞서 정한 실행 단위를 기존 시간에 더하여 종료시간 계산한다.
            this.getGanttChartLists().add(new GanttChart(process.getProcessPid(), presentTime, presentTime + oneBurstTime, process.getProcessColor()));
            // 다음 프로세스의 시작시간을 현재 시간에 한번에 실행하는 시간 단위를 더한 값으로 설정한다.
            presentTime += oneBurstTime;
            // 이번 타임 슬라이스에 실행한 프로세스를 삭제한다.(다시 맨 뒤에 추가할 예정)
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getProcessPid().equals(process.getProcessPid())) {
                 processes.remove(i);
                    break;
                }
            }  
            // 만약 실행시간이 시간할당량보다 많으면 프로세스의 남은 실행시간에서 시간할당량을 빼준다.
            if (process.getProcessBurstTime() > processTimeQuantum) {
            	process.setProcessBurstTime(process.getProcessBurstTime() - processTimeQuantum); 
                for (int i = 0; i < processes.size(); i++) {
                	// 다음 프로세스가 아직 도착을 안했으면 현재 프로세스를 실행시키도록 해준다.
                    if (processes.get(i).getProcessArriveTime() > presentTime) {
                    	processes.add(i, process);
                        break;
                    }
                    else if (i == processes.size()-1) {
                    	processes.add(process);
                        break;
                    }
                }
            }
            // 만약 실행시간이 시간할당량보다 적거나 같으면 프로세스의 남은 실행시간을 0으로 만든다.
            else if(process.getProcessBurstTime() <= processTimeQuantum)
            {
            	process.setProcessBurstTime(process.getProcessBurstTime() - process.getProcessBurstTime());
            }
            // 프로세스 리스트에 실행시간이 남아있는 프로세스는 다시 추가 해준다.
            if (processes.size() == 0 && process.getProcessBurstTime() != 0) {
            	processes.add(process);
            }
        }
        // {PID : 간트차트 리스트의 해당 프로세스의 종료시간}을 Key값과 Value값으로 가지는 Map 객체를 설정함.
        Map ganttChartMap = new HashMap();
        // 이중 반복문을 이용하여 프로세스와 간트차트의 프로세스를 한개씩 비교하면서 대기,응답,반환을 계산함.
        for (ProcessInfo process : this.getProcesses()) {
        	ganttChartMap.clear();   //ganttChartMap을 초기화 시켜준다.(이전에 사용한 정보를 초기화 하기 위하여)
            for (GanttChart copyChartList : this.getGanttChartLists()) {
            	// process와 copyChartList가 서로 가리키는 프로세스가 같은 경우
                if (copyChartList.getPid().equals(process.getProcessPid())) {
                	  // ganttChartMap에 현재 가리키는 프로세스의 정보가 없다면 응답 시간과 대기 시간을 계산하여 설정함.
                	  if (!ganttChartMap.containsKey(copyChartList.getPid())) {  
                        process.setProcessResponseTime(copyChartList.getProcessStart() - process.getProcessArriveTime());
                        process.setProcessWaitingTime(copyChartList.getProcessStart() - process.getProcessArriveTime());        
                     }
                	 // ganttChartMap에 현재 가리키는 프로세스의 정보가 이미 있는 경우
                     else {
                    	// 같은 프로세스들 사이의 중간 대기 시간을 계산하기 위하여 현재 간트차트 리스트의 시작시간에서 이전에 들어왔던 때의 종료시간을 빼준다.
                        int resentWaitingTime = copyChartList.getProcessStart() - (int) ganttChartMap.get(copyChartList.getPid());    
                        // 기존 대기 시간을 업데이트 한다.
                        process.setProcessWaitingTime(+ resentWaitingTime + process.getProcessWaitingTime());
                     }                    
                    // 프로세스의 종료시간을 업데이트한다.
                    ganttChartMap.put(copyChartList.getPid(), copyChartList.getProcessFinish());
                }
            }
            // 마지막으로 프로세스의 반환시간 계산한다.
            process.setProcessTurnAroundTime(process.getProcessBurstTime() + process.getProcessWaitingTime());
        }
    }
}
