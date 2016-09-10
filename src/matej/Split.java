package matej;

import java.util.List;
import matej.Instance;
import tools.Pair;

public class Split extends Pair<List<Instance>, List<Instance>> {

	public final List<Instance> yes;
	public final List<Instance> no;

	public Split(List<Instance> yes, List<Instance> no) {
		super(yes, no);
		this.yes = yes;
		this.no = no;
	}
}
