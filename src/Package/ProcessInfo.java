package Package;

import java.awt.Color;

public class ProcessInfo {
	private String pid; 				// 프로세스의 이름(프로세스 식별 번호)
	
	private int processBurstTime; 		// 프로세스의 실행 시간
	private int processArriveTime; 		// 프로세스의 도착 시간
	private int processWaitingTime; 	// 프로세스의 대기 시간
	private int processResponseTime;	// 프로세스의 응답 시간
	private int processTurnAroundTime; 	// 프로세스의 반환 시간
	private int processReturnTime; 		// 프로세스의 종료 시간
	
	private int processPriority; 		// 프로세스의 우선 순위
	private Color processColor;			// 프로세스의 색상
	
	// ProcessInfo 생성자
	private ProcessInfo(String pid, int processArriveTime, int processBurstTime, int processPriority, int processWaitingTime, int processResponseTime, int processTurnAroundTime) {
        this.pid = pid;
        this.processBurstTime = processBurstTime;
        this.processArriveTime = processArriveTime;
        this.processWaitingTime = processWaitingTime;
        this.processResponseTime = processResponseTime;
        this.processTurnAroundTime = processTurnAroundTime;
        this.processPriority = processPriority;
        this.processColor = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
    }
	
	//ProcessInfo 생성자(프로세스의 도착 시간,프로세스의 실행 시간,프로세스의 우선순위만 필요한 경우)
	public ProcessInfo(String pid, int processArriveTime, int processBurstTime, int processPriority) {
        this(pid, processArriveTime, processBurstTime, processPriority, 0, 0, 0);
    }
	
	// 프로세스의 이름(프로세스 식별 번호)을 반환하는 메소드
	public String getProcessPid() {
		return pid;
	}

	// 프로세스의 실행 시간을 반환하는 메소드
	public int getProcessBurstTime() {
		return processBurstTime;
	}
	
	// 프로세스의 도착 시간을 반환하는 메소드
	public int getProcessArriveTime() {
		return processArriveTime;
	}
	
	// 프로세스의 대기 시간을 반환하는 메소드
	public int getProcessWaitingTime() {
		return processWaitingTime;
	}
	
	// 프로세스의 응답 시간을 반환하는 메소드
	public int getProcessResponseTime() {
		return processResponseTime;
	}
	
	// 프로세스의 반환 시간을 반환하는 메소드
	public int getProcessTurnAroundTime() {
		return processTurnAroundTime;
	}
	
	// 프로세스의 종료 시간을 반환하는 메소드
	public int getProcessReturnTime() {
		return processReturnTime;
	}
	
	// 프로세스의 우선 순위를 반환하는 메소드
	public int getProcessPriority() {
		return processPriority;
	}	
	
	// 프로세스의 색상을 반환하는 메소드
	public Color getProcessColor() {
    	return this.processColor;
    }	

	//HRN 스케쥴링에서 응답비율을 계산하는 메소드
	public double getResponseRatio() {
    	return (((double)this.getProcessWaitingTime()+(double)this.getProcessBurstTime())/(double)this.getProcessBurstTime());
    }
	
	// 프로세스의 이름(프로세스의 식별 번호)을 설정하는 메소드
	public void setProcessPid(String pid) {
		this.pid = pid;
	}

	// 프로세스의 실행 시간을 설정하는 메소드
	public void setProcessBurstTime(int processBurstTime) {
		this.processBurstTime = processBurstTime;
	}	
	
	// 프로세스의 도착 시간을 설정하는 메소드
	public void setProcessArriveTime(int processArriveTime) {
		this.processArriveTime = processArriveTime;
	}

	// 프로세스의 대기 시간을 설정하는 메소드
	public void setProcessWaitingTime(int processWaitingTime) {
		this.processWaitingTime = processWaitingTime;
	}	

	// 프로세스의 응답 시간을 설정하는 메소드
	public void setProcessResponseTime(int processResponseTime) {
		this.processResponseTime = processResponseTime;
	}

	// 프로세스의 반환 시간을 설정하는 메소드
	public void setProcessTurnAroundTime(int processTurnAroundTime) {
		this.processTurnAroundTime = processTurnAroundTime;
	}	
	
	// 프로세스의 종료 시간을 설정하는 메소드
	public void setProcessReturnTime(int processReturnTime) {
		this.processReturnTime = processReturnTime;
	}

	// 프로세스의 우선 순위를 설정하는 메소드
	public void setProcessPriority(int processPriority) {
		this.processPriority = processPriority;
	}
	
	// 프로세스의 색상을 설정하는 메소드
	public void setProcessColor(Color processColor) {
    	this.processColor = processColor;
    }
}
