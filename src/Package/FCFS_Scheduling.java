package Package;

import java.util.List;
import java.util.Collections;

public class FCFS_Scheduling extends RootScheduling {
	@Override
    public void scheduling() {
    	 List<GanttChart> ganttChartLists = this.getGanttChartLists();
    	// 프로세스의 리스트를 도착시간을 기준으로 정렬 시킨다.(FCFS는 프로세스의 도착 순서대로 작업을 할당해주기 때문이다.)
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
        for (ProcessInfo process : this.getProcesses()) {
        	// 간트 차트 리스트가 비어있을 경우, 처음 프로세스를 리스트에 삽입한다.
            if (ganttChartLists.isEmpty()) {
            	ganttChartLists.add(new GanttChart(process.getProcessPid(), process.getProcessArriveTime(),
            						process.getProcessArriveTime() + process.getProcessBurstTime(), process.getProcessColor()));
            }
            // 간트 차트 리스트가 비어있지 않을 경우, 계속해서 프로세스를 리스트에 삽입한다.(간트 차트를 그리기 위해 이전 프로세스의 작업이 끝나는 시점을 알아야한다.)
            else {
            	GanttChart copyChartList = ganttChartLists.get(ganttChartLists.size() - 1);
                ganttChartLists.add(new GanttChart(process.getProcessPid(), copyChartList.getProcessFinish(),
                		copyChartList.getProcessFinish() + process.getProcessBurstTime(),process.getProcessColor()));
            }
        }  
        // 각각의 프로세스의 대기 시간, 응답 시간, 반환 시간을 계산하여 저장한다.
        for (ProcessInfo process : this.getProcesses()) {
        	process.setProcessWaitingTime(this.getGanttChartList(process).getProcessStart() - process.getProcessArriveTime());
        	process.setProcessResponseTime(this.getGanttChartList(process).getProcessStart() - process.getProcessArriveTime());
        	process.setProcessTurnAroundTime( process.getProcessBurstTime()+ process.getProcessWaitingTime());
        }
    }
}
