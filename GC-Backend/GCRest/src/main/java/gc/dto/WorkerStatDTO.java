package gc.dto;

import gc.model.Worker;

public class WorkerStatDTO {
	private Worker worker;
	private int workingHours;

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public int getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(int workingHours) {
		this.workingHours = workingHours;
	}
}
