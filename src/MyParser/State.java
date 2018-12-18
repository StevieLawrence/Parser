package MyParser;

import java.util.HashMap;
import java.util.Map;

public enum State {
	Start(0), Identifier(1), Number(2), Operator(3), Assignment(4), Error(5);

	private int index;
	private static Map map = new HashMap<>();

	private State(int index) {
		this.index = index;
	}

	static {
		for (State s : State.values()) {
			map.put(s.index, s);
		}
	}

	int getIndex() {
		return index;
	}

	static State valueOf(int index) {
		return (State) map.get(index);
	}

}
