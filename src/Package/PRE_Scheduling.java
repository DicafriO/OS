package Package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PRE_Scheduling extends RootScheduling {
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
        List<ProcessInfo> remainingProcesses = Copy.Copylist(this.getProcesses());
        // presentTime은 현재 Scheduling 중에 흐르는 시간을 나타내며 처음 시간은 첫번째 프로세스가 도착한 시간으로 설정함.
        int presentTime = remainingProcesses.get(0).getProcessArriveTime();
        //할당된 작업이나 TimeSlice가 끝날때 마다 해당 프로세스를 삭제 후 다시 삽입하기 때문에 processes 리스트가 없을때까지 반복함.
        while (!remainingProcesses.isEmpty()) {
        	// 프로세스들이 기다리고 있는 ReadyQueue 선언함.
            List<ProcessInfo> readyQueue = new ArrayList();
            // Ready Queue에 process들을 정렬된 상태로 차례대로 삽입한다.(presentTime을 기준으로 도착을 한 프로세스들만 넣어준다.)
            for (ProcessInfo process : remainingProcesses) {
            	if (process.getProcessArriveTime() <= presentTime) {
            		readyQueue.add(process);
                }
            }
            // Ready Queue에서 다음에 사용할 프로세스를 정하기 위하여 우선순위 순으로 정렬시킴.
            Collections.sort(readyQueue, (Object o1, Object o2) -> {
                if (((ProcessInfo) o1).getProcessPriority() < ((ProcessInfo) o2).getProcessPriority()) {
                    return -1;
                }
                else if (((ProcessInfo) o1).getProcessPriority() == ((ProcessInfo) o2).getProcessPriority()) {
                    return 0;
                }
                else {
                    return 1;
                }
            });
            // Ready Queue에서 첫 번째 프로세스를 가져옴.
            ProcessInfo process = readyQueue.get(0);
            // 간트차트 리스트에 가져온 프로세스를 가져와서 삽입함. 현재 흐르는 시간은 1씩 더해줌.(선점형이라 1초씩 비교)
            this.getGanttChartLists().add(new GanttChart(process.getProcessPid(), presentTime, ++presentTime, process.getProcessColor()));
            // 현재 흐르는 시간이 1초가 지났기에 남은 실행시간은 1초 빼줌.
            process.setProcessBurstTime(process.getProcessBurstTime() - 1);
            // 기존 프로세스의 리스트에서 해당 작업 수행을 완료한 프로세스를 삭제함. 
            if (process.getProcessBurstTime() == 0) {
                for (int i = 0; i < remainingProcesses.size(); i++) {
                    if (remainingProcesses.get(i).getProcessPid().equals(process.getProcessPid())) {
                    	remainingProcesses.remove(i);
                        break;
                    }
                }
            }
        }
        // 1초 단위로 표현된 간트 차트를 통합하여 표현하기 위해 간트 차트 리스트의 마지막 인덱스부터
        // 이전의 프로세스와 비교하여 PID가 같은 경우 finish 시간을 통합하였음. 
        for (int i = this.getGanttChartLists().size() - 1; i > 0; i--) {
            List<GanttChart> copyLists = this.getGanttChartLists();
            if (copyLists.get(i).getPid().equals(copyLists.get(i-1).getPid())) {
            	copyLists.get(i - 1).setProcessFinish(copyLists.get(i).getProcessFinish());
            	copyLists.remove(i);
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
