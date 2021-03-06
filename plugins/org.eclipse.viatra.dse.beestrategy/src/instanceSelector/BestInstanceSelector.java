package instanceSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.viatra.dse.beestrategy.SearchTrajectory;
import org.eclipse.viatra.dse.beestrategy.StrategyCombiner;

public class BestInstanceSelector implements IInstanceSelector{

	
	@Override
	public void selectInstances(StrategyCombiner combiner) {
		Collections.sort(combiner.getPatches(), new Comparator<SearchTrajectory>() {

			@Override
			public int compare(SearchTrajectory o1, SearchTrajectory o2) {
				return combiner.getContext().getObjectiveComparatorHelper().compare(o1.getOwnfitness(), o2.getOwnfitness());
			}

		

		});

		combiner.setBestpatches( new ArrayList<SearchTrajectory>());
		int length = combiner.getBestSitesNum();
		int sizeOfPatches = combiner.getPatchSize();
		if (length > sizeOfPatches)
			length = sizeOfPatches;

		if (combiner.getAlwaysnew() == false) {
			for (int i = 0; i < length; i++) {
				combiner.getBestpatches().add(combiner.getPatches().get(i));
//				System.out.println("fitness: "+patches.get(i).getOwnfitness());

			}
		} else {
			Iterator<SearchTrajectory> it = combiner.getPatches().iterator();
			while (length > 0 && it.hasNext()) {
				SearchTrajectory sd = it.next();
				if (sd.getHasChild() == false) {
					combiner.getBestpatches().add(sd);
					length--;
				}
			}
		}

		
	}
	
}
