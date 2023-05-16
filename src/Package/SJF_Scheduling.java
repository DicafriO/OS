package Package;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SJF_Scheduling extends RootScheduling {
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
        //할당된 작업이나 TimeSlice가 끝날때 마다 해당 프로세스를 삭제 후 다시 삽입하기 때문에 processes 리스트가 없을때까지 반복함.
        while (!processes.isEmpty()) {
        	// 프로세스들이 기다리고 있는 ReadyQueue 선언함.
            List<ProcessInfo> readyQueue = new ArrayList();
            // Ready Queue에 process들을 정렬된 상태로 차례대로 삽입한다.(presentTime을 기준으로 도착을 한 프로세스들만 넣어준다.)
            for (ProcessInfo process : processes) {
                if (process.getProcessArriveTime() <= presentTime) {
                	readyQueue.add(process);
                }
            }
            // Ready Queue에서 다음에 사용할 프로세스를 정하기 위하여 남은 실행시간이 적게 남은 순으로 정렬시킴.
            Collections.sort(readyQueue, (Object o1, Object o2) -> {
                if (((ProcessInfo) o1).getProcessBurstTime() < ((ProcessInfo) o2).getProcessBurstTime()) {
                    return -1;
                }
                else if (((ProcessInfo) o1).getProcessBurstTime() == ((ProcessInfo) o2).getProcessBurstTime()) {
                    return 0;
                }
                else {
                    return 1;
                }
            });
            // Ready Queue에서 첫 번째 프로세스를 가져옴.
            ProcessInfo process = readyQueue.get(0);
            // 간트차트 리스트에 가져온 프로세스를 가져와서 삽입함.
            this.getGanttChartLists().add(new GanttChart(process.getProcessPid(), presentTime, presentTime + process.getProcessBurstTime(), process.getProcessColor()));
            // 현재 시간을 실행시간만큼 더함.
            presentTime += process.getProcessBurstTime();
            // 기존 프로세스의 리스트에서 해당 작업 수행을 완료한 프로세스를 삭제함. 
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getProcessPid().equals(process.getProcessPid())) {
                	processes.remove(i);
                    break;
                }
            }
        }
        // 각각의 프로세스의 대기 시간, 응답 시간, 반환 시간을 계산하여 저장한다.
        for (ProcessInfo process : this.getProcesses()) {
        	process.setProcessWaitingTime(this.getGanttChartList(process).getProcessStart() - process.getProcessArriveTime());
        	process.setProcessResponseTime(this.getGanttChartList(process).getProcessStart() - process.getProcessArriveTime());
        	process.setProcessTurnAroundTime(process.getProcessBurstTime() + process.getProcessWaitingTime());
        }
    }
}
