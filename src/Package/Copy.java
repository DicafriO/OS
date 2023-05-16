package Package;

import java.util.ArrayList;
import java.util.List;

//프로세스 값을 카피하여 복사본을 만드는 클래스
public class Copy {
	public static List<ProcessInfo> Copylist(List<ProcessInfo> OriginalList) {
        @SuppressWarnings("rawtypes")
		List<ProcessInfo> copyList = new ArrayList();
        for (ProcessInfo copyProcess : OriginalList) {
            copyList.add(new ProcessInfo(copyProcess.getProcessPid(), copyProcess.getProcessArriveTime(), copyProcess.getProcessBurstTime(), copyProcess.getProcessPriority()));
        }  
        return copyList;
    }
}
