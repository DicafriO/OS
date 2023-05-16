package Package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.*;
import java.util.List;
import java.util.Scanner;

public class Main extends JFrame {
	private int TimeSlice = 0;	//TimeSliec를 저장하는 변수
	String SchedulerChoice[] = { "FCFS", "SJF","HRN","RR", "SRT", "None-preemptive Priority", "Preemptive Priority" }; // 콤보 박스안에 들어갈 스케줄러 종류 문자열
	
	private JScrollPane processScrollPane;		// processTable의 스크롤
	private JScrollPane schedulingResultPane;	// SchedulingResultTable의 스크롤
	private JScrollPane ganttChartPane;			// GanttChart의 스크롤
	
	private DefaultTableModel processModel;				// processTable의 값을 당담하는 processModel
	private DefaultTableModel schedulingResultModel;	// schedulingResultTable의 값을 당담하는 schedulingResultModel
	
	private JTable processTable;			// 프로세스의 정보를 받아오는 JTable
	private JTable schedulingResultTable;	// 프로세스의 스케쥴링 결과값을 출력하는 JTable
	
	private JButton ProgramEndBtn;			// 프로그램 종료 버튼
	private JButton schedulingResultBtn;	// 스케쥴링 결과 확인 버튼	
	private JButton schedulingRetryBtn;		// 스케쥴링 결과 출력 후에 다시 스케쥴링하기위한 버튼
	
	private JPanel rootFrame;	// 메인 프레임을 구성하는 JPanel
	private GanttChartPanel ganttChart;	// 간트 차트를 그리는 GanttChartPanel 메소드 객체
	private JComboBox<String> comboBox;	// 스케줄링 방법을 선택 할 수 있는 콤보 박스 
	private String blanks[] = { "", "", "", "" };
	// processTable, schedulingResultTable에서 사용하는 빈칸으로 구성된 문자열 배열
	private int ganttChartX = 25;	// 차트 그릴때 이용할 좌표
	//TimeSlice를 설정하는 메소드
	public void setTimeSlice(int x)
	{
		this.TimeSlice=x;
	}
	public Main() {
		File f = new File("./ProcessInfo.txt");
		setTitle("OS - 7 Types Of CPU Scheduler(20194064 이광혁)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String voidMent[] = { "Process ID", "Arriaval Time", "Burst Time", "Priority" };	//테이블의 첫 행에 들어갈 요소들
		processModel = new DefaultTableModel(voidMent, 0);	// voidMent를 첫 행으로 가지는 DefaultTableModel인 processModel에 할당함.
		// 파일 입출력을 통하여 프로세스의 정보들을 받아옴.
		try {
			 Scanner scanner = new Scanner(f);
			 String str = scanner.nextLine();
			 int FileN = Integer.parseInt(str);	//process의 수를 정장하는 변수
			 
			 for(int i=0;i<FileN;i++)
				 processModel.addRow(blanks);	//process의 숫자만큼 processModel에 빈 행을 추가함
			 
			 for(int i=0;i<FileN;i++)
			 {
				 str = scanner.next();
				 String Opid = str;
				// i번째 행의 0번째 열(프로세스 ID)의 값을 Opid값으로 저장한다.
				 processModel.setValueAt(Opid,i,0);
				 str = scanner.next();
				 int Oarrive = Integer.parseInt(str);
				// i번째 행의 1번째 열(도착시간)의 값을 정수형 Oarrive값으로 저장한다.
				 processModel.setValueAt(Oarrive,i,1);
				 str = scanner.next();
				 int Oburst = Integer.parseInt(str);
				// i번째 행의 2번째 열(실행시간)의 값을 정수형 Oburst값으로 저장한다.
				 processModel.setValueAt(Oburst,i,2);
				 str = scanner.next();
				 int Opriority = Integer.parseInt(str);
				 // i번째 행의 3번째 열(우선순위)의 값을 정수형 Opriority값으로 저장한다.
				 processModel.setValueAt(Opriority,i,3);
			 }
			 str =scanner.next();
			 setTimeSlice(Integer.parseInt(str));
			}catch (FileNotFoundException e) {
				 e.printStackTrace();
			}
		processTable = new JTable(processModel);	// processModel의 정보를 가진 JTable을 processTable 할당함. 
		processTable.setFillsViewportHeight(true);
		processTable.setEnabled(false);
		processScrollPane = new JScrollPane(processTable);	// processTable의 스크롤
		processScrollPane.setBounds(10, 15, 540, 250); 

		comboBox = new JComboBox<>();			
		for (int i = 0; i < SchedulerChoice.length; i++)
			comboBox.addItem(SchedulerChoice[i]);	//반복문을 이용하여 Combo에 SchedulerChoice 삽입함.
		comboBox.setBounds(605, 15, 150, 30);

		ganttChart = new GanttChartPanel();		//간트 차트 정보를 갖는 패널
		ganttChart.setBackground(Color.WHITE);	
		ganttChartPane = new JScrollPane(ganttChart);	// ganttChart 스크롤
		ganttChartPane.setBounds(10, 320, 1375, 100);

		schedulingResultModel = new DefaultTableModel(		// 결과값 테이블 첫 행 문자열을 설정한 DefaultTableModel 할당함.
				new String[] { "PID", "대기시간", "평균 대기시간", "응답시간", "평균 응답시간", "반환시간", "평균 반환시간" }, 0);
		schedulingResultTable = new JTable(schedulingResultModel);		// schedulingResultModel의 데이터를 가진 JTable을 할당함.
		schedulingResultTable.setFillsViewportHeight(true);				// schedulingResultTable 창 채움.
		schedulingResultPane = new JScrollPane(schedulingResultTable);	// resulTable의 스크롤
		schedulingResultPane.setBounds(815, 15, 550, 250);
		
		schedulingRetryBtn = new JButton("Retry");					// 결과를 확인 후 다른 스케줄링을 선택하기 위한 버튼
		schedulingRetryBtn.setBounds(605, 190, 95, 25);	
		schedulingRetryBtn.setEnabled(false);						// 버튼의 boolean값을 false로 하여 클릭이 안되게함.
		schedulingRetryBtn.setBackground(new Color(247, 247, 229));	
		
	
		schedulingResultBtn = new JButton("Checking the Results");	//스케줄링의 결과를 확인하는 버튼
		schedulingResultBtn.setBounds(605, 150, 165, 25);				
		schedulingResultBtn.setBackground(new Color(247, 247, 229));	
		
		
		ProgramEndBtn = new JButton("Exit");					// 종료 버튼
		ProgramEndBtn.setBounds(605, 225, 95, 25);			
		ProgramEndBtn.setEnabled(false);						//버튼의 boolean값 false로 하여 클릭이 안되게함.
		ProgramEndBtn.setBackground(new Color(248, 226, 226));	
		
		
		schedulingRetryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	// 버튼을 눌렀을때
				for (int i = schedulingResultModel.getRowCount() - 1; i > -1; i--) {
					schedulingResultModel.removeRow(i); // 모든 행을 삭제함.
				}
				comboBox.setEnabled(true);				// comboBox 버튼을 활성화 시킴.
				schedulingResultBtn.setEnabled(true);	// 결과 버튼을 활성화 시킴.		
				schedulingRetryBtn.setEnabled(false);	// 다시하기 버튼은 비활성화 시킴.
			}
		});

		ProgramEndBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	// 버튼을 눌렀을때
				System.exit(0);								// 프로그램을 종료함.
			}
		});

		schedulingResultBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						// 버튼을 눌렀을때
				ganttChartX = 20;												// 차트의 X 좌표를 20으로 설정함. 
				String selectedString = (String) comboBox.getSelectedItem();	// comboBox에서 선택된 문자열을 selectedString에 저장함.
				RootScheduling selectedScheduling;		// 스케줄링 객체를 생성함.
				
				rootFrame.add(schedulingRetryBtn);		// 버튼들을 부착함.
				rootFrame.add(schedulingResultPane);	
				rootFrame.add(ProgramEndBtn);			
				comboBox.setEnabled(false);				//결과 확인 버튼과 comboBox의 boolean값을 
				schedulingResultBtn.setEnabled(false);	//false로 설정하여 선택이 안되게 하여 실행 도중에 값을 못 바꾸게 하였음.	
				ProgramEndBtn.setEnabled(true);			// 다시 하기 버튼과 종료 버튼은 선택할 수 있도록 boolean값을 true값으로 설정함.
				schedulingRetryBtn.setEnabled(true);	
				
				for (int i = 0; i <= processModel.getRowCount(); i++) {	
					schedulingResultModel.addRow(blanks);	// schedulingResultTable에 행 추가(Table의 행의 개수 + 1개 만큼)
				}

				switch (selectedString) {		// 선택된 스케줄링 방법의 종류에 따라 각각의 스케줄링 객체를 생성함.
				case "FCFS":					
					selectedScheduling = new FCFS_Scheduling();	
					break;						
				case "SJF":						
					selectedScheduling = new SJF_Scheduling();		
					break;						
				case "HRN":						
					selectedScheduling = new HRN_Scheduling();		
					break;						
				case "RR":						
					selectedScheduling = new RR_Scheduling();		
					selectedScheduling.setProcessTimeQuantum(TimeSlice);//RootScheduling의 processTimeQuantum를 TimeSlice로 설정함.
					break;						
				case "SRT":						
					selectedScheduling = new SRT_Scheduling();	
					selectedScheduling.setProcessTimeQuantum(TimeSlice);//RootScheduling의 processTimeQuantum를 TimeSlice로 설정함.
					break;							
				case "None-preemptive Priority":			
					selectedScheduling = new NPRE_Scheduling();	
					break;						
				case "Preemptive Priority":				
					selectedScheduling = new PRE_Scheduling();		
					break;						
				default:						
					return;
				}
				
				for (int i = 0; i < processModel.getRowCount(); i++) {
					// i번째 행의 0번째 열(프로세스 ID)의 값을 processPid에 저장한다
					String processPid = (String) processModel.getValueAt(i, 0);	
					// i번째 행의 1번째 열(도착시간)의 값을 processArrive에 저장한다
					int processArrive = (int)processModel.getValueAt(i, 1);	
					// i번째 행의 2번째 열(실행시간)의 값을 processBurst에 저장한다.
					int processBurst = (int)processModel.getValueAt(i, 2);
					// i번째 행의 3번째 열(우선순위)의 값을 processPriority에 저장한다.
					int processPriority = (int) processModel.getValueAt(i, 3);
					// 값을 전달하기 위하여 위에서 저장한 데이터를 ProcessInfo 생성자에 전달해 새로운 객체를 만들어 RootScheduling의 addProcess 메소드에 전달한다.
					selectedScheduling.addProcess(new ProcessInfo(processPid, processArrive, processBurst, processPriority));
				}
				
				selectedScheduling.scheduling();// 각각의 스케줄링에 맞는 추상 메소드 scheduling()을 실행시킴.

				for (int i = 0; i < processModel.getRowCount(); i++) {
					// i번째 행의 0번째 열(프로세스 ID)의 값을 pid에 저장함.
					String pid = (String) processModel.getValueAt(i, 0);
					// pid에 해당하는 proccess 정보를 받아온다.
					ProcessInfo CopyProcess = selectedScheduling.getProcess(pid);
					// schedulingResultTable의 i행의 0번째 열을 pid(프로세스 ID)로 설정함.
					schedulingResultModel.setValueAt(pid, i, 0);
					// schedulingResultTable의 i행의 1번째 열에 pid에 해당하는 대기시간으로 설정함.
					schedulingResultModel.setValueAt(CopyProcess.getProcessWaitingTime(), i, 1);
					// schedulingResultTable의 i행의 3번째 열에 pid에 해당하는 응답시간으로 설정함.
					schedulingResultModel.setValueAt(CopyProcess.getProcessResponseTime(), i, 3);
					// schedulingResultTable i행의 5번째 열에 pid에 해당하는 반환시간으로 설정함.
					schedulingResultModel.setValueAt(CopyProcess.getProcessTurnAroundTime(), i, 5);
				}	
				// 프로세스들의 평균대기 시간을 schedulingResultTable 마지막 행, 2열에 저장함.
				schedulingResultModel.setValueAt(selectedScheduling.getProcessAverageWaitingTime(), processModel.getRowCount(), 2);
				// 프로세스들의 평균응답 시간을 schedulingResultTable 마지막 행, 4열에 저장함.
				schedulingResultModel.setValueAt(selectedScheduling.getProcessResponseTime(), processModel.getRowCount(), 4);
				// 프로세스들의 평균반환 시간을 schedulingResultTable 마지막 행, 6열에 저장함.
				schedulingResultModel.setValueAt(selectedScheduling.getProcessAverageTurnAroundTime(), processModel.getRowCount(), 6);
				// 간트 차트의 chartlist 정보를 저장함.
				ganttChart.setProcessTimeQuantum(selectedScheduling.getGanttChartLists());
			}
		});
		rootFrame = new JPanel(null);
		rootFrame.add(comboBox);				// comboBox 추가함.
		rootFrame.add(ganttChartPane);			// ganttChartPane 간트차트를 그릴 스크롤(GanttChart)을 추가함.
		rootFrame.add(processScrollPane);		// 프로세스 정보를 갖는 스크롤을 추가함.
		
		rootFrame.add(schedulingResultBtn);		// schedulingResultBtn(결과보기 버튼) 추가
		rootFrame.setBackground(new Color(204, 204, 153));		
		setBounds(75, 100, 1400, 500);		
		setVisible(true);					// 프레임이 항상 보이게 boolean값을 true로 설정함.
		setResizable(false);				// 확장하는것을 비활성화함.
		add(rootFrame);						// 모든 컨포넌트를 가진 rootFrame을 추가함.
	}

	class GanttChartPanel extends JPanel {	
		
		private List<GanttChart> chartList;		// ChartList 정보를 가지는 리스트 list 생성

		public void paintComponent(Graphics g) {	// 오버라이딩을 함.	
			super.paintComponent(g);				// 패널 내의 이전에 그려진 그래픽들을 지우기 위해 호출함.
			if (chartList != null) {							
				for (int i = 0; i < chartList.size(); i++) {	
					GanttChart copyChartList = chartList.get(i);	// i번째 인덱스의 charList 정보를 copyChartList에 저장함.
					int ganttChartY = 40;			// ganttChart의 y좌표
					int minWidth = ganttChartX;		// 작은 ganttChart의 x좌표
					int minHeight = 5;				// 작은 ganttChart의 y좌표

					g.setColor(copyChartList.getProcessColor());	// 각각의 프로세스의 색깔로 설정함.
					g.drawRect(minWidth, minHeight, 20, 20);		// 20 * 20 크기의 정사각형을 그린다.
					g.fillRect(minWidth, minHeight, 20, 20);		// 정사각형을 같은 색으로 채운다.
					g.setColor(Color.black);						// 색상을 검정색으로 설정함.
					g.drawString(copyChartList.getPid(), minWidth + 3, minHeight + 15); 	// 프로세스의 id를 x좌표 + 3, height + 15에 그린다.(정사각형 가운데에 PID 출력)
					g.drawString(Integer.toString(copyChartList.getProcessStart()), ganttChartX - 5, ganttChartY + 45);	// 간트차트의 시작점과 시작 시간을 출력함.
					g.setColor(copyChartList.getProcessColor());	// 각각의 프로세스 색상으로 설정함.
					g.drawRect(ganttChartX, ganttChartY, (copyChartList.getProcessFinish() - copyChartList.getProcessStart()) * 20, 25);	// 프로세스 실행이 끝날때마다 가로 20크기의 사각형을 그린다.
					g.fillRect(ganttChartX, ganttChartY, (copyChartList.getProcessFinish() - copyChartList.getProcessStart()) * 20, 25);
					g.setColor(Color.black);
					g.drawString(copyChartList.getPid(), ganttChartX + 5, ganttChartY + 18);					// 사각형 안에 PID를 출력함.
					ganttChartX += (copyChartList.getProcessFinish() - copyChartList.getProcessStart()) * 20;	// X좌표에 이전에 그린 사각형의 좌표 크기만큼을 더해서 저장함.
					if (i == chartList.size() - 1)	// 마지막 프로세스의 경우
                    {
                        g.drawString(Integer.toString(copyChartList.getProcessFinish()), ganttChartX , ganttChartY + 45);	// 끝나는 시간을 출력함.
                    } 
				}
			}
		}
		public void setProcessTimeQuantum(List<GanttChart> copyList) {
			this.chartList = copyList;	// 차트 리스트의 정보를 받아와서
			repaint();					// 한번 더 호출함.
		}
	}
	public static void main(String[] args) {
		new Main();
	}
}