package Package;

import java.util.ArrayList;
import java.util.List;

public abstract class RootScheduling {
    private final List<GanttChart> ganttChartLists;		// 간트차트의 리스트
    private final List<ProcessInfo> OriginalProcesses;	// 프로세스들의 리스트
    private int processTimeQuantum;						// 프로세스의 시간할당량
    // RootScheduling 생성자
    public RootScheduling() {
    	this.ganttChartLists = new ArrayList();
    	this.OriginalProcesses = new ArrayList();
    	processTimeQuantum = 1;
    }
    //각각의 스케쥴링에 따라 그에 맞는 스케쥴링을 구현하는 추상 메소드
    public abstract void scheduling();   
    // 프로세스의 시간할당량을 반환하는 메소드
    public int getProcessTimeQuantum() {
        return processTimeQuantum;
    }
    // Processes 리스트에 프로세스를 추가하는 메소드
    public void addProcess(ProcessInfo process) {
    	OriginalProcesses.add(process);
    } 
    // 프로세스의 시간할당량을 설정하는 메소드 
    public void setProcessTimeQuantum(int processTimeQuantum) {
        this.processTimeQuantum = processTimeQuantum;
    }
    // 간트차트 리스트를 반환하는 메소드
    public List<GanttChart> getGanttChartLists() {
        return ganttChartLists;
    } 
    // Process들의 리스트를 반환하는 메소드
    public List<ProcessInfo> getProcesses() {
        return OriginalProcesses;
    }    
    // 프로세스의 평균 반환 시간을 반환하는 메소드
    public double getProcessAverageTurnAroundTime() {
        double TurnAroundAvg = 0.0;
        
        for (ProcessInfo process : OriginalProcesses) {
        	TurnAroundAvg += process.getProcessTurnAroundTime();
        }
        return TurnAroundAvg / OriginalProcesses.size();
    }
    // 프로세스의 평균 대기 시간을 반환하는 메소드
    public double getProcessAverageWaitingTime() {
        double waitingTimeAvg = 0.0;
        
        for (ProcessInfo process : OriginalProcesses) {
        	waitingTimeAvg += process.getProcessWaitingTime();
        } 
        return waitingTimeAvg / OriginalProcesses.size();
    }     
    // 프로세스의 평균 응답 시간을 반환하는 메소드
    public double getProcessResponseTime() {
    	double ResponseTimeAvg = 0.0;
    	for (ProcessInfo process : OriginalProcesses) {
    		ResponseTimeAvg += process.getProcessResponseTime();
    	}
    	return ResponseTimeAvg / OriginalProcesses.size();
    }
    // 간트차트 리스트내 항목에 1대1로 대응하는 프로세스를 반환하는 메소드
    public ProcessInfo getProcess(String ganttChartListElement) {
        for (ProcessInfo process : OriginalProcesses)  {
            if (process.getProcessPid().equals(ganttChartListElement)) {
                return process;
            }
        } 
        return null;
    }
    // 프로세스와 1대1로 대응하는 간트차트 리스트내 항목을 반환하는 메소드
    public GanttChart getGanttChartList(ProcessInfo process) {
        for (GanttChart chartList : ganttChartLists) {
            if (process.getProcessPid().equals(chartList.getPid())) {
                return chartList;
            }
        }
        return null;
    }   
}
