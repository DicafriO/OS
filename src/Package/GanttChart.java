package Package;

import java.awt.Color;

public class GanttChart {
	private final String pid;		// 프로세스의 이름
    private final int processStart;		// 각 프로세스의 시작시간
    private int processFinish;			// 각 프로세스의 종료시간
    private Color processColor;		// 각각의 프로세스를 나타내는 색상
    // 생성자
    public GanttChart(String pid, int processStart, int processFinish, Color processcolor) {
        this.pid = pid;
        this.processStart = processStart;
        this.processFinish = processFinish;
        this.processColor = processcolor;
    }
    // 프로세스 식별 번호(Process IDentification) 반환 메소드
    public String getPid() {
		return pid;
	}
    // 시작시간(processStart) 반환 메소드
    public int getProcessStart() {
		return processStart;
	}
    // 종료시간(processFinish) 반환 메소드
	public int getProcessFinish() {
		return processFinish;
	}
	// 각각의 프로세스의 색상(processColor) 반환 메소드
		public Color getProcessColor() {
			return processColor;
		}
	// 종료시간(processFinish) 설정 메소드
	public void setProcessFinish(int processFinish) {
		this.processFinish = processFinish;
	}
	// 각각의 프로세스의 색상(processColor) 설정 메소드
	public void setProcessColor(Color processColor) {
		this.processColor = processColor;
	}
}
