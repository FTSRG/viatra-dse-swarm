package org.eclipse.viatra.dse.beestrategy.createbeestrategy;

import java.util.HashSet;

import org.eclipse.viatra.dse.api.strategy.interfaces.IStrategy;
import org.eclipse.viatra.dse.base.DesignSpaceManager;
import org.eclipse.viatra.dse.base.ThreadContext;
import org.eclipse.viatra.dse.beestrategy.BeeStrategy3;
import org.eclipse.viatra.dse.beestrategy.Patch;
import org.eclipse.viatra.dse.beestrategy.SearchData;
import org.eclipse.viatra.dse.beestrategy.StupidBee;
import org.eclipse.viatra.dse.beestrategy.StupidBee.BeeType;
import org.eclipse.viatra.dse.designspace.api.IState;
import org.eclipse.viatra.dse.designspace.api.ITransition;
import org.eclipse.viatra.dse.designspace.api.TrajectoryInfo;
import org.eclipse.viatra.dse.objectives.TrajectoryFitness;

public abstract class WorkerBeeStrategy implements ICreateBee {

	protected BeeStrategy3 bs;
	protected SearchData searchData;
	protected HashSet<IState> statesInTrajectory = new HashSet<IState>();

	protected ThreadContext context;
	protected DesignSpaceManager dsm;

	protected Integer radius;
	protected boolean interrupted;

	@Override
	public void setSearchData(SearchData sd){
		this.searchData = sd;
		radius = sd.getRadiusSize();
	}
	
	protected WorkerBeeStrategy(BeeStrategy3 bs) {
		this.bs = bs;
	}

	@Override
	public void setStatesInTrajectory(HashSet<IState> states) {
		this.statesInTrajectory = states;
	}

	@Override
	public void initStrategy(ThreadContext context) {
		this.context = context;
		this.dsm = context.getDesignSpaceManager();

	}

	@Override
	public void explore() {
		if (this.searchData == null || this.searchData.getActualState() == null
				|| this.searchData.getRadiusSize() == null || this.searchData.getRadiusSize() <= 0) {
			this.bs.decreasenumberOfActiveBees();
			return;
		}
		SearchData sd = createBee();
		if (sd == null) {
			this.bs.decreasenumberOfActiveBees();
			return;
		}
		;

	}

	@Override
	public SearchData createBee() {
		if (this.searchData == null || this.searchData.getActualState() == null
				|| this.searchData.getRadiusSize() == null || this.searchData.getRadiusSize() <= 0) {
			this.bs.decreasenumberOfActiveBees();
			return null;
		}
		SearchData sd = createBee();
		if (sd == null) {
			this.bs.decreasenumberOfActiveBees();
			return null;
		}
		;

		boolean start = true;
		Integer deepness = 0;

		radius = searchData.getRadiusSize();
		// step patchsize many steps
		int originalpatchSize = radius;
		for (int i = 0; i < originalpatchSize; i++) {
		
				ITransition nextTran = this.selectNextTransition();
				while (nextTran == null && deepness >= 0) {
					dsm.undoLastTransformation();
					deepness--;
					nextTran = this.selectNextTransition();
				}
				if (deepness == 0 && start == false) {
					return null;
				}
				start = false;
				dsm.fireActivation(nextTran);
				if(isAlreadyFoundInThisTrajectory(dsm.getCurrentState())){
					dsm.undoLastTransformation();
					deepness--;
				}
				deepness++;
		
		}
		this.searchData.setActualState(context.getDesignSpaceManager().getTrajectoryInfo());
		this.searchData.setOwnfitness(context.calculateFitness());
		TrajectoryFitness tf = new TrajectoryFitness(searchData.getActualState(), context.getLastFitness());
		this.searchData.setOwntrajectoryFitness(tf);
		return searchData;

	}

	public boolean isAlreadyFoundInThisTrajectory(IState currentState) {
		if (this.statesInTrajectory.add(currentState))
			return false;
		return true;
	}

	abstract ITransition selectNextTransition();

	public ThreadContext getContext() {
		return context;
	}

	public void setContext(ThreadContext context) {
		this.context = context;
	}
	
	@Override
	public synchronized void interruptStrategy() {
		this.interrupted = true;

	}
	
	@Override
	public void setMainStrategy(IStrategy beeStrategy) {
		this.bs = (BeeStrategy3) beeStrategy;

	}
	

}
